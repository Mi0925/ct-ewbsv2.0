package cn.comtom.core.main.access.service.impl;

import cn.comtom.core.fw.BaseDao;
import cn.comtom.core.fw.BaseServiceImpl;
import cn.comtom.core.main.access.dao.AccessRecordDao;
import cn.comtom.core.main.access.entity.dbo.AccessRecord;
import cn.comtom.core.main.access.service.IAccessRecordService;
import cn.comtom.domain.core.access.info.AccessRecordInfo;
import cn.comtom.domain.core.access.request.AccessRecordPageRequest;
import cn.comtom.tools.enums.SysDictCodeEnum;
import cn.comtom.tools.utils.ReflectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AccessRecordServiceImpl extends BaseServiceImpl<AccessRecord,String> implements IAccessRecordService {

    @Autowired
    private AccessRecordDao recordDao;

    @Override
    public BaseDao<AccessRecord, String> getDao() {
        return recordDao;
    }

    @Override
    public List<AccessRecordInfo> list(AccessRecordPageRequest req) {
        Map<String,Object> map = ReflectionUtils.convertBean2Map(req);
        map.put("dictGroupCode",SysDictCodeEnum.ACCESS_RECORD_STATUS.getCode()); //关联数据字典查询
       return recordDao.list(map);
    }
}
