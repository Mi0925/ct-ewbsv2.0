package cn.comtom.core.main.ebm.dao;

import cn.comtom.core.fw.BaseDao;
import cn.comtom.core.fw.CoreMapper;
import cn.comtom.core.main.ebm.entity.dbo.EbmRes;
import cn.comtom.core.main.ebm.mapper.EbmResMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.util.List;

@Repository
public class EbmResDao extends BaseDao<EbmRes,String> {

    @Autowired
    private EbmResMapper mapper;


    @Override
    public CoreMapper<EbmRes, String> getMapper() {
        return mapper;
    }

    public List<EbmRes> getEbmResListByBrdItemId(String brdItemId) {
        Weekend<EbmRes> weekend = Weekend.of(EbmRes.class);
        WeekendCriteria<EbmRes,Object> weekendCriteria = weekend.weekendCriteria();
        weekendCriteria.andEqualTo(EbmRes::getBrdItemId,brdItemId);
        return mapper.selectByExample(weekend);
    }
}
