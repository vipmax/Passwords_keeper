import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * Created by max on 25.03.2014.
 */
public class Main {

    AbstractXmlApplicationContext ac;
    DbDao dao;

    MainForm form;

    public static void main(String[] args) {
        Main main = new Main();
        main.ac = new ClassPathXmlApplicationContext("spring-config.xml");
        main.dao = (DbDao) main.ac.getBean("DbDaoImp");
        main.dao.createDB();
        main.form = (MainForm) main.ac.getBean("form");
        main.form.setDao(main.dao);
        main.form.loadListModel(main.dao.getAllUnit());
    }



}
