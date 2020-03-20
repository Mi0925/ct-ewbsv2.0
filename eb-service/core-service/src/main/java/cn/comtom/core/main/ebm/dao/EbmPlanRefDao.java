package cn.comtom.core.main.ebm.dao;

import cn.comtom.core.fw.BaseDao;
import cn.comtom.core.fw.CoreMapper;
import cn.comtom.core.main.ebm.entity.dbo.EbmPlanRef;
import cn.comtom.core.main.ebm.mapper.EbmPlanRefMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

@Repository
public class EbmPlanRefDao extends BaseDao<EbmPlanRef,String> {

    @Autowired
    private EbmPlanRefMapper mapper;


    @Override
    public CoreMapper<EbmPlanRef, String> getMapper() {
        return mapper;
    }


    public EbmPlanRef getOneByEbmId(String ebmId) {
        Weekend<EbmPlanRef> weekend = Weekend.of(EbmPlanRef.class);
        WeekendCriteria<EbmPlanRef,Object> weekendCriteria = weekend.weekendCriteria();
        weekendCriteria.andEqualTo(EbmPlanRef::getEbmId,ebmId);
        return mapper.selectOneByExample(weekend);
    }
}
