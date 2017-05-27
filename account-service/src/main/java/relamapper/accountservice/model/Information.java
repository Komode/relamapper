package relamapper.accountservice.model;

import java.util.Date;

public class Information {

    private String uuid;
    private String email;
    private String role;
    private Date registered;
    private boolean enabled;
    private Date disabledDate;

    public Information() {}

    public Information(Account account) {
        uuid = account.getUuid();
        email = account.getEmail();
        role = account.getRole();
        registered = account.getRegistered();
        enabled = account.isEnabled();
        disabledDate = account.getDisabledDate();
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Date getRegistered() {
        return registered;
    }

    public void setRegistered(Date registered) {
        this.registered = registered;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Date getDisabledDate() {
        return disabledDate;
    }

    public void setDisabledDate(Date disabledDate) {
        this.disabledDate = disabledDate;
    }
}
