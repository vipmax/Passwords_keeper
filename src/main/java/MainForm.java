import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by max on 26.03.2014.
 */

public class MainForm extends JFrame {


    private  Change change;
    List<Unit> saveBufferUnit = new ArrayList<Unit>();
    List<Unit> deleteBufferUnit = new ArrayList<Unit>();
    List<Unit> editBufferUnit = new ArrayList<Unit>();
    Boolean isUnitsShown = false;
    DbDao dao;
    DefaultListModel<Unit> listModel;
    private JList unitsList;
    private JButton addUnitButton;
    private JTextField passTextField;
    private JTextField unitTextField;
    private JPanel rootPanel;
    private JButton deleteSelectedUnit;
    private JButton showButton;
    private JButton copyToBufferButton;
    private JButton changeButton;

    public MainForm() {
        init();
        listModel = new DefaultListModel();
        unitsList.setModel(listModel);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                removeExcess();
                if (!saveBufferUnit.isEmpty())
                    saveBufferUnit.forEach(new Consumer<Unit>() {
                        @Override
                        public void accept(Unit unit) {
                            dao.saveUnit(unit);
                        }
                    });
                if (!deleteBufferUnit.isEmpty()) {
                    deleteBufferUnit.forEach(new Consumer<Unit>() {
                        @Override
                        public void accept(Unit unit) {
                            dao.deleteUnit(unit);
                        }
                    });
                }
                if (!editBufferUnit.isEmpty()) {
                    editBufferUnit.forEach(new Consumer<Unit>() {
                        @Override
                        public void accept(Unit unit) {
                            dao.editUnit(unit);
                        }
                    });
                }

                System.exit(0);
            }
        });

        addUnitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainForm.this.addUnitToList();
            }
        });
        passTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    addUnitToList();
            }
        });


        deleteSelectedUnit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainForm.this.deleteSelectedUnit();
            }
        });
        unitsList.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DELETE)
                    deleteSelectedUnit();
            }
        });
        showButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isUnitsShown) {
                    unitsList.setVisible(false);
                    isUnitsShown = false;
                } else {
                    unitsList.setVisible(true);
                    unitsList.updateUI();
                    isUnitsShown = true;

                }


            }
        });
        copyToBufferButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainForm.this.setClipboard(listModel.get(unitsList.getSelectedIndex()).getPassword());
            }
        });

        changeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (unitsList.isSelectionEmpty()) {
                    return;
                }
                Unit unit = listModel.get(unitsList.getSelectedIndex());
                change = new Change(unit);
                unitsList.updateUI();
                editBufferUnit.add(unit);
            }
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

    public void setDao(DbDao dao) {
        this.dao = dao;
    }

    private void deleteSelectedUnit() {
        int[] indexs = unitsList.getSelectedIndices();


        for (int index : indexs) {
//            System.out.println(index);
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

        units.forEach(new Consumer<Unit>() {
            @Override
            public void accept(Unit unit) {
                listModel.addElement(unit);
            }
        });

    }

    public void removeExcess() {


        for (int i = 0; i < saveBufferUnit.size(); i++) {
            for (int j = 0; j < deleteBufferUnit.size(); j++) {

                if (deleteBufferUnit.get(j).getName() == saveBufferUnit.get(i).getName()
                        && deleteBufferUnit.get(j).getPassword() == saveBufferUnit.get(i).getPassword()) {
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
