package cn.comtom.reso.main.ebr.dao;

import cn.comtom.domain.reso.ebr.info.EbrGisInfo;
import cn.comtom.domain.reso.ebr.info.EbrstatusCount;
import cn.comtom.domain.reso.ebr.request.EbrGisPageRequest;
import cn.comtom.reso.main.ebr.mapper.VEbrGisMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author:WJ
 * @date: 2019/3/7 0007
 * @time: 下午 5:21
 */
@Repository
public class VEbrGisDao {

    @Autowired
    private VEbrGisMapper mapper;

    public List<EbrGisInfo> getEbrGisByPage(EbrGisPageRequest request) {

        return mapper.getEbrGisByPage(request);
    }

    public List<EbrstatusCount> getEbrGisStatusCount(EbrGisPageRequest request) {

        return mapper.getEbrGisStatusCount(request);
    }
}
