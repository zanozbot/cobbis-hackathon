package si.zanozbot.cobisshackathon;

public class DataModel {

    private String message;
    private String number;

    public DataModel(String message, String number) {
        this.message = message;
        this.number = number;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
