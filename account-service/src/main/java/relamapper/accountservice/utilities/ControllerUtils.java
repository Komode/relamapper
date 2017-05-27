package relamapper.accountservice.utilities;

import relamapper.accountservice.model.Account;
import relamapper.accountservice.model.Information;
import relamapper.accountservice.model.Message;

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

    public List<Information> listTransformToInformation(List<Account> accounts) {
        List<Information> information = new ArrayList<>(accounts.size());
        for(Account account : accounts) {
            information.add(new Information(account));
        }
        return information;
    }

    public String normalizeRole(String role) {
        role = role.toLowerCase();
        switch (role) {
            case "owner":
            case "admin":
            case "moderator":
            case "member":
                return role;
            default:
                return "member";
        }
    }

}
