package cn.nju.edu.chemical_monitor_system.constant;

public enum ExpressStatusEnum {
    NOT_START(0, "未开始"),
    OUT_INVENTORY_ING(4,"出库中"),
    OUT_INVENTORY(1, "已出库"),
    IN_INVENTORY(2, "已入库"),
    ERROR(3, "出错");

    private int code;

    private String name;

    ExpressStatusEnum(int code, String name) {
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
