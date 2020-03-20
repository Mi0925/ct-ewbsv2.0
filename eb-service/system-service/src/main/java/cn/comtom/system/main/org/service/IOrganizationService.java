package cn.comtom.system.main.org.service;

import java.util.List;

import cn.comtom.domain.system.org.OrgPageRequest;
import cn.comtom.domain.system.org.OrganizationInfo;
import cn.comtom.system.fw.BaseService;
import cn.comtom.system.main.org.entity.dbo.Organization;

public interface IOrganizationService extends BaseService<Organization,String> {

	List<OrganizationInfo> getList(OrgPageRequest request);

	OrganizationInfo getOrgById(String id);

}
