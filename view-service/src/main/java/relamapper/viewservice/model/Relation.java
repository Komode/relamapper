package relamapper.viewservice.model;

import java.util.Map;

public class Relation {

    private String id;
    private String uuid;
    private String theirType;
    private String masterType;

    public Relation() {}

    public Relation(Map<String,String> relation) {
        id = relation.get("relation_id");
        uuid = relation.get("their_uuid");
        theirType = relation.get("their_type");
        masterType = relation.get("master_type");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTheirType() {
        return theirType;
    }

    public void setTheirType(String theirType) {
        this.theirType = theirType;
    }

    public String getMasterType() {
        return masterType;
    }

    public void setMasterType(String masterType) {
        this.masterType = masterType;
    }
}
