import java.util.List;

/**
 * Created by max on 26.03.2014.
 */
public interface Dao {
    void createTableIfNotExist();
    void saveUnit(Unit unit);
    void saveListUnit(List<Unit> units);
    List<Unit> getAllUnit();
    void deleteUnit(Unit unit);

    void editPasswordUnit(Unit unit);

    boolean isContain(Unit unit);

    boolean isCreateMasterPasswordTable();

    boolean checkMasterPassword(String mail, String pass);

    void createMasterPassword(String email, String password);
}
