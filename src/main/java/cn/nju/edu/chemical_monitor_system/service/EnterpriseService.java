package cn.nju.edu.chemical_monitor_system.service;

import cn.nju.edu.chemical_monitor_system.vo.EnterpriseVO;

import java.util.List;

public interface EnterpriseService {

    EnterpriseVO addEnterprise(String name);

    EnterpriseVO deleteEnterprise(int eid);

    EnterpriseVO updateEnterprise(EnterpriseVO enterpriseVO);

    List<EnterpriseVO> searchEnterprise(String s);
}
