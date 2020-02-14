package cn.nju.edu.chemical_monitor_system.request;

import cn.nju.edu.chemical_monitor_system.vo.InOutBatchVO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@ToString
public class CreateBatchRequest {

    private int productionlineId;

    private Timestamp time;

    private String type;

    private List<InOutBatchVO> inOutBatchVOS;

}
