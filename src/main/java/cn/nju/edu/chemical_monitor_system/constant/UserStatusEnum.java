package cn.nju.edu.chemical_monitor_system.constant;

public enum UserStatusEnum {
    NOT_lOGIN(0, "未登录"),
    LOGIN(1, "已登录");

    private int code;

    private String name;

    private UserStatusEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }


    public int getCode() {
        return code;
    }


    public String getName() {
        return name;
    }
}
