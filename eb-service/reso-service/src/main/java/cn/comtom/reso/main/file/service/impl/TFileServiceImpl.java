package cn.comtom.reso.main.file.service.impl;

import cn.comtom.domain.reso.file.info.FileInfo;
import cn.comtom.domain.reso.file.request.FilePageRequest;
import cn.comtom.reso.fw.BaseDao;
import cn.comtom.reso.fw.BaseServiceImpl;
import cn.comtom.reso.main.file.dao.TFileDao;
import cn.comtom.reso.main.file.entity.dbo.TFile;
import cn.comtom.reso.main.file.service.ITFileService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TFileServiceImpl extends BaseServiceImpl<TFile,String> implements ITFileService {

    @Autowired
    private TFileDao dao;

    @Override
    public BaseDao<TFile, String> getDao() {
        return dao;
    }


    @Override
    public FileInfo getByMd5(String md5Code) {
        TFile file = dao.getByMd5(md5Code);
        if(file == null){
            return null;
        }
        FileInfo info = new FileInfo();
        BeanUtils.copyProperties(file,info);
        return info;
    }

    @Override
    public List<FileInfo> getList(FilePageRequest request) {
        return dao.getList(request);
    }
}
