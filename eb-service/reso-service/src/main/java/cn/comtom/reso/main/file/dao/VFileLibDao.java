package cn.comtom.reso.main.file.dao;

import cn.comtom.domain.reso.file.info.VFileLibInfo;
import cn.comtom.domain.reso.file.request.VFileLibPageRequest;
import cn.comtom.reso.fw.BaseDao;
import cn.comtom.reso.fw.ResoMapper;
import cn.comtom.reso.main.file.entity.dbo.VFileLib;
import cn.comtom.reso.main.file.mapper.VFileLibMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class VFileLibDao extends BaseDao<VFileLib,String> {

    @Autowired
    private VFileLibMapper mapper;


    @Override
    public ResoMapper<VFileLib, String> getMapper() {
        return mapper;
    }


    public List<VFileLibInfo> getList(VFileLibPageRequest request) {
        return mapper.getListFromXml(request);
    }
}
