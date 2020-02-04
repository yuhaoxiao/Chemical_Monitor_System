package cn.nju.edu.chemical_monitor_system.constant;

public enum BatchStatusEnum {
    NOT_START(0, "未开始"),
    IN_BATCH(1, "原材料上线"),
    IN_PROCESS(2, "在生产"),
    OUT_BATCH(3, "产品下线"),
    COMPLETE(4, "已完成"),
    ERROR(5, "出错");

    private int code;

    private String name;

    BatchStatusEnum(int code, String name) {
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
