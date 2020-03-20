package cn.comtom.system.main.org.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.comtom.domain.system.org.OrgPageRequest;
import cn.comtom.domain.system.org.OrganizationInfo;
import cn.comtom.system.fw.BaseDao;
import cn.comtom.system.fw.BaseServiceImpl;
import cn.comtom.system.main.org.dao.OrganizationDao;
import cn.comtom.system.main.org.entity.dbo.Organization;
import cn.comtom.system.main.org.service.IOrganizationService;

@Service
public class OrganizationServiceImpl extends BaseServiceImpl<Organization,String> implements IOrganizationService {

    @Autowired
    private OrganizationDao dao;

    @Override
    public BaseDao<Organization, String> getDao() {
        return dao;
    }

	@Override
	public List<OrganizationInfo> getList(OrgPageRequest request) {
		
		return dao.queryList(request);
	}

	@Override
	public OrganizationInfo getOrgById(String id) {
		return dao.getOrgById(id);
	}


}
