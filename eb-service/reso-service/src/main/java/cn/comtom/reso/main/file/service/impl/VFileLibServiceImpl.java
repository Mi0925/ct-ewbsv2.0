package cn.comtom.reso.main.file.service.impl;

import cn.comtom.domain.reso.file.info.VFileLibInfo;
import cn.comtom.domain.reso.file.request.VFileLibPageRequest;
import cn.comtom.reso.fw.BaseDao;
import cn.comtom.reso.fw.BaseServiceImpl;
import cn.comtom.reso.main.file.dao.VFileLibDao;
import cn.comtom.reso.main.file.entity.dbo.VFileLib;
import cn.comtom.reso.main.file.service.IVFileLibService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VFileLibServiceImpl extends BaseServiceImpl<VFileLib,String> implements IVFileLibService {

    @Autowired
    private VFileLibDao dao;

    @Override
    public BaseDao<VFileLib, String> getDao() {
        return dao;
    }


    @Override
    public List<VFileLibInfo> getList(VFileLibPageRequest request) {
        return dao.getList(request);
    }
}
