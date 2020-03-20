package cn.comtom.system.main.dict.service.impl;

import cn.comtom.domain.system.dict.info.SysDictInfo;
import cn.comtom.system.fw.BaseDao;
import cn.comtom.system.fw.BaseServiceImpl;
import cn.comtom.system.main.dict.dao.SysDictDao;
import cn.comtom.system.main.dict.entity.dbo.SysDict;
import cn.comtom.system.main.dict.service.ISysDictService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户服务类
 * @author guomao
 * @Date 2018-10-29 9:30
 */
@Slf4j
@Service
public class SysDictServiceImpl extends BaseServiceImpl<SysDict,String> implements ISysDictService {

    @Autowired
    private SysDictDao sysDictDao;


    @Override
    public BaseDao<SysDict, String> getDao() {
        return sysDictDao;
    }


    @Override
    public List<SysDictInfo> list(SysDictInfo sysDictInfo) {
        List<SysDict> sysDictList = sysDictDao.list(sysDictInfo);
        return Optional.ofNullable(sysDictList).orElse(Collections.emptyList()).stream()
                .filter(Objects::nonNull)
                .map(sysDict -> {
                    SysDictInfo sysDictInfo1 = new SysDictInfo();
                    BeanUtils.copyProperties(sysDict,sysDictInfo1);
                    return sysDictInfo1;
                })
                .sorted(Comparator.comparing(SysDictInfo::getOrderNum))
                .collect(Collectors.toList());
    }
}
