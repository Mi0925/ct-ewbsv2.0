package cn.comtom.system.main.org.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.comtom.domain.system.org.OrgPageRequest;
import cn.comtom.domain.system.org.OrganizationInfo;
import cn.comtom.system.fw.BaseDao;
import cn.comtom.system.fw.SystemMapper;
import cn.comtom.system.main.org.entity.dbo.Organization;
import cn.comtom.system.main.org.mapper.OrganizationMapper;

@Repository
public class OrganizationDao extends BaseDao<Organization,String> {

    @Autowired
    private OrganizationMapper mapper;


    @Override
    public SystemMapper<Organization, String> getMapper() {
        return mapper;
    }


	public List<OrganizationInfo> queryList(OrgPageRequest request) {
		
		return mapper.queryList(request);
	}


	public OrganizationInfo getOrgById(String id) {
		
		return mapper.getOrgById(id);
	}

    
    
}
