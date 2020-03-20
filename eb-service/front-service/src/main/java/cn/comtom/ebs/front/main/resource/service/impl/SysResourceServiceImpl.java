package cn.comtom.ebs.front.main.resource.service.impl;

import cn.comtom.domain.system.sysresource.info.SysResourceInfo;
import cn.comtom.domain.system.sysresource.request.SysResourcePageRequest;
import cn.comtom.ebs.front.fegin.SystemFegin;
import cn.comtom.ebs.front.fegin.service.ISystemFeginService;
import cn.comtom.ebs.front.main.resource.service.ISysResourceService;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiListResponse;
import cn.comtom.tools.response.ApiPageResponse;
import cn.comtom.tools.utils.ReflectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class SysResourceServiceImpl implements ISysResourceService {

    @Autowired
    private SystemFegin systemFegin;

    @Autowired
    private ISystemFeginService systemFeginService;

    @Override
    public List<SysResourceInfo> getSysResourceByIds(List<String> resourceIds) {
        ApiListResponse<SysResourceInfo> response = systemFegin.getSysResourceByIds(resourceIds);
        if(response != null && response.getSuccessful()){
            return response.getData();
        }
        return null;
    }

    @Override
    public ApiPageResponse<SysResourceInfo> getSysResourceInfoListPage(SysResourcePageRequest request) {
        return systemFegin.getSysResourceByPage(ReflectionUtils.convertBean2Map(request));
    }

    @Override
    public ApiPageResponse<SysResourceInfo> getSysResAndChildren(SysResourcePageRequest request) {
        return systemFegin.getSysResAndChildren(ReflectionUtils.convertBean2Map(request));
    }

    @Override
    public List<SysResourceInfo> getByPid(String pid) {
        ApiListResponse<SysResourceInfo> response = systemFegin.getSysResourceByPid(pid);
        return Optional.ofNullable(response).map(ApiListResponse::getData).orElseGet(ArrayList::new);
    }

    @Override
    public SysResourceInfo getSysResourceById(String id) {
        ApiEntityResponse<SysResourceInfo> response = systemFegin.getSysResourceById(id);
        return Optional.ofNullable(response).map(ApiEntityResponse::getData).orElseGet(SysResourceInfo::new);
    }

    @Override
    public List<SysResourceInfo> getAllWithRecursion() {
        return systemFeginService.getAllWithRecursion();
    }
}
