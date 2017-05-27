package relamapper.viewservice.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RelationContainer {

    private String uuid;
    private List<Relation> relations = new ArrayList<>();

    public RelationContainer() {}

    public RelationContainer(String uuid) {
        this.uuid = uuid;
    }

    public void addRelation(Map<String, String> relation) {
        relations.add(new Relation(relation));
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public List<Relation> getRelations() {
        return relations;
    }

    public void setRelations(List<Relation> relations) {
        this.relations = relations;
    }
}
