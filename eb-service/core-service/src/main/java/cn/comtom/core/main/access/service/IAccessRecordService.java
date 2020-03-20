package cn.comtom.core.main.access.service;

import cn.comtom.core.fw.BaseService;
import cn.comtom.core.main.access.entity.dbo.AccessRecord;
import cn.comtom.domain.core.access.info.AccessRecordInfo;
import cn.comtom.domain.core.access.request.AccessRecordPageRequest;

import java.util.List;

public interface IAccessRecordService extends BaseService<AccessRecord,String> {
    List<AccessRecordInfo> list(AccessRecordPageRequest req);
}
