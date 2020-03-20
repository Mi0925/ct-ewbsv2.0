package cn.comtom.core.main.ebd.dao;

import cn.comtom.core.fw.BaseDao;
import cn.comtom.core.fw.CoreMapper;
import cn.comtom.core.main.ebd.entity.dbo.Ebd;
import cn.comtom.core.main.ebd.mapper.EbdMapper;
import cn.comtom.domain.core.ebd.info.EbdInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
public class EbdDao extends BaseDao<Ebd,String> {

    @Autowired
    private EbdMapper mapper;


    @Override
    public CoreMapper<Ebd, String> getMapper() {
        return mapper;
    }


    public List<Ebd> getByEbmId(String ebmId) {
        Ebd ebd = new Ebd();
        ebd.setEbmId(ebmId);
        return mapper.select(ebd);
    }

    public List<EbdInfo> list(Map<String,Object> mapRequest) {
        return  mapper.queryList(mapRequest);
    }
}
