package relamapper.model;

import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @Column(nullable = false, unique = true, updatable = false)
    private String uuid;

    @Email
    @NotNull
    @Column(nullable = false, unique = true)
    private String email;

    @NotNull
    @Column(nullable = false)
    private String password;

    @NotNull
    @Column(nullable = false)
    private String type;    // member, moderator, administrator, owner

    @NotNull
    @Column(nullable = false)
    private boolean disabled = false;

    @NotNull
    @Column(nullable = false)
    private boolean locked = false;

    @NotNull
    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date registeredDate;

    @Temporal(TemporalType.DATE)
    @Column
    private Date disabledDate;

    /*************************
     * UUID generator
     *************************/
    private String UUIDGenerator() {
        return UUID.randomUUID().toString();
    }

    /*************************
     * constructors
     * 1. default empty
     * 2. "registration"
     *************************/
    public Account() {}

    public Account(AccountForm accountForm) {
        this.uuid = UUIDGenerator();
        this.email = accountForm.getEmail();
        this.password = accountForm.getPassword();
        this.type = accountForm.getType();
        this.registeredDate = new Date();
    }

    // function to regenerate UUID on collision
    public String regenUUID() {
        this.uuid = UUIDGenerator();
        return uuid;
    }

    /*************************
     * convenience methods
     *************************/
    public void disable() {
        disabled = true;
        disabledDate = new Date();
    }

    public void enable() {
        disabled = false;
        if(!locked)
            disabledDate = null;
    }

    public void lock() {
        locked = true;
        disabledDate = new Date();
    }

    public void unlock() {
        locked = false;
        if(!disabled)
            disabledDate = null;
    }


    /*************************
     * getters / setters
     *************************/
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
        this.type = type;
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

    public Date getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(Date registeredDate) {
        this.registeredDate = registeredDate;
    }

    public Date getDisabledDate() {
        return disabledDate;
    }

    public void setDisabledDate(Date disabledDate) {
        this.disabledDate = disabledDate;
    }

    /*************************
     * equals, hashcode
     *************************/
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        if (uuid != null ? !uuid.equals(account.uuid) : account.uuid != null) return false;
        return email != null ? email.equals(account.email) : account.email == null;
    }

    @Override
    public int hashCode() {
        int result = uuid != null ? uuid.hashCode() : 0;
        result = 31 * result + (email != null ? email.hashCode() : 0);
        return result;
    }

    /*************************
     * toString
     *************************/
    @Override
    public String toString() {
        return "Account{" +
                "uuid='" + uuid + '\'' +
                ", email='" + email + '\'' +
                ", type='" + type + '\'' +
                ", disabled=" + disabled +
                ", locked=" + locked +
                ", registeredDate=" + registeredDate +
                ", disabledDate=" + disabledDate +
                '}';
    }
}
