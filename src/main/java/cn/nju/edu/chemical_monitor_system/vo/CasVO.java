package cn.nju.edu.chemical_monitor_system.vo;

import cn.nju.edu.chemical_monitor_system.entity.CasEntity;
import lombok.Data;

import java.util.Objects;

@Data
public class CasVO {

    private int casId;
    private String name;
    private int code;
    private String message;

    public CasVO(CasEntity c) {
        if (c == null) {
            this.code = 0;
            return;
        }

        this.casId = c.getCasId();
        this.name = c.getName();
        this.code = 1;
    }

    public CasVO(String message) {
        this.code = 0;
        this.message = message;
    }

    public CasVO(int casId, String name) {
        this.casId = casId;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CasVO casVO = (CasVO) o;
        return casId == casVO.casId &&
                Objects.equals(name, casVO.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(casId, name);
    }
}
