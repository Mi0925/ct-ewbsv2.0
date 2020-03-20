package cn.comtom.ebs.front.main.core.service;

import cn.comtom.domain.core.ebm.info.EbmBrdRecordInfo;
import cn.comtom.domain.core.ebm.request.EbmBrdRecordPageRequest;

import java.util.List;

public interface IEbmBrdRecordService {

    List<EbmBrdRecordInfo> page(EbmBrdRecordPageRequest req);
}
