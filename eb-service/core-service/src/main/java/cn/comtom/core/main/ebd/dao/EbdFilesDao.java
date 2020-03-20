package cn.comtom.core.main.ebd.dao;

import cn.comtom.core.fw.BaseDao;
import cn.comtom.core.fw.CoreMapper;
import cn.comtom.core.main.ebd.entity.dbo.EbdFiles;
import cn.comtom.core.main.ebd.mapper.EbdFilesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.util.List;


@Repository
public class EbdFilesDao extends BaseDao<EbdFiles,String> {

    @Autowired
    private EbdFilesMapper mapper;


    @Override
    public CoreMapper<EbdFiles, String> getMapper() {
        return mapper;
    }


    public List<EbdFiles> getByEbdId(String ebdId) {
        Weekend<EbdFiles> weekend = Weekend.of(EbdFiles.class);
        WeekendCriteria<EbdFiles,Object> weekendCriteria = weekend.weekendCriteria();
        weekendCriteria.andEqualTo(EbdFiles::getEbdId,ebdId);
        return mapper.selectByExample(weekend);
    }
}
