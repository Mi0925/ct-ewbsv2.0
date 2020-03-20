package cn.comtom.system.main.dict.service.impl;

import cn.comtom.domain.system.dict.info.SysDictGroupInfo;
import cn.comtom.system.fw.BaseDao;
import cn.comtom.system.fw.BaseServiceImpl;
import cn.comtom.system.main.dict.dao.SysDictGroupDao;
import cn.comtom.system.main.dict.entity.dbo.SysDictGroup;
import cn.comtom.system.main.dict.service.ISysDictGroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 用户服务类
 * @author guomao
 * @Date 2018-10-29 9:30
 */
@Slf4j
@Service
public class SysDictGroupServiceImpl extends BaseServiceImpl<SysDictGroup,String> implements ISysDictGroupService {

    @Autowired
    private SysDictGroupDao sysDictGroupDao;


    @Override
    public BaseDao<SysDictGroup, String> getDao() {
        return sysDictGroupDao;
    }


    @Override
    public List<SysDictGroupInfo> list(SysDictGroupInfo sysDictGroupInfo) {
        List<SysDictGroup> sysDictGroupList = sysDictGroupDao.list(sysDictGroupInfo);
        return Optional.ofNullable(sysDictGroupList).orElse(Collections.emptyList()).stream()
                .filter(Objects::nonNull)
                .map(sysDictGroup -> {
                    SysDictGroupInfo sysDictGroupInfo1 = new SysDictGroupInfo();
                    BeanUtils.copyProperties(sysDictGroup,sysDictGroupInfo1);
                    return sysDictGroupInfo1;
                })
                .collect(Collectors.toList());
    }
}
