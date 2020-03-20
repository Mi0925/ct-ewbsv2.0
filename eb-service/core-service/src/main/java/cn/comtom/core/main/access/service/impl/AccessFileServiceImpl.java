package cn.comtom.core.main.access.service.impl;

import cn.comtom.core.fw.BaseDao;
import cn.comtom.core.fw.BaseServiceImpl;
import cn.comtom.core.main.access.dao.AccessFileDao;
import cn.comtom.core.main.access.entity.dbo.AccessFile;
import cn.comtom.core.main.access.service.IAccessFileService;
import cn.comtom.domain.core.access.info.AccessFileInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AccessFileServiceImpl extends BaseServiceImpl<AccessFile,String> implements IAccessFileService {

    @Autowired
    private AccessFileDao accessFileDao;

    @Override
    public BaseDao<AccessFile, String> getDao() {
        return accessFileDao;
    }

    @Override
    public List<AccessFileInfo> getByInfoId(String infoId) {
        List<AccessFile> list = accessFileDao.getByInfoId(infoId);
        return Optional.ofNullable(list).orElse(Collections.emptyList()).stream().filter(Objects::nonNull).map(access -> {
            AccessFileInfo info = new AccessFileInfo();
            BeanUtils.copyProperties(access,info);
            return info;
        }).collect(Collectors.toList());
    }
}
