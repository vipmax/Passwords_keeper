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

    public void setCrypt(CryptImpl crypt) {
        this.crypt = crypt;
    }


    @Override
    public void createTableIfNotExist() {

        jdbcTemplate.update("CREATE TABLE IF NOT EXISTS sites (site TEXT NOT NULL,login TEXT NOT NULL,password TEXT NOT NULL)");
        System.out.println("Таблица с паролями создана");
    }

    @Override
    public void saveUnit(Unit unit) {
        int rowUpdated = jdbcTemplate.update("INSERT INTO sites   values (?,?,?)", unit.getSite(),unit.getLogin(), unit.getPassword());
        System.out.println("Добавлено в sites: " + unit.getSite() + " " + unit.getLogin() + " " + unit.getPassword() + " строчек: " + rowUpdated);
    }

    @Override
    public void saveListUnit(List<Unit> units) {

        BatchPreparedStatementSetter batchPreparedStatementSetter = new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                Unit unit = units.get(i);
                preparedStatement.setString(1, unit.getSite());
                preparedStatement.setString(2, unit.getLogin());
                preparedStatement.setString(3, crypt.encrypt(unit.getPassword()));
            }
            @Override
            public int getBatchSize() {
                return units.size();
            }
        };

        jdbcTemplate.batchUpdate("INSERT INTO sites  values (?,?,?)", batchPreparedStatementSetter);
        System.out.println("Вставлено в sites = " + units.size());
    }

    @Override
    public List<Unit> getAllUnit() {
        String sql = "SELECT * FROM sites";

        List<Unit> query = jdbcTemplate.query(sql, (resultSet, i) -> new Unit(resultSet.getString(1), resultSet.getString(2), crypt.decrypt(resultSet.getString(3))));
        System.out.println("Таблица sites загружена в память");
        return query;


    }

    @Override
    public void deleteUnit(Unit unit) {
        int update = jdbcTemplate.update("delete from sites where sites.site = ? AND sites.password = ? ", unit.getSite(), crypt.encrypt(unit.getPassword()));
        System.out.println("Удалено из sites" + update + " строк");
    }

    @Override
    public void editPasswordUnit(Unit unit) {
        System.out.println("Изменяем  в таблице sites " + unit.getSite() + " " + unit.getPassword());
        jdbcTemplate.execute("update sites set password = '" + crypt.encrypt(unit.getPassword()) + "' where  name is '" + unit.getSite() + "'");
    }

    @Override
    public boolean isContain(Unit unit) {
        int count = jdbcTemplate.queryForInt("select count(*) from sites where site = ?  and login = ? and password = ?", unit.getSite(),unit.getLogin(), crypt.encrypt(unit.getPassword()));
        return count > 0;
    }


    @Override
    public boolean isCreateMasterPasswordTable() {
        try {
            jdbcTemplate.update("CREATE TABLE master_password (name TEXT NOT NULL,password TEXT NOT NULL)");
        } catch (Exception e) {
            System.out.println("Таблица мастер пароля уже была создана ранее");
            return false;
        }
        System.out.println("Таблица мастер пароля создана");
        return true;
    }

    @Override
    public boolean checkMasterPassword(String mail, String pass) {
        boolean b = jdbcTemplate.queryForInt("select count(*) from master_password where name = ?  and password = ?",
                mail, crypt.MD5(pass)) > 0;
        System.out.println("Проверка пароля = " + b);
        return b;
    }

    @Override
    public void createMasterPassword(String email, String password) {
        crypt.setKey(crypt.MD5(password));
        jdbcTemplate.update("insert into master_password values (?,?)", email, crypt.MD5(password));
        System.out.println("Мастер пароль создан");
    }

}
