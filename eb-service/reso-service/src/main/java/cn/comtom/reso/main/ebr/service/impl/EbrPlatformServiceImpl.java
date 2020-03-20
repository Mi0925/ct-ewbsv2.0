package cn.comtom.reso.main.ebr.service.impl;

import cn.comtom.domain.reso.ebr.info.EbrInfo;
import cn.comtom.domain.reso.ebr.info.EbrPlatformInfo;
import cn.comtom.domain.reso.ebr.request.EbrAdapterPageRequest;
import cn.comtom.domain.reso.ebr.request.EbrPlatformPageRequest;
import cn.comtom.domain.reso.ebr.request.PlatformWhereRequest;
import cn.comtom.reso.fw.BaseDao;
import cn.comtom.reso.fw.BaseServiceImpl;
import cn.comtom.reso.main.ebr.dao.EbrChannelDao;
import cn.comtom.reso.main.ebr.dao.EbrPlatformDao;
import cn.comtom.reso.main.ebr.entity.dbo.EbrAdapter;
import cn.comtom.reso.main.ebr.entity.dbo.EbrBroadcast;
import cn.comtom.reso.main.ebr.entity.dbo.EbrChannel;
import cn.comtom.reso.main.ebr.entity.dbo.EbrPlatform;
import cn.comtom.reso.main.ebr.entity.dbo.EbrStation;
import cn.comtom.reso.main.ebr.service.IEbrPlatformService;
import cn.comtom.tools.enums.CommonDictEnum;
import cn.comtom.tools.enums.ResTypeDictEnum;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EbrPlatformServiceImpl extends BaseServiceImpl<EbrPlatform,String> implements IEbrPlatformService {

    @Autowired
    private EbrPlatformDao dao;

    @Autowired
    private EbrChannelDao ebrChannelDao;
    
    @Override
    public BaseDao<EbrPlatform, String> getDao() {
        return dao;
    }

    @Override
    public List<EbrPlatformInfo> findPlatformListByWhere(PlatformWhereRequest request) {
        List<EbrPlatform> ebrPlatformList = dao.findPlatformListByWhere(request);
        return Optional.ofNullable(ebrPlatformList).orElse(Collections.emptyList()).stream().filter(Objects::nonNull).map(ebrPlatform -> {
            EbrPlatformInfo ebrPlatformInfo = new EbrPlatformInfo();
            BeanUtils.copyProperties(ebrPlatform,ebrPlatformInfo);
            return ebrPlatformInfo;
        }).collect(Collectors.toList());
    }

    @Override
    public EbrInfo getEbrInfoById(String ebrId) {
        return dao.getEbrInfoById(ebrId);
    }

    @Override
    public List<EbrPlatformInfo> list(EbrPlatformPageRequest request) {
        return dao.list(request);
    }
    
    @Override
	public int save(EbrPlatform t,String channel) {
    	setEbrChannel(t,channel);
		return super.save(t);
	}
    
	@Override
	public int update(EbrPlatform t,String channel) {
		setEbrChannel(t,channel);
		return super.update(t);
	}
    
	private void setEbrChannel(EbrPlatform t,String channel) {
		if(StringUtils.isBlank(channel)) {
			return;
		}
		String channelByEbrId = ebrChannelDao.getChannelByEbrId(t.getPsEbrId());
		EbrChannel ebrChannel = new EbrChannel();
		ebrChannel.setEbrId(t.getPsEbrId());
		ebrChannel.setSendChannel(channel);
		if(StringUtils.isBlank(channelByEbrId)) {
			ebrChannelDao.save(ebrChannel);
		}else {
			ebrChannelDao.updateByEbrId(ebrChannel);
		}
	}
	
   @Override
   public Double countFailureRate(String resType) {
	   String tableName = "";
	   String fieldName = "";
	   if(StringUtils.equals("01", resType)) {
		   tableName = "bc_ebr_platform";
		   fieldName = "psState";
	   }else if(StringUtils.equals("02", resType)) {
		   tableName = "bc_ebr_adapter";
		   fieldName = "adapterState";
	   }else if(StringUtils.equals("03", resType)) {
		   tableName = "bc_ebr_broadcast";
		   fieldName = "bsState";
	   }else if(StringUtils.equals("04", resType)) {
		   tableName = "bc_ebr_terminal";
		   fieldName = "terminalState";
	   }else if(StringUtils.equals("05", resType)) {
		   tableName = "bc_ebr_station";
		   fieldName = "stationState";
	   }
	   HashMap<String, String> params = Maps.newHashMap();
	   params.put("tableName", tableName);
	   params.put("fieldName", fieldName);
	   double totalNums = dao.getAllCount(params);
	   double nomalNums = dao.getNormalCount(params);
	   double rate = (totalNums - nomalNums)/totalNums;
	   BigDecimal bg = new BigDecimal(rate);
       return bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

}
