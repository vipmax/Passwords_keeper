import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 * Created by max on 26.03.2014.
 */
public class DaoImpl implements Dao {
    private JdbcTemplate jdbcTemplate;

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
    public List<Unit> getAllUnit() {

        String sql = "SELECT * FROM passwords";

//        return jdbcTemplate.query(sql,new ParameterizedRowMapper<Unit>() {   also works
//            @Override
//            public Unit mapRow(ResultSet resultSet, int i) throws SQLException {
//                Unit  user = new Unit(resultSet.getString(1), resultSet.getString(2));
//                System.out.println(user);
//                return user;
//            }
//        });

        return jdbcTemplate.query(sql, (resultSet, i) -> new Unit(resultSet.getString(1), resultSet.getString(2)));


    }

    @Override
    public void deleteUnit(Unit unit) {
        System.out.println("Deleting "+unit.getName() + " " + unit.getPassword());
        int update = jdbcTemplate.update("delete from passwords where passwords.name = ? AND passwords.password = ? ", unit.getName(), unit.getPassword());
        System.out.println("delete "+ update + "rows");
    }

    @Override
    public void editUnit(Unit unit) {
        System.out.println("Editing "+unit.getName() + " " + unit.getPassword());

        jdbcTemplate.execute("update passwords SET password = '"+ unit.getPassword()+"' where  name is '"+ unit.getName()+"'");
    }

}
