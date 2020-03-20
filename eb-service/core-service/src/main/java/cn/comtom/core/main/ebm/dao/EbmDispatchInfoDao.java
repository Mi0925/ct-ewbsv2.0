package cn.comtom.core.main.ebm.dao;

import cn.comtom.core.fw.BaseDao;
import cn.comtom.core.fw.CoreMapper;
import cn.comtom.core.main.ebm.entity.dbo.EbmDispatchInfo;
import cn.comtom.core.main.ebm.mapper.EbmDispatchInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EbmDispatchInfoDao extends BaseDao<EbmDispatchInfo,String> {

    @Autowired
    private EbmDispatchInfoMapper mapper;


    @Override
    public CoreMapper<EbmDispatchInfo, String> getMapper() {
        return mapper;
    }


    public List<EbmDispatchInfo> getByEbmId(String ebmId) {
        EbmDispatchInfo ebmDispatchInfo = new EbmDispatchInfo();
        ebmDispatchInfo.setEbmId(ebmId);
        return mapper.select(ebmDispatchInfo);
    }
}
