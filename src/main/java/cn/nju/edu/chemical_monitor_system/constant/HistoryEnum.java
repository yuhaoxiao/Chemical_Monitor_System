package cn.nju.edu.chemical_monitor_system.constant;

public enum HistoryEnum {
    REACTANT(0,"反应物"),
    PRODUCT(1,"产品");

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
