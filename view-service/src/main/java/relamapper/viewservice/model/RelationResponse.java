package relamapper.viewservice.model;

import java.util.HashMap;
import java.util.Map;

public class RelationResponse {

    private Integer id;
    private String uuid1;
    private String uuid2;
    private String type1;
    private String type2;

    public RelationResponse() {}

    public String getUUIDRelation(String uuid) {
        if(uuid.equals(uuid1)) {
            return type1;
        } else if(uuid.equals(uuid2)) {
            return type2;
        } else {
            return null;
        }
    }

    public String getUUIDOfRelation(String uuid) {
        if(uuid.equals(uuid1)) {
            return uuid2;
        } else if(uuid.equals(uuid2)) {
            return uuid1;
        } else {
            return null;
        }
    }

    public Map<String, String> getRelationOf(String uuid) {
        Map<String, String> map = new HashMap<>();
        if(uuid.equals(uuid1)) {
            map.put("relation_id", String.valueOf(id));
            map.put("their_uuid", uuid2);
            map.put("their_type", type2);
            map.put("master_type", type1);
        } else if(uuid.equals(uuid2)) {
            map.put("relation_id", String.valueOf(id));
            map.put("their_uuid", uuid1);
            map.put("their_type", type1);
            map.put("master_type", type2);
        }
        return map;
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
