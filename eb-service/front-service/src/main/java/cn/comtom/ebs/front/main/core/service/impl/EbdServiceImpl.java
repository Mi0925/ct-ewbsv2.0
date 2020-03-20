package cn.comtom.ebs.front.main.core.service.impl;

import cn.comtom.domain.core.ebd.info.EbdInfo;
import cn.comtom.domain.core.ebd.request.EbdPageRequest;
import cn.comtom.ebs.front.fegin.service.ICoreFeginService;
import cn.comtom.ebs.front.main.core.service.IEbdService;
import cn.comtom.tools.response.ApiPageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author:WJ
 * @date: 2018/12/25 0025
 * @time: 下午 7:42
 */
@Service
public class EbdServiceImpl implements IEbdService {

    @Autowired
    private ICoreFeginService coreFeginService;

    @Override
    public ApiPageResponse<EbdInfo> findPage(EbdPageRequest req) {

        return coreFeginService.findEbdPage(req);
    }

    @Override
    public List<EbdInfo> getEbdInfoByEbmId(String ebmId) {
        return coreFeginService.getEbdInfoByEbmId(ebmId);
    }
}
