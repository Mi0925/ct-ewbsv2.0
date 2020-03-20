package cn.comtom.reso.main.ebr.dao;

import cn.comtom.domain.reso.ebr.info.EbrTerminalInfo;
import cn.comtom.domain.reso.ebr.request.EbrTerminalPageRequest;
import cn.comtom.domain.reso.ebr.request.TerminalConditionRequest;
import cn.comtom.domain.reso.ebr.request.TerminalWhereRequest;
import cn.comtom.reso.fw.BaseDao;
import cn.comtom.reso.fw.ResoMapper;
import cn.comtom.reso.main.ebr.entity.dbo.EbrTerminal;
import cn.comtom.reso.main.ebr.mapper.EbrTerminalMapper;
import cn.comtom.tools.utils.DateUtil;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.util.List;


@Repository
public class EbrTerminalDao extends BaseDao<EbrTerminal,String> {

    @Autowired
    private EbrTerminalMapper mapper;


    @Override
    public ResoMapper<EbrTerminal, String> getMapper() {
        return mapper;
    }

    public List<EbrTerminalInfo> getList(EbrTerminalPageRequest request) {
        return mapper.getListWithXml(request);
    }

    public List<EbrTerminalInfo> findListByTremIds(List<String> devTrmIds) {

        return mapper.findListByTremIds(devTrmIds);
    }

    public List<EbrTerminal> findTerminalListByWhere(TerminalWhereRequest request) {
        Weekend<EbrTerminal> weekend = Weekend.of(EbrTerminal.class);
        WeekendCriteria<EbrTerminal,Object> weekendCriteria = weekend.weekendCriteria();
        if(request.getSyncFlag() != null){
            weekendCriteria.andEqualTo(EbrTerminal::getSyncFlag,request.getSyncFlag());
        }
        if(request.getStatusSyncFlag() != null){
            weekendCriteria.andEqualTo(EbrTerminal::getStatusSyncFlag,request.getStatusSyncFlag());
        }
        if(request.getRptStartTime() != null ){
            weekendCriteria.andGreaterThan(EbrTerminal::getUpdateTime,DateUtil.stringToDate(request.getRptStartTime()) );
        }
        if(request.getRptEndTime() != null ){
            weekendCriteria.andLessThan(EbrTerminal::getUpdateTime,DateUtil.stringToDate(request.getRptEndTime()));
        }
        return mapper.selectByExample(weekend);
    }

    public int saveOneTerminal(EbrTerminal ebrTerminal) {
        return mapper.insertSelective(ebrTerminal);
    }

	public Integer countTerminalByCondition(TerminalConditionRequest req) {
		Weekend<EbrTerminal> weekend = Weekend.of(EbrTerminal.class);
        WeekendCriteria<EbrTerminal,Object> weekendCriteria = weekend.weekendCriteria();
        if(StringUtils.isNotBlank(req.getTerminalState())) {
        	weekendCriteria.andEqualTo(EbrTerminal::getTerminalState, req.getTerminalState());
        }
        if(CollectionUtils.isNotEmpty(req.getAreCodes())) {
        	for (int i = 0; i < req.getAreCodes().size(); i++) {
				if(i == 0) {
					weekendCriteria.andLike(EbrTerminal::getTerminalEbrId, "%".concat(req.getAreCodes().get(i)).concat("%"));
				}else {
					weekendCriteria.orLike(EbrTerminal::getTerminalEbrId, "%".concat(req.getAreCodes().get(i)).concat("%"));
				}
			}
        }
		return mapper.selectCountByExample(weekend);
	}
}
