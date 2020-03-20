package cn.comtom.core.main.scheme.dao;

import cn.comtom.core.fw.BaseDao;
import cn.comtom.core.fw.CoreMapper;
import cn.comtom.core.main.scheme.entity.dbo.SchemeEbr;
import cn.comtom.core.main.scheme.mapper.SchemeEbrMapper;
import cn.comtom.domain.core.scheme.info.SchemeEbrInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class SchemeEbrDao extends BaseDao<SchemeEbr,String> {

    @Autowired
    private SchemeEbrMapper mapper;

    @Override
    public CoreMapper<SchemeEbr, String> getMapper() {
        return mapper;
    }


    public List<SchemeEbrInfo> getSchemeEbrListBySchemeId(String schemeId) {
        return mapper.getSchemeEbrListBySchemeId(schemeId);
    }
}
