import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by max on 25.03.2014.
 */
public class Main {

    AbstractXmlApplicationContext ac = new ClassPathXmlApplicationContext("spring-config.xml");
    Dao dao = (Dao) ac.getBean("DbDaoImp");

    MainForm form = (MainForm) ac.getBean("form");

    public static void main(String[] args) {
        Main main = new Main();

        main.dao.createTableIfNotExist();
        main.form.setDao(main.dao);
        main.form.loadListModel(main.dao.getAllUnit());
    }


}
