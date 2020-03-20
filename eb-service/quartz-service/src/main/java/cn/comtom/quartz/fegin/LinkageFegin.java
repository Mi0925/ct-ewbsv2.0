package cn.comtom.quartz.fegin;


import cn.comtom.tools.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "linkage-service")
public interface LinkageFegin {

    @GetMapping(value = "/linkage/dispatch")
    ApiResponse dispatch();

    @GetMapping(value = "/linkage/monitor/scanResourceStatus")
    ApiResponse scanResourceState();

    @GetMapping(value = "/linkage/monitor/sendHartBeat")
    ApiResponse sendHartBeat();

    @GetMapping(value = "/linkage/ebr/psInfoReport")
    ApiResponse ebrpsInfoReport();

    @GetMapping(value = "/linkage/ebr/psStateReport")
    ApiResponse ebrpsStateReport();

    @GetMapping(value = "/linkage/ebr/bsInfoReport")
    ApiResponse ebrbsInfoReport();

    @GetMapping(value = "/linkage/ebr/bsStateReport")
    ApiResponse ebrbsStateReport();

    @GetMapping(value = "/linkage/ebr/dtInfoReport")
    ApiResponse ebrdtInfoReport();

    @GetMapping(value = "/linkage/ebr/dtStateReport")
    ApiResponse ebrdtStateReport();

    @GetMapping(value = "/linkage/ebr/brdLogReport")
    ApiResponse brdLogReport();
    
    @GetMapping(value = "/linkage/ebr/stInfoReport")
	ApiResponse ebrstInfoReport();
    
    @GetMapping(value = "/linkage/ebr/asInfoReport")
	ApiResponse ebrasInfoReport();
    
    @GetMapping(value = "/linkage/ebr/asStateReport")
	ApiResponse ebrasStateReport();
}
