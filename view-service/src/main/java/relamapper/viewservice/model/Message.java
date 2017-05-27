package relamapper.viewservice.model;

public class Message {

    private String status;
    private String result;

    public Message() {}
    public Message(String s, String r) {
        status = s;
        result = r;
    }

    public void setError(String result) {
        status = "error";
        this.result = result;
    }

    public void setUnauthorized() {
        status = "error";
        result = "unauthorized";
    }

    public void setSuccess(String result) {
        status = "success";
        this.result = result;
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

