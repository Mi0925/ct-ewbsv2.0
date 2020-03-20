package cn.comtom.reso.main.ebr.service;

import java.util.List;

import cn.comtom.domain.reso.ebr.info.EbrAdapterInfo;
import cn.comtom.domain.reso.ebr.request.AdapterWhereRequest;
import cn.comtom.domain.reso.ebr.request.EbrAdapterPageRequest;
import cn.comtom.reso.fw.BaseService;
import cn.comtom.reso.main.ebr.entity.dbo.EbrAdapter;

public interface IEbrAdapterService extends BaseService<EbrAdapter,String> {

    /**
     * 根据条件适配器列表
     * @param request
     * @return
     */
    List<EbrAdapterInfo> findAdapterListByWhere(AdapterWhereRequest request);

	/**
	 * 根据适配器id查询列表
	 * @param adapterIds
	 * @return
	 */
    List<EbrAdapterInfo> findListByAdapterIds(List<String> adapterIds);

	/**
	 * 按条件分页查询
	 * @param request
	 * @return
	 */
	List<EbrAdapterInfo> getList(EbrAdapterPageRequest request);
	
	public int save(EbrAdapter t, String channel);
	
	public int update(EbrAdapter t, String channel);
}
