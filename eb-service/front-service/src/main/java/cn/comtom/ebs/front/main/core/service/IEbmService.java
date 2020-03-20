package cn.comtom.ebs.front.main.core.service;

import cn.comtom.domain.core.ebd.info.EbdFilesInfo;
import cn.comtom.domain.core.ebm.info.EbmInfo;
import cn.comtom.domain.core.ebm.request.EbmPageRequest;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiPageResponse;

import java.util.List;

public interface IEbmService {
    ApiPageResponse<EbmInfo> getEbmInfoByPage(EbmPageRequest request);

    ApiEntityResponse<EbmInfo> getEbmInfoById(String ebmId);

    List<EbdFilesInfo> getEbdFilesByEbmId(String ebmId);
}
