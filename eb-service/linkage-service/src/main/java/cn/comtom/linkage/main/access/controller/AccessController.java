package cn.comtom.linkage.main.access.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.comtom.linkage.fw.BaseController;
import cn.comtom.linkage.main.access.service.AccessService;
import cn.comtom.linkage.main.common.service.ICommonService;


/**
 * WJ  信息接入入口
 */
@RestController
@RequestMapping(value = "linkage")
public class AccessController extends BaseController {

    @Autowired
    private AccessService accessService;
    
    @RequestMapping("/access")
    public void access() {
        accessService.service(this.request, this.response);
    }
    

      


}
