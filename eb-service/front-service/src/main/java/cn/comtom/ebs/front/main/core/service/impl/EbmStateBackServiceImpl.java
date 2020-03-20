package cn.comtom.ebs.front.main.core.service.impl;


import cn.comtom.domain.core.ebm.info.EbmStateBackInfo;
import cn.comtom.domain.core.ebm.request.EbmStateBackQueryRequest;
import cn.comtom.ebs.front.fegin.service.ICoreFeginService;
import cn.comtom.ebs.front.main.core.service.IEbmStateBackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EbmStateBackServiceImpl  implements IEbmStateBackService {

    @Autowired
    private ICoreFeginService coreFeginService;

    @Override
    public List<EbmStateBackInfo> list(EbmStateBackQueryRequest request) {
        return coreFeginService.findEbmStateBackListByEbmId(request);
    }
}
