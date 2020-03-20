package cn.comtom.ebs.front.main.org.service.impl;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.comtom.domain.system.org.OrgPageRequest;
import cn.comtom.domain.system.org.OrganizationInfo;
import cn.comtom.ebs.front.fegin.service.ISystemFeginService;
import cn.comtom.ebs.front.main.org.service.IOrganizationService;
import cn.comtom.tools.response.ApiPageResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrganizationServiceImpl implements IOrganizationService {

    @Autowired
    private ISystemFeginService systemFeginService;

	@Override
	public OrganizationInfo getOrgInfoById(String id) {
		return systemFeginService.getOrgInfoById(id);
	}

	@Override
	public ApiPageResponse<OrganizationInfo> getOrgInfoByPage(OrgPageRequest request) {
		return systemFeginService.getOrgInfoByPage(request);
	}

	@Override
	public OrganizationInfo saveOrgInfo(OrganizationInfo request) {
		return systemFeginService.saveOrgInfo(request);
	}

	@Override
	public void updateOrgInfo(OrganizationInfo request) {
		systemFeginService.updateOrgInfo(request);
	}

	@Override
	public Boolean deleteOrgInfo(String id) {
		return systemFeginService.deleteOrgInfo(id);
	}


}
