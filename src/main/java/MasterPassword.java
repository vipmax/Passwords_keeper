import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by admin on 10.05.14.
 */
public class MasterPassword extends JFrame {
    private static String login;
    boolean isAutorize = false;
    private JButton okButton;
    private JButton exitButton;
    private JPanel rootPanel;
    private JTextField emailTF;
    private JPasswordField passwordTF;
    private JLabel masterPasswordLabel;
    private Dao dao;
    private Thread thread;
    private boolean isnotCreateMasterPassword;

    public MasterPassword(Thread thread) {
        this.thread = thread;
        setLocation(MainForm.xLoc + MainForm.height / 2 - 100, MainForm.yLoc + MainForm.width / 2 - 50);
        setContentPane(rootPanel);
        pack();
        setVisible(true);


        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        okButton.addActionListener(e -> {
            String email = emailTF.getText();
            String password = passwordTF.getText();

            if (email.isEmpty() && password.isEmpty()) return;
            if (isnotCreateMasterPassword) {
                login = email;
                showMainForm();
                new Thread(() -> {
                    dao.createMasterPassword(email, password);
                }).start();
                return;
            }
            if (dao.checkMasterPassword(email, password)) {
                login = email;
                showMainForm();

            }

        });
        exitButton.addActionListener(e -> {
            System.exit(ABORT);
        });
        System.out.println("Диалог мастер пароля создан");

    }

    public static String getLogin() {
        return login;
    }

    private void showMainForm() {
        thread.interrupt(); //показываем главное окно
        dispose();
    }

    public void defineState() {

        isnotCreateMasterPassword = dao.isCreateMasterPasswordTable();
        if (isnotCreateMasterPassword) {

            masterPasswordLabel.setText("Create master password");
            return;
        }
        masterPasswordLabel.setText("Enter master password");
    }

    public void setDao(Dao dao) {
        this.dao = dao;
    }
}

