package cn.comtom.core.main.ebd.dao;

import cn.comtom.core.fw.BaseDao;
import cn.comtom.core.fw.CoreMapper;
import cn.comtom.core.main.ebd.entity.dbo.EbdResponse;
import cn.comtom.core.main.ebd.mapper.EbdResponseMapper;
import cn.comtom.domain.core.ebd.request.EbdResponsePageRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.util.List;


@Repository
public class EbdResponseDao extends BaseDao<EbdResponse,String> {

    @Autowired
    private EbdResponseMapper mapper;


    @Override
    public CoreMapper<EbdResponse, String> getMapper() {
        return mapper;
    }


    public List<EbdResponse> list(EbdResponsePageRequest request) {
        Weekend<EbdResponse> weekend = Weekend.of(EbdResponse.class);
        WeekendCriteria<EbdResponse, Object> weekendCriteria = weekend.weekendCriteria();
        if(StringUtils.isNotBlank(request.getEbdSrcEbrId())){
            weekendCriteria.andEqualTo(EbdResponse::getEbdSrcEbrId,request.getEbdSrcEbrId());
        }
        if(StringUtils.isNotBlank(request.getEbdType())){
            weekendCriteria.andEqualTo(EbdResponse::getEbdType,request.getEbdType());
        }
        if(StringUtils.isNotBlank(request.getEbdVersion())){
            weekendCriteria.andEqualTo(EbdResponse::getEbdVersion,request.getEbdVersion());
        }
        if(StringUtils.isNotBlank(request.getRelatedEbdId())){
            weekendCriteria.andEqualTo(EbdResponse::getRelatedEbdId,request.getRelatedEbdId());
        }
        if(StringUtils.isNotBlank(request.getResultCode())){
            weekendCriteria.andEqualTo(EbdResponse::getResultCode,request.getResultCode());
        }
        if(StringUtils.isNotBlank(request.getSendFlag())){
            weekendCriteria.andEqualTo(EbdResponse::getSendFlag,request.getSendFlag());
        }
        return mapper.selectByExample(weekend);
    }
}
