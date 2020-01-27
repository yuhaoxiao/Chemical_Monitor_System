package cn.nju.edu.chemical_monitor_system.constant;

public enum ExpressStatusEnum {
    NOT_START(0, "未开始"),
    OUT_INVENTORY(1, "已出库"),
    IN_INVENTORY(2, "已入库");

    private int code;

    private String name;

    private ExpressStatusEnum(int code, String name) {
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
