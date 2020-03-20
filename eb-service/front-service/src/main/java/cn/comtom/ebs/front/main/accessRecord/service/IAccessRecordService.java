package cn.comtom.ebs.front.main.accessRecord.service;

import cn.comtom.domain.core.access.info.AccessRecordInfo;
import cn.comtom.domain.core.access.request.AccessRecordPageRequest;
import cn.comtom.tools.response.ApiPageResponse;

/**
 * @author:WJ
 * @date: 2018/12/25 0025
 * @time: 下午 7:42
 */
public interface IAccessRecordService {
    ApiPageResponse<AccessRecordInfo> findAccessRecordPage(AccessRecordPageRequest req);
}
