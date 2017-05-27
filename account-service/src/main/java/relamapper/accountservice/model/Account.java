package relamapper.accountservice.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @Column(unique = true, updatable = false)
    private String uuid;

    @NotNull
    @Column(unique = true)
    private String email;

    @NotNull
    @Column
    private String password;

    @NotNull
    @Column
    private String role;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date registered;

    @NotNull
    @Column
    private boolean enabled;

    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date disabledDate;

    public Account() {}

    public Account(String email, String password, String role) {
        generateUUID();
        this.email = email;
        this.password = password;
        this.role = role;
        registered = new Date();
        enabled = true;
    }

    public String generateUUID() {
        uuid = UUID.randomUUID().toString();
        return uuid;
    }

    public void enable() {
        if(!enabled) {
            disabledDate = null;
            enabled = true;
        }
    }

    public void disable() {
        if(enabled) {
            disabledDate = new Date();
            enabled = false;
        }
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

    @Override
    public String toString() {
        return "Account{" +
                "uuid='" + uuid + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", registered=" + registered +
                ", enabled=" + enabled +
                ", disabledDate=" + disabledDate +
                '}';
    }
}
