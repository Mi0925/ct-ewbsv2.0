package cn.comtom.ebs.front.main.core.service;

import cn.comtom.domain.core.access.info.AccessRecordInfo;
import cn.comtom.domain.core.access.request.AccessRecordPageRequest;
import cn.comtom.domain.core.ebd.info.EbdInfo;
import cn.comtom.domain.core.ebd.request.EbdPageRequest;
import cn.comtom.tools.response.ApiPageResponse;

import java.util.List;

/**
 * @author:WJ
 * @date: 2018/12/25 0025
 * @time: 下午 7:42
 */
public interface IEbdService {

    ApiPageResponse<EbdInfo> findPage(EbdPageRequest req);

    List<EbdInfo> getEbdInfoByEbmId(String ebmId);
}
