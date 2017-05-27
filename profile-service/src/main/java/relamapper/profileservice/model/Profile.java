package relamapper.profileservice.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Random;

@Entity
@Table(name = "profiles")
public class Profile {

    @Id
    @Column(unique = true, updatable = false)
    private String uuid;

    @NotNull
    @Column(unique = true, updatable = false)
    private String suid; // Simple User IDentificator

    @NotNull
    @Column
    private String firstname;

    @NotNull
    @Column
    private String lastname;

    @Column
    private String middlename;

    public Profile() {}

    public Profile(String uuid) {
        this.uuid = uuid;
        firstname = "";
        lastname = "";
        middlename = "";
        generateSUID();
    }

    private String SUIDGenerator() {
        char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for(int i = 0; i < 8; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString();
    }

    public String generateSUID() {
        suid = SUIDGenerator();
        return suid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getSuid() {
        return suid;
    }

    public void setSuid(String suid) {
        this.suid = suid;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Profile profile = (Profile) o;

        if (uuid != null ? !uuid.equals(profile.uuid) : profile.uuid != null) return false;
        return suid != null ? suid.equals(profile.suid) : profile.suid == null;
    }

    @Override
    public int hashCode() {
        int result = uuid != null ? uuid.hashCode() : 0;
        result = 31 * result + (suid != null ? suid.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "uuid='" + uuid + '\'' +
                ", suid='" + suid + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", middlename='" + middlename + '\'' +
                '}';
    }
}
