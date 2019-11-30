package si.zanozbot.cobisshackathon;

import com.google.gson.annotations.SerializedName;

public class CobissModel {

    @SerializedName("status")
    private Integer status;

    @SerializedName("message")
    private String message;

    public CobissModel(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
