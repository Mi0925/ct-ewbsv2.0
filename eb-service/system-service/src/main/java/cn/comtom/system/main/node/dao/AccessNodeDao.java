package cn.comtom.system.main.node.dao;

import cn.comtom.system.fw.BaseDao;
import cn.comtom.system.fw.SystemMapper;
import cn.comtom.system.main.node.entity.dbo.AccessNode;
import cn.comtom.system.main.node.mapper.AccessNodeMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.util.List;

@Repository
public class AccessNodeDao extends BaseDao<AccessNode,String> {

    @Autowired
    private AccessNodeMapper mapper;

    @Override
    public SystemMapper<AccessNode, String> getMapper() {
        return mapper;
    }


    public List<AccessNode> getList(AccessNode accessNode) {
        Weekend<AccessNode> weekend = Weekend.of(AccessNode.class);
        WeekendCriteria<AccessNode, Object> weekendCriteria = weekend.weekendCriteria();
        if(StringUtils.isNotBlank(accessNode.getIp())){
            weekendCriteria.andLike(AccessNode::getIp,"%".concat(accessNode.getIp()+",").concat("%"));
        }
        if(StringUtils.isNotBlank(accessNode.getPlatformId())){
            weekendCriteria.andEqualTo(AccessNode::getPlatformId,accessNode.getPlatformId());
        }
        if(StringUtils.isNotBlank(accessNode.getStatus())){
            weekendCriteria.andEqualTo(AccessNode::getStatus,accessNode.getStatus());
        }
        return mapper.selectByExample(weekend);
    }

    public List<AccessNode> getByPlatformId(String platformId) {
        Weekend<AccessNode> weekend = Weekend.of(AccessNode.class);
        WeekendCriteria<AccessNode, Object> weekendCriteria = weekend.weekendCriteria();
        if(StringUtils.isNotBlank(platformId)){
            weekendCriteria.andEqualTo(AccessNode::getPlatformId,platformId);
        }
        return mapper.selectByExample(weekend);
    }
}
