package com.taohong.snapup.result;

/**
 * @author taohong on 09/10/2018
 */
public class CodeMsg {
    private int code;
    private String msg;

    // General errors
    public static CodeMsg SUCCESS = new CodeMsg(0, "success");
    public static CodeMsg SERVER_ERROR = new CodeMsg(500100, "server error");
    public static CodeMsg BIND_ERROR = new CodeMsg(500101, "parameter validation error: %s");
    // Login errors 5002XX
    public static CodeMsg PASSWORD_EMPTY = new CodeMsg(500211, "password cannot be empty");
    public static CodeMsg MOBILE_EMPTY = new CodeMsg(500212, "phone number cannot be empty");
    public static CodeMsg MOBILE_ERROR = new CodeMsg(500213, "invalid phone number");
    public static CodeMsg MOBILE_NOT_EXIST = new CodeMsg(500214, "phone number does not exist");
    public static CodeMsg PASSWORD_ERROR = new CodeMsg(500215, "wrong password");

    // Item errors 5003XX

    // Order errors 5004XX

    // Snapup errors 5005XX
    public static CodeMsg SNAPUP_OVER = new CodeMsg(500500, "snap-up is over");
    public static CodeMsg REPEAT_SNAPUP = new CodeMsg(500501, "cannot repeat snap up one item");


    private CodeMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public CodeMsg fillArgs(Object... args) {
        int code = this.code;
        String message = String.format(this.msg, args);
        return new CodeMsg(code, message);
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}