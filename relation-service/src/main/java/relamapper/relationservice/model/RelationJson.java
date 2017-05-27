package relamapper.relationservice.model;

public class RelationJson {

    private Integer id;
    private String uuid1;
    private String uuid2;
    private String type1;
    private String type2;

    public RelationJson() {}

    public RelationJson(Relation relation) {
        id = relation.getId();
        uuid1 = relation.getUuid1();
        uuid2 = relation.getUuid2();
        type1 = relation.getType1();
        type2 = relation.getType2();
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
