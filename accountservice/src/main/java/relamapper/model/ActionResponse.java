package relamapper.model;

public class ActionResponse {

    private String task;
    private String status;
    private String result;

    public ActionResponse() {}

    public ActionResponse(String task) {
        this.task = task;
    }

    public void setStatusSuccess() {
        status = "success";
    }

    public void setStatusFailure() {
        status = "failure";
    }

    public void setStatusError() {
        status = "error";
    }

    public void setResultInvalidParam() {
        result = "invalid parameter";
    }

    public void setResultUnauthorized() {
        result = "unauthorized";
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
