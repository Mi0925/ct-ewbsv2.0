package cn.comtom.quartz.service.impl;

import cn.comtom.quartz.fegin.CoreFegin;
import cn.comtom.quartz.service.ICoreService;
import cn.comtom.tools.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CoreServiceImpl implements ICoreService {

    @Autowired
    private CoreFegin coreFegin;

    @Override
    public ApiResponse programDecompose() {
        return coreFegin.programDecompose();
    }
}
