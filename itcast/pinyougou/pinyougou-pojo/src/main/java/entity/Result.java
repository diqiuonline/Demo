package entity;

import java.io.Serializable;

/**
 * User: 李锦卓
 * Time: 2018/10/8 21:44
 * 返回结果封装
 */
public class Result implements Serializable {
    private Boolean success;
    private String message;
    public Result(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    public Boolean getSuccess() {
        return success;
    }
    public void setSuccess(Boolean success) {
        this.success = success;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}