package cn.comtom.ebs.front.main.ebmDispatch.service.impl;

import cn.comtom.domain.core.ebm.info.EbmDispatchInfo;
import cn.comtom.domain.core.ebm.info.EbmDispatchInfoInfo;
import cn.comtom.domain.core.ebm.request.EbmDispatchInfoUpdateRequest;
import cn.comtom.ebs.front.fegin.service.ICoreFeginService;
import cn.comtom.ebs.front.main.ebmDispatch.service.IEbmDispatchInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author:WJ
 * @date: 2019/1/19 0019
 * @time: 上午 11:24
 */
@Service
public class EbmDispatchInfoServiceImpl implements IEbmDispatchInfoService {

    @Autowired
    private ICoreFeginService coreFeginService;

    @Override
    public EbmDispatchInfoInfo getEbmDispatchInfoByEbmId(String ebmId) {

        return coreFeginService.getEbmDispatchInfoByEbmId(ebmId);
    }

    @Override
    public EbmDispatchInfo updateEbmDispatchInfo(EbmDispatchInfoUpdateRequest updateRequest) {
        return coreFeginService.updateEbmDispatchInfo(updateRequest);
    }
}
