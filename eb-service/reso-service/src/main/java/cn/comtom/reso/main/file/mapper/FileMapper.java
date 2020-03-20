package cn.comtom.reso.main.file.mapper;

import cn.comtom.domain.reso.file.info.FileInfo;
import cn.comtom.domain.reso.file.request.FilePageRequest;
import cn.comtom.reso.fw.ResoMapper;
import cn.comtom.reso.main.file.entity.dbo.TFile;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface FileMapper extends ResoMapper<TFile,String> {
    List<FileInfo> getListFromXml(FilePageRequest request);
}
