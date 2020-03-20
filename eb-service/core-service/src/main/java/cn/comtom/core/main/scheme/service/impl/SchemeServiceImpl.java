package cn.comtom.core.main.scheme.service.impl;

import cn.comtom.core.fw.BaseDao;
import cn.comtom.core.fw.BaseServiceImpl;
import cn.comtom.core.main.scheme.dao.SchemeDao;
import cn.comtom.core.main.scheme.entity.dbo.Scheme;
import cn.comtom.core.main.scheme.service.ISchemeService;
import cn.comtom.domain.core.scheme.info.SchemeInfo;
import cn.comtom.domain.core.scheme.info.SchemePageInfo;
import cn.comtom.domain.core.scheme.request.SchemeQueryRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class SchemeServiceImpl extends BaseServiceImpl<Scheme,String> implements ISchemeService {

    @Autowired
    private SchemeDao dao;

    @Override
    public BaseDao<Scheme, String> getDao() {
        return dao;
    }

    @Override
    public List<SchemePageInfo> list(SchemeQueryRequest request) {

        return dao.list(request);
    }

    @Override
    public SchemeInfo getSchemeInfoByEbmId(String ebmId) {
        return dao.getSchemeInfoByEbmId(ebmId);
    }

    @Override
    public SchemeInfo getSchemeInfoByProgramId(String programId) {
        List<Scheme> schemeList = dao.getSchemeInfoByProgramId(programId);
        Scheme scheme = Optional.ofNullable(schemeList).orElse(Collections.emptyList()).stream()
                .filter(Objects::nonNull)
                .findFirst()
                .orElseGet(Scheme::new);
        SchemeInfo schemeInfo = new SchemeInfo();
        BeanUtils.copyProperties(scheme,schemeInfo);
        return schemeInfo;
    }
}
