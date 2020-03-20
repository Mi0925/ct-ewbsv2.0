package cn.comtom.reso.main.ebr.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import cn.comtom.reso.fw.ResoMapper;
import cn.comtom.reso.main.ebr.entity.dbo.EbrChannel;

@Mapper
@Repository
public interface EbrChannelMapper extends ResoMapper<EbrChannel, String> {
	
    String getChannelByEbrId(String ebrId);

	void updateChannelByEbrId(EbrChannel ebrChannel);
}
