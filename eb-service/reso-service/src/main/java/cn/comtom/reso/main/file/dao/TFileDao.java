package cn.comtom.reso.main.file.dao;

import cn.comtom.domain.reso.file.info.FileInfo;
import cn.comtom.domain.reso.file.request.FilePageRequest;
import cn.comtom.reso.fw.BaseDao;
import cn.comtom.reso.fw.ResoMapper;
import cn.comtom.reso.main.file.entity.dbo.TFile;
import cn.comtom.reso.main.file.mapper.FileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Repository
public class TFileDao extends BaseDao<TFile,String> {

    @Autowired
    private FileMapper mapper;


    @Override
    public ResoMapper<TFile, String> getMapper() {
        return mapper;
    }


    public TFile getByMd5(String md5Code) {
        TFile tFile = new TFile();
        tFile.setMd5Code(md5Code);
        List<TFile> list = mapper.select(tFile);
        return CollectionUtils.isEmpty(list) == true ? null : list.get(0);
    }

    public List<FileInfo> getList(FilePageRequest request) {
        return mapper.getListFromXml(request);
    }
}
