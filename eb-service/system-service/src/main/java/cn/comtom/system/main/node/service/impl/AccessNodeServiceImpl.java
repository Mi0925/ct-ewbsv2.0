package cn.comtom.system.main.node.service.impl;

import cn.comtom.domain.system.accessnode.info.AccessNodeInfo;
import cn.comtom.system.fw.BaseDao;
import cn.comtom.system.fw.BaseServiceImpl;
import cn.comtom.system.main.node.dao.AccessNodeDao;
import cn.comtom.system.main.node.entity.dbo.AccessNode;
import cn.comtom.system.main.node.service.IAccessNodeService;
import cn.comtom.tools.constants.SymbolConstants;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AccessNodeServiceImpl extends BaseServiceImpl<AccessNode,String> implements IAccessNodeService {

    @Autowired
    private AccessNodeDao accessNodeDao;

    @Override
    public BaseDao<AccessNode, String> getDao() {
        return accessNodeDao;
    }

    @Override
    public Boolean isWhite(String ip) {
        AccessNode accessNode = new AccessNode();
      //  accessNode.setIp(ip);
        List<AccessNode> accessNodeList = accessNodeDao.getList(accessNode);
        boolean match =  Optional.ofNullable(accessNodeList).orElse(Collections.emptyList()).stream()
                .anyMatch(node -> IpMatch(node.getIp(),ip));
        return match;
    }

    private boolean IpMatch(String ips, String testIp) {
        String regex = new StringBuilder()
                .append("^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\.")
                .append("(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.")
                .append("(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.")
                .append("(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$")
                .toString();
        if(StringUtils.isBlank(ips) || StringUtils.isBlank(testIp) || !testIp.matches(regex)){
            return  false;
        }
        String[] ipArr = ips.split(SymbolConstants.GENERAL_SEPARATOR);
        List<String> ipList = Arrays.asList(ipArr);
        boolean match1 = Optional.ofNullable(ipList).orElse(Collections.emptyList()).stream()
                .filter(StringUtils::isNotBlank)
               // .filter(ip -> ip.matches(regex))
                .distinct()
                .anyMatch(ip ->{
                    String[] nets= ip.split("\\.");
                    String[] testNets = testIp.split("\\.");
                    if(nets.length != 4 && testNets.length != 4){
                        return false;
                    }
                    boolean match = true;
                    for(int i=0;i < nets.length ;i ++){
                        //如果匹配的不是 * 号，并且验证不通过
                        if(!SymbolConstants.PATTERN_START.equals(nets[i]) && !testNets[i].equals(nets[i])){
                            match =  false;
                            break;
                        }
                    }
                    return match;
                });
        return match1;
    }

    @Override
    public AccessNodeInfo getByPlatformId(String platformId) {
        List<AccessNode> accessNodeList = accessNodeDao.getByPlatformId(platformId);
        if(accessNodeList!=null && !accessNodeList.isEmpty()){
            AccessNode node=accessNodeList.get(0);
            AccessNodeInfo info = new AccessNodeInfo();
            BeanUtils.copyProperties(node,info);
            return info;
        }
        return null;
    }
}
