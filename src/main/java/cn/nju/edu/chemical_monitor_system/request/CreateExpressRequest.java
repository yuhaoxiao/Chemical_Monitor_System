package cn.nju.edu.chemical_monitor_system.request;

import cn.nju.edu.chemical_monitor_system.vo.ProductVO;
import lombok.Data;

import java.util.List;

@Data
public class CreateExpressRequest {
    int inputStoreId;
    int outputStoreId;
    List<ProductVO> productVOS;
}
