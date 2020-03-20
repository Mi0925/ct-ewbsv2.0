package cn.comtom.core.main.scheme.service;

import cn.comtom.core.fw.BaseService;
import cn.comtom.core.main.scheme.entity.dbo.Scheme;
import cn.comtom.domain.core.scheme.info.SchemeInfo;
import cn.comtom.domain.core.scheme.info.SchemePageInfo;
import cn.comtom.domain.core.scheme.request.SchemeQueryRequest;

import java.util.List;

public interface ISchemeService extends BaseService<Scheme,String> {

    List<SchemePageInfo> list(SchemeQueryRequest request);

    SchemeInfo getSchemeInfoByEbmId(String ebmId);

    SchemeInfo getSchemeInfoByProgramId(String programId);
}
