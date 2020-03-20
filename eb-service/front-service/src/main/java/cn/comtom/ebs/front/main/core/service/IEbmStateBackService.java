package cn.comtom.ebs.front.main.core.service;

import cn.comtom.domain.core.ebm.info.EbmStateBackInfo;
import cn.comtom.domain.core.ebm.request.EbmStateBackQueryRequest;

import java.util.List;

public interface IEbmStateBackService  {

    List<EbmStateBackInfo> list(EbmStateBackQueryRequest request);
}
