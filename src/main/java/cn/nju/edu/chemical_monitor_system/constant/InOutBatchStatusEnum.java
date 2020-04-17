package cn.nju.edu.chemical_monitor_system.constant;

public enum InOutBatchStatusEnum {
    NOT_START(0, "未开始"),
    ING(1, "进行中"),
    COMPLETED(2, "已完成"),
    ERROR(3, "出错");

    private int code;

    private String name;

    InOutBatchStatusEnum(int code, String name) {
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
