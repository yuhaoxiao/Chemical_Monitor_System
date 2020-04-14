package cn.nju.edu.chemical_monitor_system.service;

import cn.nju.edu.chemical_monitor_system.vo.CasVO;

import java.util.List;

public interface CasService {

    CasVO getCas(int cid);

    List<CasVO> searchCas(String key);

    void init();
}
