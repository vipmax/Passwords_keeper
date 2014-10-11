import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by max on 25.03.2014.
 */
public class Main {

    AbstractXmlApplicationContext ac;
    Dao dao;
    MainForm form;


    public static void main(String[] args) {


        MasterPasswordForm masterPasswordForm = new MasterPasswordForm(Thread.currentThread());
        Main main = new Main();
        main.ac = new ClassPathXmlApplicationContext("spring-config.xml");

        main.dao = (Dao) main.ac.getBean("DbDaoImp");
        masterPasswordForm.setDao(main.dao);
        masterPasswordForm.defineState();

        new Thread(() -> {
            main.form = new MainForm();
            main.form.setDao(main.dao);
            main.dao.createTableIfNotExist();
            main.form.loadListModel(main.dao.getAllUnit());
        }).start();


        try {
            synchronized (Thread.currentThread()) {
                Thread.currentThread().wait(100000000);
            }
        } catch (InterruptedException e) {
            System.out.println("Все введено верно, открываю главное окно");
            main.form.shoow();
        }


    }


}
