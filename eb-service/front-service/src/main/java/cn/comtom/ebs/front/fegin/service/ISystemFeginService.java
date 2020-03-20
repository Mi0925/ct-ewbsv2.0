package cn.comtom.ebs.front.fegin.service;

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
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiPageResponse;

import java.util.List;

public interface ISystemFeginService {

    Boolean isWhite(String reqIp);

    AccessNodeInfo getAccessNodeByPlatformId(String ebrid);

    AccessNodeInfo saveAccessNode(AccessNodeRequest request);

    List<SysPlanMatchInfo> getMatchPlan(String eventLevel,String eventType, String areaCodeList);

    List<SysPlanResoRefInfo> getPlanResRefByPlanId(String planId);

    List<RegionAreaInfo> getRegionAreaByAreaCodes(List<String> areaCodeList);

    RegionAreaInfo getRegionAreaByAreaCode(String areaCode);

    SysOperateLogInfo saveSysOperateLog(SysOperateLogAddRequest request);

    SysDictInfo saveSysDictInfo(SysDictAddRequest request);

    SysDictInfo getSysDictInfoById(String dictId);

    SysDictInfo getSysDictInfoByDictKey(String dictKey);

    List<SysDictInfo> getSysDictInfoByGroupCode(String dictGroupCode);

    Boolean deleteSysDictInfo(String dictId);

    Boolean updateSysDictInfo(SysDictUpdateRequest request);

    Boolean updateUserInfo(SysUserUpdateRequest request);

    SysDictGroupInfo saveSysDictGroupInfo(SysDictGroupAddRequest request);

    SysDictGroupInfo getSysDictGroupInfoById(String dictGroupId);

    Boolean deleteSysDictGroupInfo(String dictGroupId);

    Boolean updateSysDictGroupInfo(SysDictGroupUpdateRequest request);

    ApiPageResponse<SysUserInfo> findUserPage(UserPageRequest pageRequest);

    ApiPageResponse<SysParamsInfo> findSysParamPage(SysParamPageRequest request);

    ApiPageResponse<SysRoleInfo> findSysRolePage(SysRolePageRequest request);

    ApiPageResponse<SysPlanMatchInfo> findSysPlanMathPage(SysPlanPageRequest pageRequest);

    void deleteSysPlanMathById(SysPlanDelRequest request);

    SysPlanMatchInfo saveSysPlanMath(SysPlanMatchAddRequest request);

    SysPlanMatchInfo updateSysPlanMath(SysPlanMatchUpdRequest request);

    SysUserInfo saveUserInfo(UserAddRequest request);

    SysUserInfo getUserInfoByAccount(String account);

    Boolean saveUserRoleRefBatch(SysUserRoleAddBatchRequest request);

    Boolean deleteUserRoleByUserId(String userId);

    Boolean deleteRoleInfoById(String roleId);

    Boolean deleteRoleResourceByRoleId(String roleId);

    Boolean deleteRoleResourceByResourceId(String resourceId);

    Boolean deleteUserRoleByRoleId(String roleId);

    SysPlanMatchInfo getByPlanId(String planId);

    List<RegionAreaInfo> getNextAreaCodeByParentCode(String parentCode);

    SysParamsInfo getByKey(String ebrPlatformId);

    List<EventtypeInfo> getNextByParentCode(String parentCode);

    List<SysResourceInfo> getAllWithRecursion();

    Boolean saveSysRoleResourceBatch(RoleResAddBatchRequest request);

    SysParamsInfo getSysParamsInfoByKey(String paramKey);

    Boolean deleteAccessNodeByEbrId(String ebrId);

    Boolean updateAccessNode(AccessNodeRequest request);

    List<RegionAreaInfo> getRegionAreaByParentAreaCode(String parentAreaCode);

    Integer getAreaTotal();

    List<RegionAreaInfo> getNextReginCodeByParentCode(String parentAreaCode);

    boolean sureInitArea(String areaCode);

    RegionAreaInfo getRootArea();

	boolean updateOrSaveArea(RegionAreaInfo regionArea);

	boolean deleteAreaById(String id);

	OrganizationInfo getOrgInfoById(String id);

	ApiPageResponse<OrganizationInfo> getOrgInfoByPage(OrgPageRequest request);

	OrganizationInfo saveOrgInfo(OrganizationInfo request);

	boolean updateOrgInfo(OrganizationInfo request);

	Boolean deleteOrgInfo(String id);

}

