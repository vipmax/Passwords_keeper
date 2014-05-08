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

    private List<Unit> saveBufferUnit = new ArrayList<Unit>();
    private List<Unit> deleteBufferUnit = new ArrayList<Unit>();
    private List<Unit> editBufferUnit = new ArrayList<Unit>();
    private Boolean isUnitsShown = false;
    private Dao dao;

    public MainForm() {
        init();
        listModel = new DefaultListModel();
        unitsList.setModel(listModel);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                removeExcess();
                if (!saveBufferUnit.isEmpty())
                    saveBufferUnit.forEach(unit -> dao.saveUnit(unit));
                if (!deleteBufferUnit.isEmpty()) {
                    deleteBufferUnit.forEach(unit -> dao.deleteUnit(unit));
                }
                if (!editBufferUnit.isEmpty()) {
                    editBufferUnit.forEach(unit -> dao.editUnit(unit));
                }

                System.exit(0);
            }
        });

        addUnitButton.addActionListener(e -> MainForm.this.addUnitToList());
        passTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    addUnitToList();
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
            change = new Change(unit);
            unitsList.updateUI();
            editBufferUnit.add(unit);
        });
    }

    private void init() {
        setContentPane(rootPanel);
        setTitle("Password Keeper");
        setVisible(true);
        pack();
        setSize(700, 400);
        unitsList.setVisible(false);
        unitsList.updateUI();
    }

    public void setDao(Dao dao) {
        this.dao = dao;
    }

    private void deleteSelectedUnit() {
        int[] indexs = unitsList.getSelectedIndices();

        for (int index : indexs) {
            deleteBufferUnit.add(listModel.get(index));
            listModel.remove(index);
        }


    }

    private void addUnitToList() {
        if (!unitTextField.toString().isEmpty() && !passTextField.getText().isEmpty()) {
            Unit unit = new Unit(unitTextField.getText(), passTextField.getText());
            listModel.addElement(unit);
            saveBufferUnit.add(unit);
        }
    }

    public void loadListModel(List<Unit> units) {

        units.forEach(listModel::addElement);

    }

    public void removeExcess() {
        for (int i = 0; i < saveBufferUnit.size(); i++) {
            for (int j = 0; j < deleteBufferUnit.size(); j++) {

                if (deleteBufferUnit.get(j).getName().equals(saveBufferUnit.get(i).getName())
                        && deleteBufferUnit.get(j).getPassword().equals(saveBufferUnit.get(i).getPassword())) {
                    System.out.println("Ложный юнит " + deleteBufferUnit.get(j));
                    deleteBufferUnit.remove(j);
                    saveBufferUnit.remove(i);
                }
            }
        }

    }

    public void setClipboard(String str) {
        StringSelection ss = new StringSelection(str);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
    }

}
