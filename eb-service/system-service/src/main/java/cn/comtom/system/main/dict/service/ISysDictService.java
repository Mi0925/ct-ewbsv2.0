package cn.comtom.system.main.dict.service;


import cn.comtom.domain.system.dict.info.SysDictInfo;
import cn.comtom.system.fw.BaseService;
import cn.comtom.system.main.dict.entity.dbo.SysDict;

import java.util.List;

/**
 * 用户服务接口
 * @author guomao
 * @Date 2018-10-29 9:17
 */
public interface ISysDictService extends BaseService<SysDict,String> {


  List<SysDictInfo> list(SysDictInfo sysDictInfo);
}
