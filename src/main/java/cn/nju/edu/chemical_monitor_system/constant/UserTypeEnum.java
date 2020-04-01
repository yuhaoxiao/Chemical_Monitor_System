package cn.nju.edu.chemical_monitor_system.constant;

public enum UserTypeEnum {
    OPERATOR(1, "操作员"),
    ADMINISTRATOR(2,"管理员"),
    MONITOR(3,"监控员");

    private int code;

    private String name;

    private UserTypeEnum(int code, String name) {
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
