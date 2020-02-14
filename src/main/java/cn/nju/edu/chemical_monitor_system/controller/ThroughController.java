package cn.nju.edu.chemical_monitor_system.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ThroughController {

    @GetMapping(value = "/throughput/get_cas_throughput")
    public void getCasThroughput() {

    }

    @GetMapping(value = "/throughput/get_entity_through")
    public void getEntityThroughput() {

    }

}
