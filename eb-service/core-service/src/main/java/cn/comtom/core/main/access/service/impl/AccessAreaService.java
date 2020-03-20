package cn.comtom.core.main.access.service.impl;

import cn.comtom.core.fw.BaseDao;
import cn.comtom.core.fw.BaseServiceImpl;
import cn.comtom.core.main.access.dao.AccessAreaDao;
import cn.comtom.core.main.access.entity.dbo.AccessArea;
import cn.comtom.core.main.access.service.IAccessAreaService;
import cn.comtom.domain.core.access.info.AccessAreaInfo;
import cn.comtom.domain.core.access.request.AccessAreaReq;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AccessAreaService extends BaseServiceImpl<AccessArea,String> implements IAccessAreaService {

    @Autowired
    private AccessAreaDao accessAreaDao;

    @Override
    public BaseDao<AccessArea, String> getDao() {
        return accessAreaDao;
    }
    @Override
    public List<AccessAreaInfo> list(AccessAreaReq req) {
        List<AccessArea> accessAreaList = accessAreaDao.list(req);
        return Optional.ofNullable(accessAreaList).orElse(Collections.emptyList()).stream().filter(Objects::nonNull).map(access -> {
            AccessAreaInfo info = new AccessAreaInfo();
            BeanUtils.copyProperties(access,info);
            return info;
        }).collect(Collectors.toList());
    }

    @Override
    public List<AccessAreaInfo> getByInfoId(String infoId) {
        List<AccessArea> list = accessAreaDao.getByInfoId(infoId);
        return Optional.ofNullable(list).orElse(Collections.emptyList()).stream().filter(Objects::nonNull).map(access -> {
            AccessAreaInfo info = new AccessAreaInfo();
            BeanUtils.copyProperties(access,info);
            return info;
        }).collect(Collectors.toList());
    }

}
