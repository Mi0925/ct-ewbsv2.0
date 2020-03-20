package cn.comtom.core.main.ebm.service.impl;

import cn.comtom.core.fw.BaseDao;
import cn.comtom.core.fw.BaseServiceImpl;
import cn.comtom.core.main.ebm.dao.EbmDao;
import cn.comtom.core.main.ebm.dao.EbmStateBackDao;
import cn.comtom.core.main.ebm.entity.dbo.Ebm;
import cn.comtom.core.main.ebm.entity.dbo.EbmStateBack;
import cn.comtom.core.main.ebm.service.IEbmService;
import cn.comtom.core.main.ebm.service.IEbmStateBackService;
import cn.comtom.domain.core.ebm.info.EbmInfo;
import cn.comtom.domain.core.ebm.info.EbmStateBackInfo;
import cn.comtom.domain.core.ebm.request.EbmPageRequest;
import cn.comtom.domain.core.ebm.request.EbmStateBackQueryRequest;
import cn.comtom.tools.enums.SysDictCodeEnum;
import cn.comtom.tools.utils.ReflectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EbmStateBackServiceImpl extends BaseServiceImpl<EbmStateBack,String> implements IEbmStateBackService {


    @Autowired
    private EbmStateBackDao dao;


    @Override
    public BaseDao<EbmStateBack, String> getDao() {
        return dao;
    }

    @Override
    public List<EbmStateBackInfo> list(EbmStateBackQueryRequest request) {
        Map<String,Object> map = ReflectionUtils.convertBean2Map(request);
        map.put("dictGroupCode",SysDictCodeEnum.EBD_BROADCAST_STATE.getCode());
        return dao.list(map);
    }
}
