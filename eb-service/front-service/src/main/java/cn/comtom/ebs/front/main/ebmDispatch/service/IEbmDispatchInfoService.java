package cn.comtom.ebs.front.main.ebmDispatch.service;

import cn.comtom.domain.core.ebm.info.EbmDispatchInfo;
import cn.comtom.domain.core.ebm.info.EbmDispatchInfoInfo;
import cn.comtom.domain.core.ebm.request.EbmDispatchInfoUpdateRequest;

/**
 * @author:WJ
 * @date: 2019/1/19 0019
 * @time: 上午 11:24
 */
public interface IEbmDispatchInfoService {


    EbmDispatchInfoInfo getEbmDispatchInfoByEbmId(String ebmId);

    EbmDispatchInfo updateEbmDispatchInfo(EbmDispatchInfoUpdateRequest updateRequest);
}
