package cn.comtom.reso.main.file.service.impl;

import cn.comtom.domain.reso.file.info.FileLibraryInfo;
import cn.comtom.domain.reso.file.request.FileLibraryPageRequest;
import cn.comtom.reso.fw.BaseDao;
import cn.comtom.reso.fw.BaseServiceImpl;
import cn.comtom.reso.main.file.dao.FileLibraryDao;
import cn.comtom.reso.main.file.entity.dbo.FileLibrary;
import cn.comtom.reso.main.file.service.IFileLibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TFileLibraryServiceImpl extends BaseServiceImpl<FileLibrary,String> implements IFileLibraryService {

    @Autowired
    private FileLibraryDao dao;

    @Override
    public BaseDao<FileLibrary, String> getDao() {
        return dao;
    }


    @Override
    public List<FileLibraryInfo> getList(FileLibraryPageRequest request) {
        return dao.getList(request);
    }

    @Override
    public Integer deleteThisAndSubAll(String libId) {
        return dao.deleteThisAndSubAll(libId);
    }

}
