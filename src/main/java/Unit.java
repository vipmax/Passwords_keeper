/**
 * Created by max on 26.03.2014.
 */
public class Unit {
    private String site;
    private String login;
    private String password;


    public Unit(String site, String login,String password) {
        this.site = site;
        this.login = login;
        this.password = password;
    }

    public Unit() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
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
        for (int i = 0; i < password.length(); i++) str = str + "*";

        return site + "  -  " + login + " " + str;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Unit unit = (Unit) o;

        if (login != null ? !login.equals(unit.login) : unit.login != null) return false;
        if (site != null ? !site.equals(unit.site) : unit.site != null) return false;
        if (password != null ? !password.equals(unit.password) : unit.password != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = site != null ? site.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (login != null ? login.hashCode() : 0);
        return result;
    }
}
