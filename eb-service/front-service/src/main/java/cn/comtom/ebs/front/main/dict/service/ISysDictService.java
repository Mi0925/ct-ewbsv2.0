package cn.comtom.ebs.front.main.dict.service;

import cn.comtom.domain.system.dict.info.SysDictInfo;

import java.util.List;

public interface ISysDictService {
    List<SysDictInfo> getSysDictInfoByGroupCode(String dictGroupId);
}
