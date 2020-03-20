package cn.comtom.signature.main.fegin;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.comtom.domain.system.sysparam.info.SysParamsInfo;
import cn.comtom.tools.response.ApiEntityResponse;


@FeignClient(value = "system-service")
public interface SystemFegin {

    @RequestMapping(value = "/system/param/getByKey",method = RequestMethod.GET)
    public ApiEntityResponse<SysParamsInfo> getByKey(@RequestParam(name = "paramKey") String paramKey);


}
