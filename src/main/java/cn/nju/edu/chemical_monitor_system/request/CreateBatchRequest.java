package cn.nju.edu.chemical_monitor_system.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class CreateBatchRequest {

    private int type; // 0代表生产，1代表入园（只有products），2代表出园（只有raws），3代表销毁（只有raws）
    private int productionLineId;
    private List<RawRequest> raws;

    @Data
    public class RawRequest { // 新建inbatch
        private int productId;
        private int storeId;
        private double number;
    }

}
