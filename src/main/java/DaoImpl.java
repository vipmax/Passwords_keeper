import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by max on 26.03.2014.
 */
public class DaoImpl implements Dao {
    private JdbcTemplate jdbcTemplate;
    private Crypt crypt;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public void createTableIfNotExist() {
        jdbcTemplate.update("CREATE TABLE IF NOT EXISTS passwords (name TEXT NOT NULL,password TEXT NOT NULL)");
    }

    @Override
    public void saveUnit(Unit unit) {

        int update = jdbcTemplate.update("INSERT INTO passwords   values (?,?)", unit.getName(), unit.getPassword());

        System.out.println("INSERT INTO passwords: " + unit.getName() + " " + unit.getPassword()+" rows: "+ update);
    }

    @Override
    public void saveListUnit(List<Unit> units) {
        if (crypt == null) {
            System.out.println("crypt null");
        }

        BatchPreparedStatementSetter batchPreparedStatementSetter = new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                Unit unit = units.get(i);
                preparedStatement.setString(1, unit.getName());
                preparedStatement.setString(2, crypt.encrypt(unit.getPassword()));

            }

            @Override
            public int getBatchSize() {
                return units.size();
            }
        };
        jdbcTemplate.batchUpdate("INSERT INTO passwords  values (?,?)", batchPreparedStatementSetter);
        System.out.println("insert units = " + units.size());
    }

    @Override
    public List<Unit> getAllUnit() {
        String sql = "SELECT * FROM passwords";
        return jdbcTemplate.query(sql, (resultSet, i) -> new Unit(resultSet.getString(1), crypt.decrypt(resultSet.getString(2))));


    }

    @Override
    public void deleteUnit(Unit unit) {
        System.out.println("Deleting "+unit.getName() + " " + unit.getPassword());
        int update = jdbcTemplate.update("delete from passwords where passwords.name = ? AND passwords.password = ? ", unit.getName(), crypt.encrypt(unit.getPassword()));
        System.out.println("delete "+ update + "rows");
    }

    @Override
    public void editPasswordUnit(Unit unit) {
        System.out.println("Editing "+unit.getName() + " " + unit.getPassword());
        jdbcTemplate.execute("update passwords SET password = '" + crypt.encrypt(unit.getPassword()) + "' where  name is '" + unit.getName() + "'");
    }

    @Override
    public boolean isContain(Unit unit) {
        int count = jdbcTemplate.queryForInt("select count(*) from passwords where name = ?  and password = ?", unit.getName(), crypt.encrypt(unit.getPassword()));
        return count > 0;
    }

    public void setCrypt(Crypt crypt) {
        this.crypt = crypt;
    }
}
