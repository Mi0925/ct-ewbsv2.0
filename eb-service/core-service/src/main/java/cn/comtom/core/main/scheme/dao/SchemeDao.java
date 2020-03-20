package cn.comtom.core.main.scheme.dao;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.comtom.core.fw.BaseDao;
import cn.comtom.core.fw.CoreMapper;
import cn.comtom.core.main.scheme.entity.dbo.Scheme;
import cn.comtom.core.main.scheme.mapper.SchemeMapper;
import cn.comtom.domain.core.scheme.info.SchemeInfo;
import cn.comtom.domain.core.scheme.info.SchemePageInfo;
import cn.comtom.domain.core.scheme.request.SchemeQueryRequest;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;


@Repository
public class SchemeDao extends BaseDao<Scheme,String> {

    @Autowired
    private SchemeMapper mapper;


    @Override
    public CoreMapper<Scheme, String> getMapper() {
        return mapper;
    }


    public List<SchemePageInfo> list(SchemeQueryRequest request) {
        return mapper.getSchemePageInfo(request);
    }

    public SchemeInfo getSchemeInfoByEbmId(String ebmId) {
        Scheme scheme = new Scheme();
        scheme.setEbmId(ebmId);
        List<Scheme> schemeList = mapper.select(scheme);
        if(CollectionUtils.isEmpty(schemeList)) {
        	return new SchemeInfo();
        }
        return Optional.ofNullable(schemeList).orElse(Collections.emptyList()).stream()
                .filter(Objects::nonNull)
                .map(scheme1 -> {
                    SchemeInfo schemeInfo = new SchemeInfo();
                    BeanUtils.copyProperties(scheme1,schemeInfo);
                    return schemeInfo;
                }).findFirst().orElseGet(null);
    }

    public List<Scheme> getSchemeInfoByProgramId(String programId) {
        Weekend<Scheme> weekend = Weekend.of(Scheme.class);
        WeekendCriteria<Scheme,Object> weekendCriteria = weekend.weekendCriteria();
        weekendCriteria.andEqualTo(Scheme::getProgramId,programId);
        return mapper.selectByExample(weekend);
    }
}
