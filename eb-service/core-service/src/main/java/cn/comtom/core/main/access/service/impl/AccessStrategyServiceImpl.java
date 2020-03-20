package cn.comtom.core.main.access.service.impl;

import cn.comtom.core.fw.BaseDao;
import cn.comtom.core.fw.BaseServiceImpl;
import cn.comtom.core.main.access.dao.AccessStrategyDao;
import cn.comtom.core.main.access.entity.dbo.AccessStrategy;
import cn.comtom.core.main.access.service.IAccessStrategyService;
import cn.comtom.core.main.access.service.IAccessTimeService;
import cn.comtom.domain.core.access.info.AccessStrategyAll;
import cn.comtom.domain.core.access.info.AccessStrategyInfo;
import cn.comtom.domain.core.access.info.AccessTimeInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AccessStrategyServiceImpl extends BaseServiceImpl<AccessStrategy,String> implements IAccessStrategyService {

    @Autowired
    private AccessStrategyDao accessStrategyDao;

    @Autowired
    private IAccessTimeService accessTimeService;

    @Override
    public BaseDao<AccessStrategy, String> getDao() {
        return accessStrategyDao;
    }

    @Override
    public List<AccessStrategyInfo> getByInfoId(String infoId) {
        List<AccessStrategy> list = accessStrategyDao.getByInfoId(infoId);
        return Optional.ofNullable(list).orElse(Collections.emptyList()).stream().filter(Objects::nonNull).map(access -> {
            AccessStrategyInfo info = new AccessStrategyInfo();
            BeanUtils.copyProperties(access,info);
            return info;
        }).collect(Collectors.toList());
    }

    @Override
    public List<AccessStrategyAll> getInfoAllByInfoId(String infoId) {
        List<AccessStrategyInfo> accessStrategyInfoList = getByInfoId(infoId);
        return Optional.ofNullable(accessStrategyInfoList).orElse(Collections.emptyList()).stream().filter(Objects::nonNull).map(accessStrategyInfo -> {
            AccessStrategyAll strategyAll = new AccessStrategyAll();
            BeanUtils.copyProperties(accessStrategyInfo,strategyAll);
            List<AccessTimeInfo> timeInfoList = accessTimeService.getByStrategyId(accessStrategyInfo.getStrategyId());
            strategyAll.setAccessTimeInfoList(timeInfoList);
            return strategyAll;
        }).collect(Collectors.toList());
    }
}
