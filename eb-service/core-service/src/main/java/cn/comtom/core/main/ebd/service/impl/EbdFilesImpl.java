package cn.comtom.core.main.ebd.service.impl;

import cn.comtom.core.fw.BaseDao;
import cn.comtom.core.fw.BaseServiceImpl;
import cn.comtom.core.main.ebd.dao.EbdFilesDao;
import cn.comtom.core.main.ebd.entity.dbo.EbdFiles;
import cn.comtom.core.main.ebd.service.IEbdFilesService;
import cn.comtom.domain.core.ebd.info.EbdFilesInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EbdFilesImpl extends BaseServiceImpl<EbdFiles,String> implements IEbdFilesService {

    @Autowired
    private EbdFilesDao dao;

    @Override
    public BaseDao<EbdFiles, String> getDao() {
        return dao;
    }

    @Override
    public List<EbdFilesInfo> getByEbdId(String ebdId) {
        List<EbdFiles> ebdFilesList = dao.getByEbdId(ebdId);
        return Optional.ofNullable(ebdFilesList).orElse(Collections.emptyList()).stream().filter(Objects::nonNull).map(ebdFile ->{
            EbdFilesInfo ebdFilesInfo = new EbdFilesInfo();
            BeanUtils.copyProperties(ebdFile,ebdFilesInfo);
            return  ebdFilesInfo;
        }).collect(Collectors.toList());
    }
}
