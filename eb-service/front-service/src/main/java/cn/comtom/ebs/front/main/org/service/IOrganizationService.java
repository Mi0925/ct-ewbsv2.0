package cn.comtom.ebs.front.main.org.service;

import javax.validation.Valid;

import cn.comtom.domain.system.org.OrgPageRequest;
import cn.comtom.domain.system.org.OrganizationInfo;
import cn.comtom.tools.response.ApiPageResponse;

public interface IOrganizationService {

	OrganizationInfo getOrgInfoById(String id);

	ApiPageResponse<OrganizationInfo> getOrgInfoByPage(@Valid OrgPageRequest request);

	OrganizationInfo saveOrgInfo(@Valid OrganizationInfo request);

	void updateOrgInfo(@Valid OrganizationInfo request);

	Boolean deleteOrgInfo(String id);

}
