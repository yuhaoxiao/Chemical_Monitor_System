package cn.nju.edu.chemical_monitor_system.request;

import lombok.Data;

import java.util.List;

@Data
public class BatchOutRequest {

    private int batchId;
    private List<ProductRequest> products;
}
