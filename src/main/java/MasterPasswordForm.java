import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by admin on 10.05.14.
 */
public class MasterPasswordForm extends JFrame {
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
    private boolean isNotCreateMasterPassword;

    public MasterPasswordForm(Thread thread) {
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
            if (isNotCreateMasterPassword) {
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
            }else {
                masterPasswordLabel.setText("Неверный пароль. Введите заново");
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

        isNotCreateMasterPassword = dao.isCreateMasterPasswordTable();
        if (isNotCreateMasterPassword) {

            masterPasswordLabel.setText("Создайте мастер пароль");
            return;
        }
        masterPasswordLabel.setText("Мастер пароль найден. Введите");
    }

    public void setDao(Dao dao) {
        this.dao = dao;
    }
}

