package cn.comtom.core.main.access.service;

import cn.comtom.core.fw.BaseService;
import cn.comtom.core.main.access.entity.dbo.AccessArea;
import cn.comtom.domain.core.access.info.AccessAreaInfo;
import cn.comtom.domain.core.access.request.AccessAreaReq;

import java.util.List;

public interface IAccessAreaService extends BaseService<AccessArea,String> {

    List<AccessAreaInfo> list(AccessAreaReq req);

    List<AccessAreaInfo> getByInfoId(String infoId);
}
