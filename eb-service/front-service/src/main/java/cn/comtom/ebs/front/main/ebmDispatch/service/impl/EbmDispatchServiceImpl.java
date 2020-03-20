package cn.comtom.ebs.front.main.ebmDispatch.service.impl;

import cn.comtom.domain.core.ebm.info.EbmDispatchAndEbdFileInfo;
import cn.comtom.ebs.front.fegin.service.ICoreFeginService;
import cn.comtom.ebs.front.main.ebmDispatch.service.IEbmDispatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author:WJ
 * @date: 2019/1/19 0019
 * @time: 上午 11:24
 */
@Service
public class EbmDispatchServiceImpl implements IEbmDispatchService {

    @Autowired
    private ICoreFeginService coreFeginService;

    @Override
    public List<EbmDispatchAndEbdFileInfo> getEbmDispatchAndEbdFileByEbmId(String ebmId) {
        return coreFeginService.getEbmDispatchAndEbdFileByEbmId(ebmId);
    }
}
