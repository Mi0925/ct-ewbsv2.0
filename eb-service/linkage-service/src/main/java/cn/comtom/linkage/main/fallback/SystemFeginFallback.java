package cn.comtom.linkage.main.fallback;

import cn.comtom.domain.system.accessnode.info.AccessNodeInfo;
import cn.comtom.domain.system.log.info.SysOperateLogInfo;
import cn.comtom.domain.system.log.request.SysOperateLogAddRequest;
import cn.comtom.domain.system.plan.info.SysPlanMatchInfo;
import cn.comtom.domain.system.plan.info.SysPlanResoRefInfo;
import cn.comtom.domain.system.region.info.RegionAreaInfo;
import cn.comtom.domain.system.sequence.info.SysSequenceInfo;
import cn.comtom.domain.system.sequence.request.SequenceUpdateRequest;
import cn.comtom.domain.system.sysparam.info.SysParamsInfo;
import cn.comtom.linkage.main.fegin.SystemFegin;
import cn.comtom.tools.enums.BasicError;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiListResponse;
import cn.comtom.tools.response.ApiResponse;
import cn.comtom.tools.response.ApiResponseBuilder;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.util.List;

@Component
public class SystemFeginFallback implements SystemFegin {
    @Override
    public ApiEntityResponse<SysParamsInfo> getByKey(String paramKey) {
        return ApiResponseBuilder.buildEntityError(BasicError.FEIGN_ERROR);
    }

    @Override
    public ApiResponse isWhite(String ip) {
        return ApiResponseBuilder.buildError(BasicError.FEIGN_ERROR);
    }

    @Override
    public ApiEntityResponse<AccessNodeInfo> getByPlatformId(String platformId) {
        return ApiResponseBuilder.buildEntityError(BasicError.FEIGN_ERROR);
    }

    @Override
    public ApiEntityResponse<SysSequenceInfo> getValue(String name) {
        return ApiResponseBuilder.buildEntityError(BasicError.FEIGN_ERROR);
    }

    @Override
    public ApiResponse updateById(SequenceUpdateRequest request) {
        return ApiResponseBuilder.buildError(BasicError.FEIGN_ERROR);
    }

    @Override
    public ApiListResponse<SysPlanMatchInfo> getMatchPlan(String severity, String eventType, String srcEbrId, String areaCodes) {
        return null;
    }

    @Override
    public ApiListResponse<SysPlanResoRefInfo> getPlanResRefByPlanId(String planId) {
        return null;
    }

    @Override
    public ApiEntityResponse<RegionAreaInfo> getRegionAreaByAreaCode(String areaCode) {
        return null;
    }

    @Override
    public ApiListResponse<RegionAreaInfo> getRegionAreaByAreaCodes(List<String> areaCodes) {
        return null;
    }

    @Override
    public ApiEntityResponse<SysOperateLogInfo> saveSysOperateLog(@Valid SysOperateLogAddRequest request) {
        return null;
    }
}
