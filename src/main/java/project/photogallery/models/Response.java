package project.photogallery.models;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class Response {

    private String message;
    private Integer code;

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public Integer getCode() {
        return code;
    }
    public void setCode(Integer code) {
        this.code = code;
    }

    public JsonObject toJson() {
        return Json.createObjectBuilder()
            .add("message", message)
            .add("code", code)
            .build();
    }
    
    
}
