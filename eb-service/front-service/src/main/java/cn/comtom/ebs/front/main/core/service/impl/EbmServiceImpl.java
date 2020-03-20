package cn.comtom.ebs.front.main.core.service.impl;

import cn.comtom.domain.core.ebd.info.EbdFilesInfo;
import cn.comtom.domain.core.ebd.info.EbdInfo;
import cn.comtom.domain.core.ebm.info.EbmInfo;
import cn.comtom.domain.core.ebm.request.EbmPageRequest;
import cn.comtom.domain.reso.file.info.OriginFileInfo;
import cn.comtom.ebs.front.fegin.CoreFegin;
import cn.comtom.ebs.front.fegin.service.ICoreFeginService;
import cn.comtom.ebs.front.fegin.service.IResoFeginService;
import cn.comtom.ebs.front.main.core.service.IEbmService;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiPageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class EbmServiceImpl implements IEbmService {

    @Autowired
    private ICoreFeginService coreFeginService;

    @Autowired
    private CoreFegin coreFegin;

    @Autowired
    private IResoFeginService resoFeginService;

    @Override
    public ApiPageResponse<EbmInfo> getEbmInfoByPage(EbmPageRequest request) {

        return coreFeginService.getEbmInfoByPage(request);
    }

    @Override
    public ApiEntityResponse<EbmInfo> getEbmInfoById(String ebmId) {
        return coreFegin.getEbmInfoByEbmId(ebmId);
    }

    @Override
    public List<EbdFilesInfo> getEbdFilesByEbmId(String ebmId) {
        return null;
    }
}
