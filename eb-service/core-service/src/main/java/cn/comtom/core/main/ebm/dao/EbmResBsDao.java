package cn.comtom.core.main.ebm.dao;

import cn.comtom.core.fw.BaseDao;
import cn.comtom.core.fw.CoreMapper;
import cn.comtom.core.main.ebm.entity.dbo.EbmResBs;
import cn.comtom.core.main.ebm.mapper.EbmResBsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.util.List;

@Repository
public class EbmResBsDao extends BaseDao<EbmResBs,String> {

    @Autowired
    private EbmResBsMapper mapper;


    @Override
    public CoreMapper<EbmResBs, String> getMapper() {
        return mapper;
    }

    public List<EbmResBs> getEbmResBsByResourceId(String brdItemId) {
        Weekend<EbmResBs> weekend = Weekend.of(EbmResBs.class);
        WeekendCriteria<EbmResBs,Object> weekendCriteria = weekend.weekendCriteria();
        weekendCriteria.andEqualTo(EbmResBs::getBrdItemId,brdItemId);
        return mapper.selectByExample(weekend);
    }

    public List<EbmResBs> getByBrdItemIdIdAndSysInfo(String brdItemId, String brdSysInfo) {
        Weekend<EbmResBs> weekend = Weekend.of(EbmResBs.class);
        WeekendCriteria<EbmResBs,Object> weekendCriteria = weekend.weekendCriteria();
        weekendCriteria.andEqualTo(EbmResBs::getBrdItemId,brdItemId);
        weekendCriteria.andEqualTo(EbmResBs::getBrdSysInfo,brdSysInfo);
        return mapper.selectByExample(weekend);
    }
}
