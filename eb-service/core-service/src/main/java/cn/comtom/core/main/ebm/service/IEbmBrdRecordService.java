package cn.comtom.core.main.ebm.service;

import cn.comtom.core.fw.BaseService;
import cn.comtom.core.main.ebm.entity.dbo.EbmBrdRecord;
import cn.comtom.domain.core.ebm.info.EbmBrdRecordInfo;
import cn.comtom.domain.core.ebm.request.EbmBrdRecordPageRequest;
import cn.comtom.domain.core.ebm.request.EbmBrdRecordWhereRequest;

import java.util.List;

public interface IEbmBrdRecordService extends BaseService<EbmBrdRecord,String> {
    List<EbmBrdRecordInfo> getEbmBrdRecordListByEbmId(String ebmId);

    List<EbmBrdRecordInfo> findEbmBrdRecordListByWhere(EbmBrdRecordWhereRequest request);

    EbmBrdRecordInfo getEbmBrdByEbmIdAndResourceId(String ebmId,String resourceId);

    List<EbmBrdRecordInfo> page(EbmBrdRecordPageRequest req);
}
