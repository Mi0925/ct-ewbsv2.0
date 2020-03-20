package cn.comtom.reso.main.ebr.service.impl;

import cn.comtom.domain.reso.ebr.info.EbrIdCreatorInfo;
import cn.comtom.reso.fw.BaseDao;
import cn.comtom.reso.fw.BaseServiceImpl;
import cn.comtom.reso.main.ebr.dao.EbrIdCreatorDao;
import cn.comtom.reso.main.ebr.entity.dbo.EbrIdCreator;
import cn.comtom.reso.main.ebr.service.IEbrIdCreatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EbrIdCreatorServiceImpl extends BaseServiceImpl<EbrIdCreator, String> implements IEbrIdCreatorService {

    @Autowired
    private EbrIdCreatorDao dao;

    @Override
    public BaseDao<EbrIdCreator, String> getDao() {
        return dao;
    }

    @Override
    public String getSourceTypeCodeSN(EbrIdCreatorInfo creatorInfo) {
        List<EbrIdCreator> creatorList = dao.getSourceTypeCodeSNByCond(creatorInfo);
        return Optional.ofNullable(creatorList).orElse(Collections.emptyList()).stream()
                .filter(Objects::nonNull)
                .max(Comparator.comparing(ebrIdCreator -> Integer.valueOf(ebrIdCreator.getSourceTypeSN())))
                .map(ebrIdCreator -> {
                    Integer sn = Integer.valueOf(ebrIdCreator.getSourceTypeSN()) + 1;
                    if (sn > 9) {
                        return sn.toString();
                    } else {
                        return "0" + sn;
                    }
                })
                .orElse("01");

    }


    @Override
    public EbrIdCreator getByCondition(EbrIdCreatorInfo creatorInfo) {
        List<EbrIdCreator> creatorList = dao.getByCondition(creatorInfo);
        return Optional.ofNullable(creatorList).orElse(Collections.emptyList()).stream()
                .filter(Objects::nonNull)
                .max(Comparator.comparing(ebrIdCreator -> Integer.valueOf(ebrIdCreator.getSourceSubTypeSN())))
                .orElse(null);
    }
}
