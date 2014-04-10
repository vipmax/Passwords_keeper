/**
 * Created by max on 26.03.2014.
 */
public class Unit {
    private String name;
    private String password;

    public Unit(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public Unit() {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        String str = "";
        for (int i = 0; i < password.length(); i++) {
            str = str + "*";
        }
        return  name + "  -  " + str;
    }
}
