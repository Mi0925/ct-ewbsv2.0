package cn.comtom.system.main.dict.service;


import cn.comtom.domain.system.dict.info.SysDictGroupInfo;
import cn.comtom.system.fw.BaseService;
import cn.comtom.system.main.dict.entity.dbo.SysDictGroup;

import java.util.List;

/**
 * 用户服务接口
 * @author guomao
 * @Date 2018-10-29 9:17
 */
public interface ISysDictGroupService extends BaseService<SysDictGroup,String> {


  List<SysDictGroupInfo> list(SysDictGroupInfo sysDictGroupInfo);
}
