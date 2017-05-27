package relamapper.profileservice.utilities;

import relamapper.profileservice.model.Message;

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

}
