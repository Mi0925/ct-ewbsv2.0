package cn.comtom.ebs.front.main.core.service.impl;

import cn.comtom.domain.core.ebm.info.EbmBrdRecordInfo;
import cn.comtom.domain.core.ebm.request.EbmBrdRecordPageRequest;
import cn.comtom.ebs.front.fegin.service.ICoreFeginService;
import cn.comtom.ebs.front.main.core.service.IEbmBrdRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author:WJ
 * @date: 2019/1/15 0015
 * @time: 下午 8:05
 */
@Service
public class EbmBrdRecordServiceImpl implements IEbmBrdRecordService {

    @Autowired
    private ICoreFeginService coreFeginService;


    @Override
    public List<EbmBrdRecordInfo> page(EbmBrdRecordPageRequest req) {
        return coreFeginService.getEbmBrdRecordInfoPage(req);
    }
}
