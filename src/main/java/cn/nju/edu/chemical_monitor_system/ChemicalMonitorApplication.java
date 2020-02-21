package cn.nju.edu.chemical_monitor_system;

import cn.nju.edu.chemical_monitor_system.utils.common.SpringContextUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class ChemicalMonitorApplication {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(ChemicalMonitorApplication.class);
        SpringContextUtil.setApplicationContext(applicationContext);
    }
}
