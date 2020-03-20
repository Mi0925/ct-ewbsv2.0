package cn.comtom.quartz.fegin;


import cn.comtom.tools.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "core-service")
public interface CoreFegin {

    @GetMapping(value = "/core/quartz/programDecompose")
    ApiResponse programDecompose();
}
