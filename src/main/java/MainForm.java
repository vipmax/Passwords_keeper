import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by max on 26.03.2014.
 */

public class MainForm extends JFrame {


    DefaultListModel<Unit> listModel;
    private Change change;
    private JList unitsList;
    private JButton addUnitButton;
    private JTextField passTextField;
    private JTextField unitTextField;
    private JPanel rootPanel;
    private JButton deleteSelectedUnit;
    private JButton showButton;
    private JButton copyToBufferButton;
    private JButton changeButton;
    private JLabel infoLabel;

    private List<Unit> saveBufferUnit = new ArrayList<Unit>();
    private List<Unit> deleteBufferUnit = new ArrayList<Unit>();
    private List<Unit> editBufferUnit = new ArrayList<Unit>();
    private Boolean isUnitsShown = false;
    private Dao dao;
    private Thread thread;

    public MainForm() {
        init();
        listModel = new DefaultListModel();
        unitsList.setModel(listModel);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (!saveBufferUnit.isEmpty())
                    dao.saveListUnit(saveBufferUnit);
                if (!deleteBufferUnit.isEmpty()) {
                    deleteBufferUnit.forEach(unit -> dao.deleteUnit(unit));
                }
                if (!editBufferUnit.isEmpty()) {
                    editBufferUnit.forEach(unit -> dao.editPasswordUnit(unit));
                }

                System.exit(0);
            }
        });

        addUnitButton.addActionListener(e -> {
            if (!unitTextField.toString().isEmpty() && !passTextField.getText().isEmpty()) {
                Unit unit = new Unit(unitTextField.getText(), passTextField.getText());
                addUnitToBuffer(unit);
            }
        });
        passTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    if (!unitTextField.toString().isEmpty() && !passTextField.getText().isEmpty()) {
                        Unit unit = new Unit(unitTextField.getText(), passTextField.getText());
                        addUnitToBuffer(unit);
                    }
            }
        });


        deleteSelectedUnit.addActionListener(e -> MainForm.this.deleteSelectedUnit());
        unitsList.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DELETE)
                    deleteSelectedUnit();
            }
        });
        showButton.addActionListener(e -> {
            if (isUnitsShown) {
                unitsList.setVisible(false);
                isUnitsShown = false;
            } else {
                unitsList.setVisible(true);
                unitsList.updateUI();
                isUnitsShown = true;

            }


        });
        copyToBufferButton.addActionListener(e -> MainForm.this.setClipboard(listModel.get(unitsList.getSelectedIndex()).getPassword()));

        changeButton.addActionListener(e -> {
            if (unitsList.isSelectionEmpty()) {
                return;
            }
            Unit unit = listModel.get(unitsList.getSelectedIndex());
            editUnit(unit);
        });
    }

    private void addUnitToBuffer(Unit unit) {
        if (saveBufferUnit.contains(unit) || dao.isContain(unit)) {
            animateInfoLabel("Already exist");
            return;
        }

        listModel.addElement(unit);
        saveBufferUnit.add(unit);
        animateInfoLabel("Adding successful");
    }
    private void deleteSelectedUnit() {
        int[] indexs = unitsList.getSelectedIndices();

        for (int index : indexs) {
            Unit unit = listModel.get(index);
            if (saveBufferUnit.size() > 0 && saveBufferUnit.contains(unit)) {
                saveBufferUnit.remove(unit);
                listModel.remove(index);
                animateInfoLabel("Deleting successful");
                System.out.println("Удалено из буфера сохранения");
                return;
            }
            deleteBufferUnit.add(unit);
            animateInfoLabel("Deleting successful");
            listModel.remove(index);

        }


    }

    private void editUnit(Unit unit) {
        change = new Change(unit);
        Unit changingUnit = change.getChangingUnit();
        if (changingUnit.getName() != unit.getName()) {
            deleteBufferUnit.add(unit);
            saveBufferUnit.add(changingUnit);
            listModel.removeElement(unit);
            listModel.addElement(changingUnit);
            System.out.println("unit = " + unit);
            System.out.println("changingUnit = " + changingUnit);
        }

        unitsList.updateUI();
        if (saveBufferUnit.contains(unit)) {
            saveBufferUnit.set(saveBufferUnit.indexOf(unit), unit);
            animateInfoLabel("Changing successful");
            System.out.println("Изменено в буфере");
            return;
        }

        editBufferUnit.add(changingUnit);
    }

    private void animateInfoLabel(String text) {
        infoLabel.setText(text);

        if (thread != null) {
            thread.interrupt();
        }
        thread = new Thread(() -> {

            try {


                for (int i = 0; i < 255; i++) {
                    infoLabel.setForeground(new Color(255, 0, 0, i));
                    Thread.sleep(10);
                }
                for (int i = 255; i > 0; i--) {
                    infoLabel.setForeground(new Color(i, 0, 0, i));

                    Thread.sleep(5);
                }

                infoLabel.setText("");

            } catch (InterruptedException e) {
                System.out.println("Прерывание потока");
            }

        });
        thread.start();
    }


    private void init() {
        this.setLocation(300, 300);
        setContentPane(rootPanel);
        setTitle("Password Keeper");
        setVisible(true);
        unitsList.setVisible(false);
        unitsList.updateUI();
        infoLabel.setOpaque(true);


        for (int i = 50; i < 300; i += 5) {
            this.setSize(600, i);
        }

        showButton.setOpaque(true);


        animateInfoLabel("Welcome");
    }

    public void loadListModel(List<Unit> units) {

        units.forEach(listModel::addElement);

    }

    public void setClipboard(String str) {
        StringSelection ss = new StringSelection(str);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
    }

    public void setDao(Dao dao) {
        this.dao = dao;
    }
}
