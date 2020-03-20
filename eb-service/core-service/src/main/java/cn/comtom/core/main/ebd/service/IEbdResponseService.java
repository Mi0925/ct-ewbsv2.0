package cn.comtom.core.main.ebd.service;

import cn.comtom.core.fw.BaseService;
import cn.comtom.core.main.ebd.entity.dbo.EbdResponse;
import cn.comtom.domain.core.ebd.info.EbdResponseInfo;
import cn.comtom.domain.core.ebd.request.EbdResponsePageRequest;

import java.util.List;

public interface IEbdResponseService extends BaseService<EbdResponse,String> {

    List<EbdResponseInfo> list(EbdResponsePageRequest request);
}
