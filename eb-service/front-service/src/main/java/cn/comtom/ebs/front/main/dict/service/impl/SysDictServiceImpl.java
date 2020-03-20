package cn.comtom.ebs.front.main.dict.service.impl;

import cn.comtom.domain.system.dict.info.SysDictInfo;
import cn.comtom.ebs.front.fegin.service.ISystemFeginService;
import cn.comtom.ebs.front.main.dict.service.ISysDictService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class SysDictServiceImpl implements ISysDictService {

    @Autowired
    private ISystemFeginService systemFeginService;

    @Override
    public List<SysDictInfo> getSysDictInfoByGroupCode(String dictGroupId) {
        return systemFeginService.getSysDictInfoByGroupCode(dictGroupId);
    }
}
