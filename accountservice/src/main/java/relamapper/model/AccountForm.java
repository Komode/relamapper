package relamapper.model;

public class AccountForm {

    private String email;

    private String password;

    private String type;

    /*************************
     * constructor
     *************************/
    public AccountForm() {}

    public AccountForm(String email, String password, String type) {
        setEmail(email);
        setPassword(password);
        setType(type);
    }

    /*************************
     * getters / setters
     *************************/
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        type = type.toLowerCase();
        if(!type.equals("owner") && !type.equals("administrator") && !type.equals("moderator") && !type.equals("member"))
            this.type = "member";

        this.type = type;

    }

    /*************************
     * toString
     *************************/
    @Override
    public String toString() {
        return "AccountForm{" +
                "email='" + email + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
