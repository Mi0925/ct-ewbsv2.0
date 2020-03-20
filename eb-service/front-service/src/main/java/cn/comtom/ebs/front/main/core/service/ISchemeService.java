package cn.comtom.ebs.front.main.core.service;

import cn.comtom.domain.core.scheme.request.SchemeUpdateRequest;

public interface ISchemeService {

    String getEbmDispatchInfoTitle(String msgTitle);

    Boolean auditSchemeInfo(SchemeUpdateRequest request);
}
