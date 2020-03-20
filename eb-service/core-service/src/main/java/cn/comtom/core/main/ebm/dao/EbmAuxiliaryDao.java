package cn.comtom.core.main.ebm.dao;

import cn.comtom.core.fw.BaseDao;
import cn.comtom.core.fw.CoreMapper;
import cn.comtom.core.main.ebm.entity.dbo.EbmAuxiliary;
import cn.comtom.core.main.ebm.mapper.EbmAuxiliaryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.util.List;

@Repository
public class EbmAuxiliaryDao extends BaseDao<EbmAuxiliary,String> {

    @Autowired
    private EbmAuxiliaryMapper mapper;


    @Override
    public CoreMapper<EbmAuxiliary, String> getMapper() {
        return mapper;
    }

    public List<EbmAuxiliary> getEbmAuxiliaryInfoByEbmId(String ebmId) {
        Weekend<EbmAuxiliary> weekend = Weekend.of(EbmAuxiliary.class);
        WeekendCriteria<EbmAuxiliary,Object> weekendCriteria = weekend.weekendCriteria();
        weekendCriteria.andEqualTo(EbmAuxiliary::getEbmId,ebmId);
        return mapper.selectByExample(weekend);
    }
}
