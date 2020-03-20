package cn.comtom.reso.main.file.mapper;

import cn.comtom.domain.reso.file.info.FileLibraryInfo;
import cn.comtom.domain.reso.file.request.FileLibraryPageRequest;
import cn.comtom.reso.fw.ResoMapper;
import cn.comtom.reso.main.file.entity.dbo.FileLibrary;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface FileLibraryMapper extends ResoMapper<FileLibrary,String> {
    List<FileLibraryInfo> getListWithXml(FileLibraryPageRequest request);

    void callProcedureDeleteFileAll(Map<String ,Object> map);
}
