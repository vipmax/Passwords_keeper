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
        return name + "  -  " + str;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Unit unit = (Unit) o;

        if (name != null ? !name.equals(unit.name) : unit.name != null) return false;
        if (password != null ? !password.equals(unit.password) : unit.password != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }
}
