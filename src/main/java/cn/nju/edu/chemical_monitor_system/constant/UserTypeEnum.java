package cn.nju.edu.chemical_monitor_system.constant;


public enum UserTypeEnum {
    OPERATOR(1, "operator"),
    ADMINISTRATOR(2, "administrator"),
    MONITOR(3, "monitor");

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

    public static String getRole(String type) {
        switch (type) {
            case "1":
                return "operator";
            case "2":
                return "administrator";
            case "3":
                return "monitor";
            default:
                return "null";
        }
    }

}
