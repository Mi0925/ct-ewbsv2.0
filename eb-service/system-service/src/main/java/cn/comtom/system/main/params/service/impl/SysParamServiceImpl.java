package cn.comtom.system.main.params.service.impl;

import cn.comtom.system.fw.BaseDao;
import cn.comtom.system.fw.BaseServiceImpl;
import cn.comtom.system.main.params.dao.SysParamDao;
import cn.comtom.system.main.params.entity.dbo.SysParams;
import cn.comtom.system.main.params.service.ISysParamService;
import cn.comtom.domain.system.sysparam.info.SysParamsInfo;
import cn.comtom.domain.system.sysparam.request.SysParamPageRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SysParamServiceImpl extends BaseServiceImpl<SysParams,String> implements ISysParamService {

    @Autowired
    private SysParamDao sysParamDao;

    @Override
    public BaseDao<SysParams, String> getDao() {
        return sysParamDao;
    }

    @Override
    public List<SysParamsInfo> pageList(SysParamPageRequest request) {
        List<SysParams> list = sysParamDao.pageList(request);
        return Optional.ofNullable(list).orElse(Collections.emptyList()).stream().filter(Objects::nonNull).map(sysParams -> {
            SysParamsInfo info = new SysParamsInfo();
            BeanUtils.copyProperties(sysParams,info);
            return info;
        }).collect(Collectors.toList());
    }

    @Override
    public SysParamsInfo getByKey(String paramKey) {
        SysParams sysParams = sysParamDao.getByKey(paramKey);
        SysParamsInfo info = null;
        if(sysParams != null){
            info = new SysParamsInfo();
            BeanUtils.copyProperties(sysParams,info);
        }
        return info;
    }
}
