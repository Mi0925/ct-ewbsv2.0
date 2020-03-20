package cn.comtom.core.main.ebm.dao;

import cn.comtom.core.fw.BaseDao;
import cn.comtom.core.fw.BaseMapper;
import cn.comtom.core.fw.CoreMapper;
import cn.comtom.core.main.ebm.entity.dbo.Ebm;
import cn.comtom.core.main.ebm.entity.dbo.EbmStateBack;
import cn.comtom.core.main.ebm.mapper.EbmMapper;
import cn.comtom.core.main.ebm.mapper.EbmStateBackMapper;
import cn.comtom.domain.core.access.info.AccessRecordInfo;
import cn.comtom.domain.core.ebm.info.EbmStateBackInfo;
import cn.comtom.domain.core.ebm.request.EbmPageRequest;
import cn.comtom.tools.constants.SymbolConstants;
import cn.comtom.tools.utils.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.util.List;
import java.util.Map;

@Repository
public class EbmStateBackDao extends BaseDao<EbmStateBack,String> {

    @Autowired
    private EbmStateBackMapper mapper;


    @Override
    public CoreMapper<EbmStateBack, String> getMapper() {
        return mapper;
    }

    public List<EbmStateBackInfo> list(Map<String,Object> map) {
        return mapper.queryList(map);
    }
}
