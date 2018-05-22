package JavaBean.Entity;

import java.io.Serializable;

public class User implements Serializable{
    private String usr_id;
    private String usrname;
    private String password;
    private int authority;

    public String getUsr_id() {
        return usr_id;
    }

    public String getUsrname() {
        return usrname;
    }

    public String getPassword() {
        return password;
    }

    public int getAuthority() {
        return authority;
    }

    public void setUsr_id(String usr_id) {
        this.usr_id = usr_id;
    }

    public void setUsrname(String usrname) {
        this.usrname = usrname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAuthority(int authority) {
        this.authority = authority;
    }
}
