package cn.comtom.system.main.node.service;

import cn.comtom.domain.system.accessnode.info.AccessNodeInfo;
import cn.comtom.system.fw.BaseService;
import cn.comtom.system.main.node.entity.dbo.AccessNode;

public interface IAccessNodeService extends BaseService<AccessNode,String> {
    Boolean isWhite(String ip);

    AccessNodeInfo getByPlatformId(String platformId);
}
