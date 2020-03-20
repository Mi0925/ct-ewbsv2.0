package cn.comtom.core.main.ebm.dao;

import cn.comtom.core.fw.BaseDao;
import cn.comtom.core.fw.CoreMapper;
import cn.comtom.core.main.ebm.entity.dbo.EbmDispatch;
import cn.comtom.core.main.ebm.mapper.EbmDispatchMapper;
import cn.comtom.domain.core.ebm.info.EbmDispatchAndEbdFileInfo;
import cn.comtom.domain.core.ebm.info.EbmDispatchInfo;
import cn.comtom.domain.core.ebm.request.EbmDispatchPageRequest;
import cn.comtom.tools.enums.SysDictCodeEnum;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class EbmDispatchDao extends BaseDao<EbmDispatch,String> {

    @Autowired
    private EbmDispatchMapper mapper;


    @Override
    public CoreMapper<EbmDispatch, String> getMapper() {
        return mapper;
    }

    public List<EbmDispatch> getEbmDispatchByEbmId(String ebmId) {
        Weekend<EbmDispatch> weekend = Weekend.of(EbmDispatch.class);
        WeekendCriteria<EbmDispatch,Object> weekendCriteria = weekend.weekendCriteria();
        weekendCriteria.andEqualTo(EbmDispatch::getEbmId,ebmId);
        return mapper.selectByExample(weekend);
    }

    public List<EbmDispatch> getEbmDispatchByEbdId(String ebdId) {
        Weekend<EbmDispatch> weekend = Weekend.of(EbmDispatch.class);
        WeekendCriteria<EbmDispatch,Object> weekendCriteria = weekend.weekendCriteria();
        weekendCriteria.andEqualTo(EbmDispatch::getEbdId,ebdId);
        return mapper.selectByExample(weekend);
    }

    public List<EbmDispatch> getEbmDispatchList(EbmDispatchPageRequest req) {
        Weekend<EbmDispatch> weekend = Weekend.of(EbmDispatch.class);
        WeekendCriteria<EbmDispatch,Object> weekendCriteria = weekend.weekendCriteria();
        if(StringUtils.isNotBlank(req.getState())){
            weekendCriteria.andEqualTo(EbmDispatch::getState,req.getState());
        }
        if(StringUtils.isNotBlank(req.getEbmId())){
            weekendCriteria.andEqualTo(EbmDispatch::getEbmId,req.getEbmId());
        }
        if(StringUtils.isNotBlank(req.getEbdId())){
            weekendCriteria.andEqualTo(EbmDispatch::getEbdId,req.getEbdId());
        }
        if(StringUtils.isNotBlank(req.getBrdSysType())){
            weekendCriteria.andEqualTo(EbmDispatch::getBrdSysType,req.getBrdSysType());
        }
        if(StringUtils.isNotBlank(req.getPsEbrId())){
            weekendCriteria.andEqualTo(EbmDispatch::getPsEbrId,req.getPsEbrId());
        }
        if(StringUtils.isNotBlank(req.getBsEbrId())){
            weekendCriteria.andEqualTo(EbmDispatch::getBsEbrId,req.getBsEbrId());
        }
        return mapper.selectByExample(weekend);
    }

    public List<EbmDispatch> getEbmDispatchByMatchedEbdId(String matchedEbdId) {
        Weekend<EbmDispatch> weekend = Weekend.of(EbmDispatch.class);
        WeekendCriteria<EbmDispatch,Object> weekendCriteria = weekend.weekendCriteria();
        weekendCriteria.andEqualTo(EbmDispatch::getMatchEbdId,matchedEbdId);
        return mapper.selectByExample(weekend);
    }

    public List<EbmDispatchAndEbdFileInfo> getEbmDispatchAndEbdFileByEbmId(String ebmId) {
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("ebmId",ebmId);
        map.put("DISPATCH_STATUS_CODE",SysDictCodeEnum.DISPATCH_STATUS.getCode());
        return  mapper.queryList(map);
    }
}
