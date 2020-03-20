package cn.comtom.ebs.front.main.ebr.service.impl;

import cn.comtom.domain.reso.ebr.info.EbrPlatformInfo;
import cn.comtom.domain.reso.ebr.request.EbrPlatformPageRequest;
import cn.comtom.domain.reso.ebr.request.PlatformAddRequest;
import cn.comtom.domain.reso.ebr.request.PlatformUpdateRequest;
import cn.comtom.domain.reso.ebr.request.PlatformWhereRequest;
import cn.comtom.ebs.front.fegin.service.IResoFeginService;
import cn.comtom.ebs.front.main.ebr.service.IPlatformService;
import cn.comtom.tools.response.ApiPageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;

/**
 * @author:WJ
 * @date: 2018/12/25 0025
 * @time: 下午 7:42
 */
@Service
public class PlatformServiceImpl implements IPlatformService {

    @Autowired
    private IResoFeginService resoFeginService;


    @Override
    public List<EbrPlatformInfo> list() {
        PlatformWhereRequest whereRequest=new PlatformWhereRequest();
        List<EbrPlatformInfo> list = resoFeginService.findPlatformListByWhere(whereRequest);
        return list;
    }

    @Override
    public EbrPlatformInfo findById(String id) {
        return resoFeginService.findPlatfomById(id);
    }

    @Override
    public ApiPageResponse<EbrPlatformInfo> getEbrPlatformInfoByPage(EbrPlatformPageRequest pageRequest) {

        return resoFeginService.getEbrPlatformInfoByPage(pageRequest);
    }

    @Override
    public EbrPlatformInfo save(PlatformAddRequest request) {
        return resoFeginService.saveEbrPlatform(request);
    }

    @Override
    public Boolean update(PlatformUpdateRequest request) {
        return resoFeginService.updatePlatform(request);
    }

    @Override
    public Boolean delete(String ebrId) {
        return resoFeginService.deleteEbrPlatformInfo(ebrId);
    }

	@Override
	public Boolean checkEbrAlarmVal(String resType, String paramValue) throws ParseException {
		Double rate = resoFeginService.getFailureRate(resType);
		NumberFormat nf = NumberFormat.getPercentInstance();
		Double configVal = nf.parse(paramValue).doubleValue();
		return rate>=configVal;
	}
	
}
