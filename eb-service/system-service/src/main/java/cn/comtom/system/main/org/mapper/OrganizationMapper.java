package cn.comtom.system.main.org.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import cn.comtom.domain.system.org.OrgPageRequest;
import cn.comtom.domain.system.org.OrganizationInfo;
import cn.comtom.system.fw.SystemMapper;
import cn.comtom.system.main.org.entity.dbo.Organization;

@Mapper
@Repository
public interface OrganizationMapper extends SystemMapper<Organization,String> {
	
	/**
	 * 分页按条件查询
	 * @param request
	 * @return
	 */
	List<OrganizationInfo> queryList(OrgPageRequest request);

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 */
	OrganizationInfo getOrgById(String id);
}
