package cn.nju.edu.chemical_monitor_system.service.impl;

import cn.nju.edu.chemical_monitor_system.dao.CasDao;
import cn.nju.edu.chemical_monitor_system.entity.CasEntity;
import cn.nju.edu.chemical_monitor_system.service.CasService;
import cn.nju.edu.chemical_monitor_system.vo.CasVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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

    @Override
    public List<CasVO> searchCas(String key) {
        List<CasEntity> casEntities = casDao.findByNameLike("%" + key + "%");

        try {
            int id = Integer.parseInt(key);
            Optional<CasEntity> cas = casDao.findById(id);
            cas.ifPresent(casEntities::add);
        } catch (Exception ignored) {
        }

        return casEntities.stream().map(CasVO::new).collect(Collectors.toList());
    }

    @Override
    public void init() {
        Map<Integer, String> map = new HashMap<>();
        map.put(7722841, "过氧化氢");
        map.put(7647145, "氯化钠");
        map.put(108883, "甲苯");
        map.put(7664393, "氢氟酸");
        List<CasEntity> list = new ArrayList<>();
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            CasEntity casEntity = new CasEntity();
            casEntity.setCasId(entry.getKey());
            casEntity.setName(entry.getValue());
            list.add(casEntity);
        }
        casDao.saveAll(list);
    }

}
