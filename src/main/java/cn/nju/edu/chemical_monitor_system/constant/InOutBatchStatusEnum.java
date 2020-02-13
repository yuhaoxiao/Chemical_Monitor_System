package cn.nju.edu.chemical_monitor_system.constant;

public enum InOutBatchStatusEnum {
    NOT_START(0, "未开始"),
    COMPLETED(1, "已完成"),
    ERROR(2, "出错");

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
