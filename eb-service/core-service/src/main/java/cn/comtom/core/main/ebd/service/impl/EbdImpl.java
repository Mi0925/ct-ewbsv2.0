package cn.comtom.core.main.ebd.service.impl;

import cn.comtom.core.fw.BaseDao;
import cn.comtom.core.fw.BaseServiceImpl;
import cn.comtom.core.main.ebd.dao.EbdDao;
import cn.comtom.core.main.ebd.entity.dbo.Ebd;
import cn.comtom.core.main.ebd.service.IEbdService;
import cn.comtom.domain.core.ebd.info.EbdInfo;
import cn.comtom.domain.core.ebd.request.EbdPageRequest;
import cn.comtom.tools.enums.SysDictCodeEnum;
import cn.comtom.tools.utils.ReflectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EbdImpl extends BaseServiceImpl<Ebd,String> implements IEbdService {

    @Autowired
    private EbdDao dao;

    @Override
    public BaseDao<Ebd, String> getDao() {
        return dao;
    }

    @Override
    public List<EbdInfo> getByEbmId(String ebmId) {
        List<Ebd> ebdList = dao.getByEbmId(ebmId);
        return Optional.ofNullable(ebdList).orElse(Collections.emptyList()).stream()
                .filter(Objects::nonNull)
                .map(ebd -> {
                    EbdInfo ebdInfo = new EbdInfo();
                    BeanUtils.copyProperties(ebd,ebdInfo);
                    return ebdInfo;
                }).collect(Collectors.toList());
    }

    @Override
    public List<EbdInfo> list(EbdPageRequest req) {
        Map<String,Object> map = ReflectionUtils.convertBean2Map(req);
        map.put("dictGroupCode",SysDictCodeEnum.EBD_TYPE.getCode()); //关联数据字典查询
        return dao.list(map);
    }
}
