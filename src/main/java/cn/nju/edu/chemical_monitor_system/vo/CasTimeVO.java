package cn.nju.edu.chemical_monitor_system.vo;

import lombok.Data;

import java.util.Objects;

@Data
public class CasTimeVO {
    int casId;
    String name;
    String time;

    public CasTimeVO(int casId, String name, String time) {
        this.casId = casId;
        this.name = name;
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CasTimeVO casTimeVO = (CasTimeVO) o;
        return casId == casTimeVO.casId &&
                Objects.equals(name, casTimeVO.name) &&
                Objects.equals(time, casTimeVO.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(casId, name, time);
    }
}
