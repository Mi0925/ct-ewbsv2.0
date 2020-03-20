package cn.comtom.reso.main.ebr.dao;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.function.DoubleUnaryOperator;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.comtom.domain.reso.ebr.info.EbrAdapterInfo;
import cn.comtom.domain.reso.ebr.request.AdapterWhereRequest;
import cn.comtom.domain.reso.ebr.request.EbrAdapterPageRequest;
import cn.comtom.reso.fw.BaseDao;
import cn.comtom.reso.fw.ResoMapper;
import cn.comtom.reso.main.ebr.entity.dbo.EbrAdapter;
import cn.comtom.reso.main.ebr.mapper.EbrAdapterMapper;
import cn.comtom.tools.utils.DateUtil;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;


@Repository
public class EbrAdapterDao extends BaseDao<EbrAdapter,String> {

    @Autowired
    private EbrAdapterMapper mapper;


    @Override
    public ResoMapper<EbrAdapter, String> getMapper() {
        return mapper;
    }

    public List<EbrAdapter> findAdapterListByWhere(AdapterWhereRequest request) {
 	   Weekend<EbrAdapter> weekend = Weekend.of(EbrAdapter.class);
       WeekendCriteria<EbrAdapter,Object> weekendCriteria = weekend.weekendCriteria();
       if(request.getSyncFlag() != null){
           weekendCriteria.andEqualTo(EbrAdapter::getSyncFlag,request.getSyncFlag());
       }
       if(request.getStatusSyncFlag() != null){
           weekendCriteria.andEqualTo(EbrAdapter::getStatusSyncFlag,request.getStatusSyncFlag());
       }
       if(StringUtils.isNotBlank(request.getRptStartTime())){
           weekendCriteria.andGreaterThan(EbrAdapter::getUpdateTime,DateUtil.stringToDate(request.getRptStartTime()) );
       }
       if(StringUtils.isNotBlank(request.getRptEndTime())){
           weekendCriteria.andLessThan(EbrAdapter::getUpdateTime,DateUtil.stringToDate(request.getRptEndTime()));
       }
       return mapper.selectByExample(weekend);
    }

	public List<EbrAdapterInfo> findListByAdapterIds(List<String> adapterIds) {

		return mapper.findListByAdapterIds(adapterIds);
	}

	public List<EbrAdapterInfo> getList(EbrAdapterPageRequest request) {

		return mapper.getListWithXml(request);
	}

	public int saveAdapter(EbrAdapter ebrAdapter) {
        return mapper.insertSelective(ebrAdapter);
    }

}
