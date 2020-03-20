package cn.comtom.ebs.front.main.ebr.service;

import cn.comtom.domain.reso.ebr.info.EbrPlatformInfo;
import cn.comtom.domain.reso.ebr.request.EbrPlatformPageRequest;
import cn.comtom.domain.reso.ebr.request.PlatformAddRequest;
import cn.comtom.domain.reso.ebr.request.PlatformUpdateRequest;
import cn.comtom.tools.response.ApiPageResponse;

import java.text.ParseException;
import java.util.List;

/**
 * @author:WJ
 * @date: 2018/12/25 0025
 * @time: 下午 7:42
 */
public interface IPlatformService {

   List<EbrPlatformInfo> list();

    EbrPlatformInfo findById(String paramValue);

    ApiPageResponse<EbrPlatformInfo> getEbrPlatformInfoByPage(EbrPlatformPageRequest pageRequest);

    EbrPlatformInfo save(PlatformAddRequest request);

    Boolean update(PlatformUpdateRequest request);

    Boolean delete(String ebrId);

	Boolean checkEbrAlarmVal(String resType, String paramValue) throws ParseException ;
}
