package cn.nju.edu.chemical_monitor_system.service.impl;

import cn.nju.edu.chemical_monitor_system.dao.CasDao;
import cn.nju.edu.chemical_monitor_system.entity.CasEntity;
import cn.nju.edu.chemical_monitor_system.service.CasService;
import cn.nju.edu.chemical_monitor_system.vo.CasVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CasServiceImpl implements CasService {

    @Autowired
    private CasDao casDao;

    @Override
    public CasVO getCas(int cid) {
        Optional<CasEntity> cas = casDao.findById(cid);

        if (!cas.isPresent()) {
            return new CasVO("CAS ID不存在");
        }

        return new CasVO(cas.get());
    }

}
