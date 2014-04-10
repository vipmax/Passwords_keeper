import java.util.List;

/**
 * Created by max on 26.03.2014.
 */
public interface DbDao {
    void createDB();
    void saveUnit(Unit unit);
    List<Unit> getAllUnit();
    void deleteUnit(Unit unit);
    void editUnit(Unit unit);
}
