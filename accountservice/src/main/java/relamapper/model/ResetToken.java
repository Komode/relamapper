package relamapper.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

@Entity
@Table(name = "reset_tokens")
public class ResetToken {

    @Id
    @NotNull
    @Column(nullable = false, unique = true, updatable = false)
    private String token;

    @NotNull
    @Column(nullable = false)
    private String uuid;

    @NotNull
    @Column(nullable = false)
    private boolean used;

    @NotNull
    @Column(nullable = false)
    private boolean valid;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date created;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date expires;

    public ResetToken() {}

    public ResetToken(String uuid) {
        this.uuid = uuid;
        generateToken();
        used = false;
        valid = true;
        created = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(created);
        c.add(Calendar.HOUR, 1);
        expires = c.getTime();
    }

    public String generateToken() {
        char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFHIJKLMNOPQRSTUVWXYZ1234567890".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random rand = new Random();
        for(int i = 0; i < 20; i++) {
            char c = chars[rand.nextInt(chars.length)];
            sb.append(c);
        }
        token = sb.toString();
        return token;
    }

    public boolean checkValidUUID(String uuid) {
        if(used || !valid)
            return false;
        if(!this.uuid.equals(uuid))
            return false;
        return expires.after(new Date());
    }

    public void setInvalid() {
        this.valid = false;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getExpires() {
        return expires;
    }

    public void setExpires(Date expires) {
        this.expires = expires;
    }
}
