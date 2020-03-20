package cn.comtom.ebs.front.fegin.service.impl;

import cn.comtom.domain.system.accessnode.info.AccessNodeInfo;
import cn.comtom.domain.system.accessnode.request.AccessNodeRequest;
import cn.comtom.domain.system.dict.info.SysDictGroupInfo;
import cn.comtom.domain.system.dict.info.SysDictInfo;
import cn.comtom.domain.system.dict.request.SysDictAddRequest;
import cn.comtom.domain.system.dict.request.SysDictGroupAddRequest;
import cn.comtom.domain.system.dict.request.SysDictGroupUpdateRequest;
import cn.comtom.domain.system.dict.request.SysDictUpdateRequest;
import cn.comtom.domain.system.eventtype.info.EventtypeInfo;
import cn.comtom.domain.system.log.info.SysOperateLogInfo;
import cn.comtom.domain.system.log.request.SysOperateLogAddRequest;
import cn.comtom.domain.system.org.OrgPageRequest;
import cn.comtom.domain.system.org.OrganizationInfo;
import cn.comtom.domain.system.plan.info.SysPlanMatchInfo;
import cn.comtom.domain.system.plan.info.SysPlanResoRefInfo;
import cn.comtom.domain.system.plan.request.SysPlanDelRequest;
import cn.comtom.domain.system.plan.request.SysPlanMatchAddRequest;
import cn.comtom.domain.system.plan.request.SysPlanMatchUpdRequest;
import cn.comtom.domain.system.plan.request.SysPlanPageRequest;
import cn.comtom.domain.system.region.info.RegionAreaInfo;
import cn.comtom.domain.system.role.info.SysRoleInfo;
import cn.comtom.domain.system.role.request.SysRolePageRequest;
import cn.comtom.domain.system.roleresource.request.RoleResAddBatchRequest;
import cn.comtom.domain.system.sysparam.info.SysParamsInfo;
import cn.comtom.domain.system.sysparam.request.SysParamPageRequest;
import cn.comtom.domain.system.sysresource.info.SysResourceInfo;
import cn.comtom.domain.system.sysuser.info.SysUserInfo;
import cn.comtom.domain.system.sysuser.request.SysUserUpdateRequest;
import cn.comtom.domain.system.sysuser.request.UserAddRequest;
import cn.comtom.domain.system.sysuser.request.UserPageRequest;
import cn.comtom.domain.system.sysuserrole.request.SysUserRoleAddBatchRequest;
import cn.comtom.ebs.front.fegin.SystemFegin;
import cn.comtom.ebs.front.fegin.service.FeginBaseService;
import cn.comtom.ebs.front.fegin.service.ISystemFeginService;
import cn.comtom.tools.response.ApiPageResponse;
import cn.comtom.tools.utils.ReflectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class SystemFeginServiceImpl extends FeginBaseService implements ISystemFeginService {

    @Autowired
    private SystemFegin systemFegin;

    @Override
    public Boolean isWhite(String reqIp) {
        return getBoolean(systemFegin.isWhite(reqIp));
    }

    @Override
    public AccessNodeInfo getAccessNodeByPlatformId(String ebrid) {
        return getData(systemFegin.getAccessNodeByPlatformId(ebrid));
    }

    @Override
    public AccessNodeInfo saveAccessNode(AccessNodeRequest request) {
        return getData(systemFegin.saveAccessNode(request));
    }

    @Override
    public List<SysPlanMatchInfo> getMatchPlan(String eventLevel,String eventType, String areaCodes) {
        return getDataList(systemFegin.getMatchPlan(eventLevel,eventType,areaCodes));
    }

    @Override
    public List<SysPlanResoRefInfo> getPlanResRefByPlanId(String planId) {
        return getDataList(systemFegin.getPlanResRefByPlanId(planId));
    }

    @Override
    public List<RegionAreaInfo> getRegionAreaByAreaCodes(List<String> areaCodeList) {
        return getDataList(systemFegin.getRegionAreaByAreaCodes(areaCodeList));
    }

    @Override
    public RegionAreaInfo getRegionAreaByAreaCode(String areaCode) {
        return getData(systemFegin.getRegionAreaByAreaCode(areaCode));
    }

    @Override
    public SysOperateLogInfo saveSysOperateLog(SysOperateLogAddRequest request) {
        return getData(systemFegin.saveSysOperateLog(request));
    }

    @Override
    public SysDictInfo saveSysDictInfo(SysDictAddRequest request) {
        return getData(systemFegin.saveSysDictInfo(request));
    }

    @Override
    public SysDictInfo getSysDictInfoById(String dictId) {
        return getData(systemFegin.getSysDictInfoById(dictId));
    }

    @Override
    public SysDictInfo getSysDictInfoByDictKey(String dictKey) {
        return getData(systemFegin.getSysDictInfoByDictKey(dictKey));
    }

    @Override
    public List<SysDictInfo> getSysDictInfoByGroupCode(String dictGroupCode) {
        return getDataList(systemFegin.getSysDictInfoByGroupCode(dictGroupCode));
    }

    @Override
    public Boolean deleteSysDictInfo(String dictId) {
        return getBoolean(systemFegin.deleteSysDictInfo(dictId));
    }

    @Override
    public Boolean updateSysDictInfo(SysDictUpdateRequest request) {
        return getBoolean(systemFegin.updateSysDictInfo(request));
    }

    @Override
    public Boolean updateUserInfo(SysUserUpdateRequest request) {
        return getBoolean(systemFegin.updateUserInfo(request));
    }

    @Override
    public SysDictGroupInfo saveSysDictGroupInfo(SysDictGroupAddRequest request) {
        return getData(systemFegin.saveSysDictGroupInfo(request));
    }

    @Override
    public SysDictGroupInfo getSysDictGroupInfoById(String dictGroupId) {
        return getData(systemFegin.getSysDictGroupInfoById(dictGroupId));
    }

    @Override
    public Boolean deleteSysDictGroupInfo(String dictGroupId) {
        return getBoolean(systemFegin.deleteSysDictGroupInfo(dictGroupId));
    }

    @Override
    public Boolean updateSysDictGroupInfo(SysDictGroupUpdateRequest request) {
        return getBoolean(systemFegin.updateSysDictGroupInfo(request));
    }

    @Override
    public ApiPageResponse<SysUserInfo> findUserPage(UserPageRequest pageRequest) {
        Map<String,Object> whereRequestMap = ReflectionUtils.convertBean2Map(pageRequest);
        return systemFegin.findUserPage(whereRequestMap);
    }

    @Override
    public ApiPageResponse<SysParamsInfo> findSysParamPage(SysParamPageRequest request) {
        Map<String,Object> whereRequestMap = ReflectionUtils.convertBean2Map(request);
        return systemFegin.findSysParamPage(whereRequestMap);
    }

    @Override
    public ApiPageResponse<SysRoleInfo> findSysRolePage(SysRolePageRequest request) {
        Map<String,Object> whereRequestMap = ReflectionUtils.convertBean2Map(request);
        return systemFegin.findSysRolePage(whereRequestMap);
    }

    @Override
    public ApiPageResponse<SysPlanMatchInfo> findSysPlanMathPage(SysPlanPageRequest request) {
        Map<String,Object> whereRequestMap = ReflectionUtils.convertBean2Map(request);
        return systemFegin.findSysPlanMathPage(whereRequestMap);
    }

    @Override
    public void deleteSysPlanMathById(SysPlanDelRequest request) {
        systemFegin.deleteSysPlanMathById(request);
    }

    @Override
    public SysPlanMatchInfo saveSysPlanMath(SysPlanMatchAddRequest request) {
        return getData(systemFegin.saveSysPlanMath(request));
    }

    @Override
    public SysPlanMatchInfo updateSysPlanMath(SysPlanMatchUpdRequest request) {
        return getData(systemFegin.updateSysPlanMath(request));
    }

    @Override
    public SysUserInfo saveUserInfo(UserAddRequest request) {
        return getData(systemFegin.saveUserInfo(request));
    }

    @Override
    public SysUserInfo getUserInfoByAccount(String account) {
        return getData(systemFegin.getUserInfoByAccount(account));
    }

    @Override
    public Boolean saveUserRoleRefBatch(SysUserRoleAddBatchRequest request) {
        return getBoolean(systemFegin.saveSysUserRoleBatch(request));
    }

    @Override
    public Boolean deleteUserRoleByUserId(String userId) {
        return getBoolean(systemFegin.deleteUserRoleByUserId(userId));
    }

    @Override
    public Boolean deleteRoleInfoById(String roleId) {
        return getBoolean(systemFegin.deleteRoleInfoById(roleId));
    }

    @Override
    public Boolean deleteRoleResourceByRoleId(String roleId) {
        return getBoolean(systemFegin.deleteRoleResourceByRoleId(roleId));
    }

    @Override
    public Boolean deleteRoleResourceByResourceId(String resourceId) {
        return getBoolean(systemFegin.deleteRoleResourceByRoleId(resourceId));
    }

    @Override
    public Boolean deleteUserRoleByRoleId(String roleId) {
        return getBoolean(systemFegin.deleteUserRoleByRoleId(roleId));
    }

    @Override
    public SysPlanMatchInfo getByPlanId(String planId) {
        return getData(systemFegin.getByPlanId(planId));
    }

    @Override
    public List<RegionAreaInfo> getNextAreaCodeByParentCode(String parentCode) {
        return getData(systemFegin.getNextAreaCodeByParentCode(parentCode));
    }

    @Override
    public SysParamsInfo getByKey(String paramKey) {
        return getData(systemFegin.getByKey(paramKey));
    }

    @Override
    public List<EventtypeInfo> getNextByParentCode(String parentCode) {
        return getData(systemFegin.getEventNextByParentCode(parentCode));
    }

    @Override
    public List<SysResourceInfo> getAllWithRecursion() {
        return getDataList(systemFegin.getAllWithRecursion());
    }

    @Override
    public Boolean saveSysRoleResourceBatch(RoleResAddBatchRequest request) {
        return getBoolean(systemFegin.saveSysRoleResourceBatch(request));
    }

    @Override
    public SysParamsInfo getSysParamsInfoByKey(String paramKey) {
        return getData(systemFegin.getByKey(paramKey));
    }

    @Override
    public Boolean deleteAccessNodeByEbrId(String ebrId) {
        return getBoolean(systemFegin.deleteAccessNodeByEbrId(ebrId));
    }

    @Override
    public Boolean updateAccessNode(AccessNodeRequest request) {
        return getBoolean(systemFegin.updateAccessNode(request));
    }

    @Override
    public List<RegionAreaInfo> getRegionAreaByParentAreaCode(String parentAreaCode) {
        return getDataList(systemFegin.getRegionAreaByParentAreaCode(parentAreaCode));
    }

    @Override
    public Integer getAreaTotal() {
        return getData(systemFegin.getAreaTotal());
    }

    @Override
    public List<RegionAreaInfo> getNextReginCodeByParentCode(String parentAreaCode) {
        return getData(systemFegin.getNextReginCodeByParentCode(parentAreaCode));
    }

    @Override
    public boolean sureInitArea(String areaCode) {
        return getBoolean(systemFegin.sureInitArea(areaCode));
    }

    @Override
    public RegionAreaInfo getRootArea() {
        return getData(systemFegin.getRootArea());
    }

	@Override
	public boolean updateOrSaveArea(RegionAreaInfo regionArea) {
		return getBoolean(systemFegin.updateOrSaveArea(regionArea));
	}

	@Override
	public boolean deleteAreaById(String id) {
		return getBoolean(systemFegin.deleteAreaById(id));
	}

	@Override
	public OrganizationInfo getOrgInfoById(String id) {

		return getData(systemFegin.getOrgInfoById(id));
	}

	@Override
	public ApiPageResponse<OrganizationInfo> getOrgInfoByPage(OrgPageRequest request) {
		Map<String,Object> whereRequestMap = ReflectionUtils.convertBean2Map(request);
        return systemFegin.getOrgInfoByPage(whereRequestMap);
	}

	@Override
	public OrganizationInfo saveOrgInfo(OrganizationInfo request) {

		return getData(systemFegin.saveOrgInfo(request));
	}

	@Override
	public boolean updateOrgInfo(OrganizationInfo request) {
		return getBoolean(systemFegin.updateOrgInfo(request));
	}

	@Override
	public Boolean deleteOrgInfo(String id) {

		return getBoolean(systemFegin.deleteOrgInfo(id));
	}
}


