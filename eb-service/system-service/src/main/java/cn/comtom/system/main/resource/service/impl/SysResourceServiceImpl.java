package cn.comtom.system.main.resource.service.impl;

import cn.comtom.domain.system.sysresource.info.SysResourceInfo;
import cn.comtom.domain.system.sysresource.request.SysResourceQueryRequest;
import cn.comtom.system.fw.BaseDao;
import cn.comtom.system.fw.BaseServiceImpl;
import cn.comtom.system.main.resource.dao.SysResourceDao;
import cn.comtom.system.main.resource.entity.dbo.SysResource;
import cn.comtom.system.main.resource.service.ISysResourceService;
import cn.comtom.tools.response.ApiListResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
@Slf4j
public class SysResourceServiceImpl extends BaseServiceImpl<SysResource,String> implements ISysResourceService {

    @Autowired
    private SysResourceDao sysResourceDao;

    @Override
    public BaseDao<SysResource, String> getDao() {
        return sysResourceDao;
    }


    @Override
    public List<SysResourceInfo> list(SysResourceQueryRequest request) {
        List<SysResource> sysResourceList = sysResourceDao.list(request);
        return Optional.ofNullable(sysResourceList).orElse(Collections.emptyList()).stream()
                .filter(Objects::nonNull)
                .map(sysResource -> {
                    SysResourceInfo sysResourceInfo = new SysResourceInfo();
                    BeanUtils.copyProperties(sysResource,sysResourceInfo);
                    return sysResourceInfo;
                }).collect(Collectors.toList());
    }

    @Override
    public List<SysResourceInfo> getByPid(String pid) {
        List<SysResource> sysResourceList = sysResourceDao.getByPid(pid);
        return Optional.ofNullable(sysResourceList).orElse(Collections.emptyList()).stream()
                .filter(Objects::nonNull)
                .map(sysResource -> {
                    SysResourceInfo sysResourceInfo = new SysResourceInfo();
                    BeanUtils.copyProperties(sysResource,sysResourceInfo);
                    return sysResourceInfo;
                }).collect(Collectors.toList());
    }


    @Override
    public ApiListResponse<SysResourceInfo> getAllWithRecursion() {
        SysResourceQueryRequest sysResourceQueryRequest = new SysResourceQueryRequest();
        sysResourceQueryRequest.setType("menu");
        //查询所有一级菜单资源
        List<SysResourceInfo> sysResourceInfoList = this.list(sysResourceQueryRequest);
        sysResourceInfoList = Optional.ofNullable(sysResourceInfoList).orElse(Collections.emptyList()).stream()
                .filter(Objects::nonNull)
                .distinct()
                .sorted(Comparator.comparing(SysResourceInfo::getOrderNum))
                // .filter(sysResourceInfo -> StringUtils.isNotBlank(sysResourceInfo.getPid()))
                .peek(sysResourceInfo -> sysResourceInfo.setSubList(getSubList(sysResourceInfo)))
                .collect(Collectors.toList());
        return ApiListResponse.ok(sysResourceInfoList);
    }

    private List<SysResourceInfo> getSubList(SysResourceInfo sysResourceInfo) {
        List<SysResourceInfo> subList = this.getByPid(sysResourceInfo.getId());
        return Optional.ofNullable(subList).orElse(Collections.emptyList()).stream()
                .filter(Objects::nonNull)
                .peek(sysResourceInfo1 -> sysResourceInfo1.setSubList(getSubList(sysResourceInfo1)))
                .distinct()
                .sorted(Comparator.comparing(SysResourceInfo::getOrderNum))
                .collect(Collectors.toList());
    }
}
