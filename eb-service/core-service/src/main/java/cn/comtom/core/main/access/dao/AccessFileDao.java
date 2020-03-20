package cn.comtom.core.main.access.dao;

import cn.comtom.core.fw.BaseDao;
import cn.comtom.core.fw.CoreMapper;
import cn.comtom.core.main.access.entity.dbo.AccessFile;
import cn.comtom.core.main.access.mapper.AccessFileMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.util.List;

@Repository
public class AccessFileDao extends BaseDao<AccessFile,String> {

    @Autowired
    private AccessFileMapper mapper;

    @Override
    public CoreMapper<AccessFile, String> getMapper() {
        return mapper;
    }

    public List<AccessFile> getByInfoId(String infoId) {
        Weekend<AccessFile> weekend = Weekend.of(AccessFile.class);
        WeekendCriteria<AccessFile,Object> weekendCriteria = weekend.weekendCriteria();
        if(StringUtils.isNotBlank(infoId)){
            weekendCriteria.andEqualTo(AccessFile::getInfoId,infoId);
        }
        return mapper.selectByExample(weekend);
    }
}
