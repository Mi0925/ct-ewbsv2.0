package cn.comtom.core.main.program.dao;

import cn.comtom.core.fw.BaseDao;
import cn.comtom.core.fw.CoreMapper;
import cn.comtom.core.main.program.entity.dbo.ProgramUnit;
import cn.comtom.core.main.program.mapper.ProgramUnitMapper;
import cn.comtom.domain.core.program.info.ProgramUnitInfo;
import cn.comtom.domain.core.program.request.ProgramUnitPageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProgramUnitDao extends BaseDao<ProgramUnit,String> {

    @Autowired
    private ProgramUnitMapper mapper;

    @Override
    public CoreMapper<ProgramUnit, String> getMapper() {
        return mapper;
    }


    public List<ProgramUnitInfo> getProgramUnitInfoList(ProgramUnitPageRequest request) {
        return mapper.getProgramUnitInfoList(request);
    }
}
