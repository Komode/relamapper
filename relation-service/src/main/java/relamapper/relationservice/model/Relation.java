package relamapper.relationservice.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "relations")
public class Relation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "relationID", unique = true)
    private Integer id;

    @NotNull
    @Column
    private String uuid1;

    @NotNull
    @Column
    private String uuid2;

    @NotNull
    @Column
    private String type1;

    @NotNull
    @Column
    private String type2;

    public Relation() {
    }

    public void register(String uuid1, String uuid2, String type) {
        this.uuid1 = uuid1;
        this.uuid2 = uuid2;
        this.type1 = type;
        this.type2 = type;
    }

    public boolean updateRelation(String uuid, String type) {
        if(uuid.equals(uuid1)) {
            type1 = type;
            return true;
        } else if(uuid.equals(uuid2)) {
            type2 = type;
            return true;
        } else {
            return false;
        }
    }

    public String getUUIDRelation(String uuid) {
        if(uuid.equals(uuid1)) {
            return type1;
        } else if(uuid.equals(uuid2)) {
            return type2;
        } else {
            return null;
        }
    }

    public boolean partOf(String uuid) {
        return (uuid.equals(uuid1) || uuid.equals(uuid2));
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid1() {
        return uuid1;
    }

    public void setUuid1(String uuid1) {
        this.uuid1 = uuid1;
    }

    public String getUuid2() {
        return uuid2;
    }

    public void setUuid2(String uuid2) {
        this.uuid2 = uuid2;
    }

    public String getType1() {
        return type1;
    }

    public void setType1(String type1) {
        this.type1 = type1;
    }

    public String getType2() {
        return type2;
    }

    public void setType2(String type2) {
        this.type2 = type2;
    }
}
