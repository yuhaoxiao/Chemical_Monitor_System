package cn.nju.edu.chemical_monitor_system.constant;

public enum UserTypeEnum {
    OPERATOR(0, "操作员"),
    MANAGER(1, "管理员");

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
