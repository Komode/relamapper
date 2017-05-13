package relamapper.model;

import java.util.Date;

public class AccountInfo {

    private String uuid;
    private String email;
    private String type;
    private Date registeredDate;
    private boolean disabled;
    private boolean locked;
    private Date disabledDate;

    public AccountInfo() {}

    public AccountInfo(Account account) {
        this.uuid = account.getUuid();
        this.email = account.getEmail();
        this.type = account.getType();
        this.registeredDate = account.getRegisteredDate();
        this.disabled = account.isDisabled();
        this.locked = account.isLocked();
        this.disabledDate = account.getDisabledDate();
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(Date registeredDate) {
        this.registeredDate = registeredDate;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public Date getDisabledDate() {
        return disabledDate;
    }

    public void setDisabledDate(Date disabledDate) {
        this.disabledDate = disabledDate;
    }
}
