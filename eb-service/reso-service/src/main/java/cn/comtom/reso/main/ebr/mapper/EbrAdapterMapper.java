package cn.comtom.reso.main.ebr.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import cn.comtom.domain.reso.ebr.info.EbrAdapterInfo;
import cn.comtom.domain.reso.ebr.request.EbrAdapterPageRequest;
import cn.comtom.reso.fw.ResoMapper;
import cn.comtom.reso.main.ebr.entity.dbo.EbrAdapter;

@Mapper
@Repository
public interface EbrAdapterMapper extends ResoMapper<EbrAdapter,String> {

	List<EbrAdapterInfo> findListByAdapterIds(List<String> adapterIds);

	List<EbrAdapterInfo> getListWithXml(EbrAdapterPageRequest request);


}
