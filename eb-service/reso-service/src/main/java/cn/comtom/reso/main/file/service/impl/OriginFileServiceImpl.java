package cn.comtom.reso.main.file.service.impl;

import cn.comtom.domain.reso.file.info.OriginFileInfo;
import cn.comtom.domain.reso.file.request.OriginFileAddRequest;
import cn.comtom.domain.reso.file.request.OriginFilePageRequest;
import cn.comtom.reso.fw.BaseDao;
import cn.comtom.reso.fw.BaseServiceImpl;
import cn.comtom.reso.main.file.dao.OriginFileDao;
import cn.comtom.reso.main.file.entity.dbo.OriginFile;
import cn.comtom.reso.main.file.service.IOriginFileService;
import cn.comtom.tools.utils.UUIDGenerator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OriginFileServiceImpl extends BaseServiceImpl<OriginFile,String> implements IOriginFileService {

    @Autowired
    private OriginFileDao dao;

    @Override
    public BaseDao<OriginFile, String> getDao() {
        return dao;
    }

    @Override
    public OriginFileInfo save(OriginFileAddRequest request) {
        OriginFile originFile = new OriginFile();
        BeanUtils.copyProperties(request,originFile);
        String fileId = UUIDGenerator.getUUID();
        originFile.setFileId(fileId);
        originFile.setCreateTime(new Date());
        if(StringUtils.isBlank(request.getStatus())){
            originFile.setStatus("1");
        }
        dao.save(originFile);
        OriginFileInfo info = new OriginFileInfo();
        BeanUtils.copyProperties(originFile,info);
        return info;
    }

    @Override
    public List<OriginFileInfo> list(OriginFilePageRequest request) {
        List<OriginFile> list = dao.list(request);
        return Optional.ofNullable(list).orElse(Collections.emptyList()).stream().filter(Objects::nonNull).map(originFile -> {
            OriginFileInfo info = new OriginFileInfo();
            BeanUtils.copyProperties(originFile,info);
            return info;
        }).collect(Collectors.toList());
    }

    @Override
    public OriginFileInfo getByMd5(String md5) {
        OriginFile file = dao.getByMd5(md5);
        if(file == null){
            return null;
        }
        OriginFileInfo info = new OriginFileInfo();
        BeanUtils.copyProperties(file,info);
        return info;
    }
}
