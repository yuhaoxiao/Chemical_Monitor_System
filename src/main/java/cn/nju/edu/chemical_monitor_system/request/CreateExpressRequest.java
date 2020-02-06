package cn.nju.edu.chemical_monitor_system.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
@ToString
public class CreateExpressRequest {
    int inputStoreId;
    int outputStoreId;
    Map<Integer, Double> productNumberMap;
}
