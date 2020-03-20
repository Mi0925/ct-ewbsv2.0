package cn.comtom.core.main.access.service.impl;

import cn.comtom.core.fw.BaseDao;
import cn.comtom.core.fw.BaseServiceImpl;
import cn.comtom.core.main.access.dao.AccessTimeDao;
import cn.comtom.core.main.access.entity.dbo.AccessTime;
import cn.comtom.core.main.access.service.IAccessTimeService;
import cn.comtom.domain.core.access.info.AccessTimeInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AccessTimeServiceImpl extends BaseServiceImpl<AccessTime,String> implements IAccessTimeService {

    @Autowired
    private AccessTimeDao accessTimeDao;

    @Override
    public BaseDao<AccessTime, String> getDao() {
        return accessTimeDao;
    }

    @Override
    public List<AccessTimeInfo> getByStrategyId(String infoId) {
        List<AccessTime> list = accessTimeDao.getByStrategyId(infoId);
        return Optional.ofNullable(list).orElse(Collections.emptyList()).stream().filter(Objects::nonNull).map(accessTime -> {
            AccessTimeInfo info = new AccessTimeInfo();
            BeanUtils.copyProperties(accessTime,info);
            return info;
        }).collect(Collectors.toList());
    }

}
