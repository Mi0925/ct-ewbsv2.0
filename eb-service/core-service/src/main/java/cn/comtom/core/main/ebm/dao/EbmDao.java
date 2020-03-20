package cn.comtom.core.main.ebm.dao;

import cn.comtom.core.fw.BaseDao;
import cn.comtom.core.fw.CoreMapper;
import cn.comtom.core.main.ebm.entity.dbo.Ebm;
import cn.comtom.core.main.ebm.mapper.EbmMapper;
import cn.comtom.domain.core.ebm.request.EbmPageRequest;
import cn.comtom.tools.constants.SymbolConstants;
import cn.comtom.tools.utils.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.util.List;

@Repository
public class EbmDao extends BaseDao<Ebm,String> {

    @Autowired
    private EbmMapper mapper;


    @Override
    public CoreMapper<Ebm, String> getMapper() {
        return mapper;
    }

    public List<Ebm> getDispatchEbm(String ebmState) {
        Weekend<Ebm> weekend = Weekend.of(Ebm.class);
        WeekendCriteria<Ebm,Object> weekendCriteria = weekend.weekendCriteria();
        weekendCriteria.andEqualTo(Ebm::getEbmState,ebmState);
        return mapper.selectByExample(weekend);
    }

    public List<Ebm> list(EbmPageRequest request) {
        Weekend<Ebm> weekend = Weekend.of(Ebm.class);
        WeekendCriteria<Ebm,Object> weekendCriteria = weekend.weekendCriteria();
        if(StringUtils.isNotBlank(request.getEbmId())){
            weekendCriteria.andLike(Ebm::getEbmId,request.getEbmId());
        }
        if(StringUtils.isNotBlank(request.getMsgTitle())){
            weekendCriteria.andLike(Ebm::getMsgTitle,SymbolConstants.MATH_CHARACTER_PERCENT.concat(request.getMsgTitle()).concat(SymbolConstants.MATH_CHARACTER_PERCENT));
        }
        if(StringUtils.isNotBlank(request.getSendName())){
            weekendCriteria.andLike(Ebm::getSendName,SymbolConstants.MATH_CHARACTER_PERCENT.concat(request.getSendName()).concat(SymbolConstants.MATH_CHARACTER_PERCENT));
        }
        if(StringUtils.isNotBlank(request.getSenderCode())){
            weekendCriteria.andEqualTo(Ebm::getSenderCode,request.getSenderCode());
        }
        if(StringUtils.isNotBlank(request.getEbmState())){
            weekendCriteria.andEqualTo(Ebm::getEbmState,request.getEbmState());
        }
        if(StringUtils.isNotBlank(request.getAuditResult())){
            weekendCriteria.andEqualTo(Ebm::getAuditResult,request.getAuditResult());
        }
        if(StringUtils.isNotBlank(request.getEventType())){
            weekendCriteria.andEqualTo(Ebm::getEventType,request.getEventType());
        }
        if(StringUtils.isNotBlank(request.getFlowId())){
            weekendCriteria.andEqualTo(Ebm::getFlowId,request.getFlowId());
        }
        if(StringUtils.isNotBlank(request.getMsgType())){
            weekendCriteria.andEqualTo(Ebm::getMsgType,request.getMsgType());
        }
        if(StringUtils.isNotBlank(request.getSchemeId())){
            weekendCriteria.andEqualTo(Ebm::getSchemeId,request.getSchemeId());
        }
        if(StringUtils.isNotBlank(request.getSendFlag())){
            weekendCriteria.andEqualTo(Ebm::getSendFlag,request.getSendFlag());
        }
        if(StringUtils.isNotBlank(request.getSeverity())){
            weekendCriteria.andEqualTo(Ebm::getSeverity,request.getSeverity());
        }
        if(request.getBroadcastState() != null){
            weekendCriteria.andEqualTo(Ebm::getBroadcastState,request.getBroadcastState());
        }
        if(StringUtils.isNotBlank(request.getStartTime())){
            weekendCriteria.andGreaterThan(Ebm::getSendTime,DateUtil.stringToDate(request.getStartTime()));
        }
        if(StringUtils.isNotBlank(request.getEndTime())){
            weekendCriteria.andLessThan(Ebm::getSendTime,DateUtil.stringToDate(request.getEndTime()));
        }
        return mapper.selectByExample(weekend);
    }
}
