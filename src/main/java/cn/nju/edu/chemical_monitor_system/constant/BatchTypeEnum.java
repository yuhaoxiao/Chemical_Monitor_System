package cn.nju.edu.chemical_monitor_system.constant;

public enum BatchTypeEnum {
    PRODUCE(0, "生产"),
    IN_PARK(1, "入园"),
    DESTROY(3, "销毁"),
    OUT_PARK(2, "出园");

    private int code;

    private String name;

    BatchTypeEnum(int code, String name) {
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
