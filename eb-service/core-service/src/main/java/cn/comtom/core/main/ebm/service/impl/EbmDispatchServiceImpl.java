package cn.comtom.core.main.ebm.service.impl;

import cn.comtom.core.fw.BaseDao;
import cn.comtom.core.fw.BaseServiceImpl;
import cn.comtom.core.main.ebm.dao.EbmDispatchDao;
import cn.comtom.core.main.ebm.entity.dbo.EbmDispatch;
import cn.comtom.core.main.ebm.service.IEbmDispatchService;
import cn.comtom.domain.core.ebm.info.EbmDispatchAndEbdFileInfo;
import cn.comtom.domain.core.ebm.info.EbmDispatchInfo;
import cn.comtom.domain.core.ebm.request.EbmDispatchPageRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EbmDispatchServiceImpl extends BaseServiceImpl<EbmDispatch, String> implements IEbmDispatchService {

    @Autowired
    private EbmDispatchDao dao;

    @Override
    public BaseDao<EbmDispatch, String> getDao() {
        return dao;
    }

    @Override
    public List<EbmDispatchInfo> getEbmDispatchByEbmId(String ebmId) {
        List<EbmDispatch> ebmDispatchList = dao.getEbmDispatchByEbmId(ebmId);
        return Optional.ofNullable(ebmDispatchList).orElse(Collections.emptyList()).stream().filter(Objects::nonNull).map(ebmDispatch -> {
            EbmDispatchInfo ebmDispatchInfo = new EbmDispatchInfo();
            BeanUtils.copyProperties(ebmDispatch, ebmDispatchInfo);
            return ebmDispatchInfo;
        }).collect(Collectors.toList());
    }

    @Override
    public List<EbmDispatchInfo> getEbmDispatchByEbdId(String ebdId) {
        List<EbmDispatch> ebmDispatchList = dao.getEbmDispatchByEbdId(ebdId);
        return Optional.ofNullable(ebmDispatchList).orElse(Collections.emptyList()).stream().filter(Objects::nonNull).map(ebmDispatch -> {
            EbmDispatchInfo ebmDispatchInfo = new EbmDispatchInfo();
            BeanUtils.copyProperties(ebmDispatch, ebmDispatchInfo);
            return ebmDispatchInfo;
        }).collect(Collectors.toList());
    }

    @Override
    public List<EbmDispatchInfo> getEbmDispatchList(EbmDispatchPageRequest req) {
        List<EbmDispatch> ebmDispatchList = dao.getEbmDispatchList(req);
        return Optional.ofNullable(ebmDispatchList).orElse(Collections.emptyList()).stream().filter(Objects::nonNull).map(ebmDispatch -> {
            EbmDispatchInfo ebmDispatchInfo = new EbmDispatchInfo();
            BeanUtils.copyProperties(ebmDispatch, ebmDispatchInfo);
            return ebmDispatchInfo;
        }).collect(Collectors.toList());
    }

    @Override
    public List<EbmDispatchInfo> getEbmDispatchByMatchedEbdId(String matchedEbdId) {
        List<EbmDispatch> ebmDispatchList = dao.getEbmDispatchByMatchedEbdId(matchedEbdId);
        return Optional.ofNullable(ebmDispatchList).orElse(Collections.emptyList()).stream().filter(Objects::nonNull).map(ebmDispatch -> {
            EbmDispatchInfo ebmDispatchInfo = new EbmDispatchInfo();
            BeanUtils.copyProperties(ebmDispatch, ebmDispatchInfo);
            return ebmDispatchInfo;
        }).collect(Collectors.toList());
    }

    @Override
    public List<EbmDispatchAndEbdFileInfo> getEbmDispatchAndEbdFileByEbmId(String ebmId) {
        return dao.getEbmDispatchAndEbdFileByEbmId(ebmId);
    }
}
