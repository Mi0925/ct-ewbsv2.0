package cn.comtom.core.main.omd.dao;

import cn.comtom.core.fw.BaseDao;
import cn.comtom.core.fw.CoreMapper;
import cn.comtom.core.main.omd.entity.dbo.OmdRequest;
import cn.comtom.core.main.omd.mapper.OmdRequestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class OmdRequestDao extends BaseDao<OmdRequest,String> {

    @Autowired
    private OmdRequestMapper mapper;


    @Override
    public CoreMapper<OmdRequest, String> getMapper() {
        return mapper;
    }



}
