package cn.comtom.linkage.main.fegin.service;

import cn.comtom.domain.system.accessnode.info.AccessNodeInfo;
import cn.comtom.domain.system.log.info.SysOperateLogInfo;
import cn.comtom.domain.system.log.request.SysOperateLogAddRequest;
import cn.comtom.domain.system.plan.info.SysPlanMatchInfo;
import cn.comtom.domain.system.plan.info.SysPlanResoRefInfo;
import cn.comtom.domain.system.region.info.RegionAreaInfo;

import java.util.List;

public interface ISystemFeginService {

    Boolean isWhite(String reqIp);

    AccessNodeInfo getByPlatformId(String ebrid);

    List<SysPlanMatchInfo> getMatchPlan(String eventLevel,String eventType,String srcEbrId, String areaCode);

    List<SysPlanResoRefInfo> getPlanResRefByPlanId(String planId);

    List<RegionAreaInfo> getRegionAreaByAreaCodes(List<String> areaCodeList);

    RegionAreaInfo getRegionAreaByAreaCode(String areaCode);

    SysOperateLogInfo saveSysOperateLog(SysOperateLogAddRequest request);
    
    String getByKey(String paramKey);
}
