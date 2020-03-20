package cn.comtom.core.main.ebm.service;


import cn.comtom.core.fw.BaseService;
import cn.comtom.core.main.ebm.entity.dbo.EbmStateBack;
import cn.comtom.domain.core.ebm.info.EbmStateBackInfo;
import cn.comtom.domain.core.ebm.request.EbmStateBackQueryRequest;

import java.util.List;

public interface IEbmStateBackService extends BaseService<EbmStateBack,String> {

    List<EbmStateBackInfo> list(EbmStateBackQueryRequest request);
}
