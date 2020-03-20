package cn.comtom.core.main.access.service.impl;

import cn.comtom.core.fw.BaseDao;
import cn.comtom.core.fw.BaseServiceImpl;
import cn.comtom.core.main.access.dao.AccessDao;
import cn.comtom.core.main.access.entity.dbo.Access;
import cn.comtom.core.main.access.service.IAccessService;
import cn.comtom.domain.core.access.info.AccessInfo;
import cn.comtom.domain.core.access.request.AccessInfoPageRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AccessServiceImpl extends BaseServiceImpl<Access,String> implements IAccessService {

    @Autowired
    private AccessDao accessDao;

    @Override
    public BaseDao<Access, String> getDao() {
        return accessDao;
    }

    @Override
    public List<AccessInfo> list(AccessInfoPageRequest request) {
        List<Access> list = accessDao.list(request);
        return Optional.ofNullable(list).orElse(Collections.emptyList()).stream().filter(Objects::nonNull).map(access -> {
            AccessInfo info = new AccessInfo();
            BeanUtils.copyProperties(access,info);
            return info;
        }).collect(Collectors.toList());
    }
}
