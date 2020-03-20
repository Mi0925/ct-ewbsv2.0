package cn.comtom.reso.main.file.dao;

import cn.comtom.domain.reso.file.info.FileLibraryInfo;
import cn.comtom.domain.reso.file.request.FileLibraryPageRequest;
import cn.comtom.reso.fw.BaseDao;
import cn.comtom.reso.fw.ResoMapper;
import cn.comtom.reso.main.file.entity.dbo.FileLibrary;
import cn.comtom.reso.main.file.mapper.FileLibraryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class FileLibraryDao extends BaseDao<FileLibrary,String> {

    @Autowired
    private FileLibraryMapper mapper;


    @Override
    public ResoMapper<FileLibrary, String> getMapper() {
        return mapper;
    }


    public List<FileLibraryInfo> getList(FileLibraryPageRequest request) {
        return mapper.getListWithXml(request);
    }

    public Integer deleteThisAndSubAll(String libId) {
        Map<String,Object> map = new HashMap<>();
        map.put("libId",libId);
        mapper.callProcedureDeleteFileAll(map);
        return map.get("cnt") == null? 0 : Integer.valueOf(map.get("cnt").toString());
    }
}
