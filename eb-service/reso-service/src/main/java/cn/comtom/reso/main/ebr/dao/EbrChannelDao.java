package cn.comtom.reso.main.ebr.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.comtom.reso.fw.BaseDao;
import cn.comtom.reso.fw.ResoMapper;
import cn.comtom.reso.main.ebr.entity.dbo.EbrChannel;
import cn.comtom.reso.main.ebr.mapper.EbrChannelMapper;

@Repository
public class EbrChannelDao extends BaseDao<EbrChannel,String> {
	
	@Autowired
    private EbrChannelMapper mapper;
	
	@Override
	public ResoMapper<EbrChannel, String> getMapper() {
		
		return mapper;
	}

	public String getChannelByEbrId(String ebrId) {
		
		return mapper.getChannelByEbrId(ebrId);
	}

	public void updateByEbrId(EbrChannel ebrChannel) {
		
		mapper.updateChannelByEbrId(ebrChannel);
	}
	
}
