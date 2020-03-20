package cn.comtom.reso.main.ebr.dao;

import cn.comtom.domain.reso.ebr.info.EbrInfo;
import cn.comtom.domain.reso.ebr.request.EbrQueryRequest;
import cn.comtom.reso.fw.BaseDao;
import cn.comtom.reso.fw.ResoMapper;
import cn.comtom.reso.main.ebr.entity.dbo.VEbr;
import cn.comtom.reso.main.ebr.mapper.VEbrMapper;
import cn.comtom.tools.constants.SymbolConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.util.List;

@Repository
public class VEbrDao extends BaseDao<VEbr,String> {

    @Autowired
    private VEbrMapper mapper;


    @Override
    public ResoMapper<VEbr, String> getMapper() {
        return mapper;
    }


    public List<VEbr> list(EbrQueryRequest request) {
        Weekend<VEbr> weekend = Weekend.of(VEbr.class);
        WeekendCriteria<VEbr,Object> weekendCriteria = weekend.weekendCriteria();
        if(StringUtils.isNotBlank(request.getEbrId())){
            weekendCriteria.andEqualTo(VEbr::getEbrId,request.getEbrId());
        }
        if(StringUtils.isNotBlank(request.getEbrState())){
            weekendCriteria.andEqualTo(VEbr::getEbrState,request.getEbrState());
        }
        if(StringUtils.isNotBlank(request.getEbrType())){
            weekendCriteria.andEqualTo(VEbr::getEbrType,request.getEbrType());
        }
        if(StringUtils.isNotBlank(request.getEbrLevel())){
            weekendCriteria.andEqualTo(VEbr::getEbrLevel,request.getEbrLevel());
        }
        if(StringUtils.isNotBlank(request.getEbrName())){
            weekendCriteria.andLike(VEbr::getEbrName,SymbolConstants.MATH_CHARACTER_PERCENT.concat(request.getEbrName()).concat(SymbolConstants.MATH_CHARACTER_PERCENT));
        }
        if(request.getAreaCodes() != null && request.getAreaCodes().size() > 0){
            weekendCriteria.andIn(VEbr::getAreaCode,request.getAreaCodes());
        }
        return mapper.selectByExample(weekend);
    }

    public List<EbrInfo> listForInfoSrc(EbrQueryRequest queryRequest) {
        return mapper.listForInfoSrc(queryRequest);
    }
}
