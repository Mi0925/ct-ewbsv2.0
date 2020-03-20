package cn.comtom.core.main.omd.service.impl;

import cn.comtom.core.fw.BaseDao;
import cn.comtom.core.fw.BaseServiceImpl;
import cn.comtom.core.main.omd.dao.OmdRequestDao;
import cn.comtom.core.main.omd.entity.dbo.OmdRequest;
import cn.comtom.core.main.omd.service.IOmdRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OmdRequestServiceImpl extends BaseServiceImpl<OmdRequest,String> implements IOmdRequestService {

    @Autowired
    private OmdRequestDao dao;

    @Override
    public BaseDao<OmdRequest, String> getDao() {
        return dao;
    }

}
