package relamapper.relationservice.utilities;

import relamapper.relationservice.model.Message;
import relamapper.relationservice.model.Relation;
import relamapper.relationservice.model.RelationJson;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ControllerUtils {

    public Message defaultMessage() {
        return new Message("error", "invalid parameter");
    }

    public boolean validUUID(String uuid) {
        String sPattern = "^[\\d\\S]{8}-[\\d\\S]{4}-[\\d\\S]{4}-[\\d\\S]{4}-[\\d\\S]{12}$";
        if(!uuid.isEmpty()) {
            Pattern p = Pattern.compile(sPattern);
            Matcher m = p.matcher(uuid);
            return m.find();
        } else {
            return false;
        }
    }

    public List<RelationJson> transformToJson(List<Relation> relations) {
        List<RelationJson> list = new ArrayList<>(relations.size());
        for(Relation relation : relations) {
            list.add(new RelationJson(relation));
        }
        return list;
    }

}
