package io.dcloud.H58E83894.data;

/**
 * Created by fire on 2017/8/11  14:28.
 */

public class PayDatas {


    /**
     * code : 1
     * message : 购买成功
     */

    private int code;
    private String message;

    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    private int orderId;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
