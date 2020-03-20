package cn.comtom.reso.main.ebr.dao;

import cn.comtom.domain.reso.ebr.info.EbrIdCreatorInfo;
import cn.comtom.reso.fw.BaseDao;
import cn.comtom.reso.fw.ResoMapper;
import cn.comtom.reso.main.ebr.entity.dbo.EbrIdCreator;
import cn.comtom.reso.main.ebr.mapper.EbrIdCreatorMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.util.List;


@Repository
public class EbrIdCreatorDao extends BaseDao<EbrIdCreator,String> {

    @Autowired
    private EbrIdCreatorMapper mapper;


    @Override
    public ResoMapper<EbrIdCreator, String> getMapper() {
        return mapper;
    }

    public List<EbrIdCreator> getSourceTypeCodeSNByCond(EbrIdCreatorInfo creatorInfo) {
        Weekend weekend = Weekend.of(EbrIdCreator.class);
        WeekendCriteria<EbrIdCreator,String> weekendCriteria = weekend.weekendCriteria();
        if(StringUtils.isNotBlank(creatorInfo.getSourceLevel())){
            weekendCriteria.andEqualTo(EbrIdCreator::getSourceLevel,creatorInfo.getSourceLevel());
        }
        if(StringUtils.isNotBlank(creatorInfo.getAreaCode())){
            weekendCriteria.andEqualTo(EbrIdCreator::getAreaCode,creatorInfo.getAreaCode());
        }
        if(StringUtils.isNotBlank(creatorInfo.getSourceTypeCode())){
            weekendCriteria.andEqualTo(EbrIdCreator::getSourceTypeCode,creatorInfo.getSourceTypeCode());
        }
        return mapper.selectByExample(weekend);
    }


    public List<EbrIdCreator> getByCondition(EbrIdCreatorInfo creatorInfo) {
        Weekend weekend = Weekend.of(EbrIdCreator.class);
        WeekendCriteria<EbrIdCreator,Object> weekendCriteria = weekend.weekendCriteria();
        if(StringUtils.isNotBlank(creatorInfo.getSourceLevel())){
            weekendCriteria.andEqualTo(EbrIdCreator::getSourceLevel,creatorInfo.getSourceLevel());
        }
        if(StringUtils.isNotBlank(creatorInfo.getAreaCode())){
            weekendCriteria.andEqualTo(EbrIdCreator::getAreaCode,creatorInfo.getAreaCode());
        }
        if(StringUtils.isNotBlank(creatorInfo.getSourceTypeCode())){
            weekendCriteria.andEqualTo(EbrIdCreator::getSourceTypeCode,creatorInfo.getSourceTypeCode());
        }
        if(StringUtils.isNotBlank(creatorInfo.getSourceSubTypeCode())){
            weekendCriteria.andEqualTo(EbrIdCreator::getSourceSubTypeCode,creatorInfo.getSourceSubTypeCode());
        }
        return mapper.selectByExample(weekend);
    }
}
