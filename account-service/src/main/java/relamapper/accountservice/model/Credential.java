package relamapper.accountservice.model;

public class Credential {

    private String uuid;
    private String password;
    private String role;
    private boolean enabled;

    public Credential() {}

    public Credential(Account account) {
        uuid = account.getUuid();
        password = account.getPassword();
        role = account.getRole();
        enabled = account.isEnabled();
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
