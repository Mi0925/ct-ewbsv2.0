package cn.comtom.ebs.front.main.accessRecord.service.impl;

import cn.comtom.domain.core.access.info.AccessRecordInfo;
import cn.comtom.domain.core.access.request.AccessRecordPageRequest;
import cn.comtom.ebs.front.fegin.service.ICoreFeginService;
import cn.comtom.ebs.front.main.accessRecord.service.IAccessRecordService;
import cn.comtom.tools.response.ApiPageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author:WJ
 * @date: 2018/12/25 0025
 * @time: 下午 7:42
 */
@Service
public class AccessRecordServiceImpl implements IAccessRecordService {

    @Autowired
    private ICoreFeginService coreFeginService;

    @Override
    public ApiPageResponse<AccessRecordInfo> findAccessRecordPage(AccessRecordPageRequest req) {
        return coreFeginService.findAccessRecordPage(req);
    }
}
