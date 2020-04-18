package cn.nju.edu.chemical_monitor_system.constant;

public enum HistoryEnum {
    PRODUCT(1, "产品"),
    BATCH(2, "批次");

    private int code;

    private String name;

    HistoryEnum(int code, String name) {
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
