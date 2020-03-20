package cn.comtom.reso.main.file.dao;

import cn.comtom.domain.reso.file.request.OriginFilePageRequest;
import cn.comtom.reso.fw.BaseDao;
import cn.comtom.reso.fw.ResoMapper;
import cn.comtom.reso.main.file.entity.dbo.OriginFile;
import cn.comtom.reso.main.file.mapper.OriginFileMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.util.List;

@Repository
public class OriginFileDao extends BaseDao<OriginFile,String> {

    @Autowired
    private OriginFileMapper mapper;


    @Override
    public ResoMapper<OriginFile, String> getMapper() {
        return mapper;
    }


    public List<OriginFile> list(OriginFilePageRequest request) {
        Weekend<OriginFile> weekend = Weekend.of(OriginFile.class);
        WeekendCriteria<OriginFile, Object> weekendCriteria = weekend.weekendCriteria();
        if(StringUtils.isNotBlank(request.getOriginName())){
            weekendCriteria.andLike(OriginFile::getOriginName,"%".concat(request.getOriginName()).concat("%"));
        }
        if(StringUtils.isNotBlank(request.getFileType())){
            weekendCriteria.andEqualTo(OriginFile::getFileType,request.getFileType());
        }
        if(StringUtils.isNotBlank(request.getStatus())){
            weekendCriteria.andEqualTo(OriginFile::getStatus,request.getStatus());
        }
        if(StringUtils.isNotBlank(request.getMd5Code())){
            weekendCriteria.andEqualTo(OriginFile::getMd5Code,request.getMd5Code());
        }
        if(StringUtils.isNotBlank(request.getFileExt())){
            weekendCriteria.andEqualTo(OriginFile::getFileExt,request.getFileExt());
        }
        return mapper.selectByExample(weekend);
    }

    public OriginFile getByMd5(String md5) {
        OriginFile originFile = new OriginFile();
        originFile.setMd5Code(md5);
        return mapper.selectOne(originFile);
    }
}
