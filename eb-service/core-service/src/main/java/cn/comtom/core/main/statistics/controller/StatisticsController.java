package cn.comtom.core.main.statistics.controller;

import cn.comtom.core.main.statistics.domain.request.TimeFrameReq;
import cn.comtom.core.main.statistics.domain.response.*;
import cn.comtom.core.main.statistics.service.IBroadcastStatisticsService;
import cn.comtom.core.main.statistics.service.IEmergencyResourceService;
import cn.comtom.core.main.statistics.service.IWarningSourceService;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiListResponse;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author wangbingyan
 * @date 2020/1/3
 * @desc 统计分析接口
 */
@RestController
@Api(tags = "统计分析", description = "统计分析")
@RequestMapping("/core/countStatistics")
@Slf4j
public class StatisticsController {

    @Autowired
    private IWarningSourceService statisticsService;

    @Autowired
    private IEmergencyResourceService emergencyResourceService;

    @Autowired
    private IBroadcastStatisticsService broadcastStatisticsService;

    @GetMapping("/warningAccessOrder")
    @ApiOperation(value = "1.预警接入排名", notes = "1.预警接入排名")
    public ApiListResponse<WarningAccess> warningAccessOrder(TimeFrameReq req) {
        log.info("统计分析预警接入排名参数：{}", JSON.toJSONString(req));
        List<WarningAccess> list = statisticsService.warningAccessOrder(req);
        return ApiListResponse.ok(list);
    }

    @GetMapping("/embsStatistics")
    @ApiOperation(value = "2.应急广播统计图", notes = "2.应急广播统计图")
    public ApiEntityResponse<GlobalBroadcast> embsStatistics(TimeFrameReq req) {
        GlobalBroadcast globalBroadcast = broadcastStatisticsService.embsStatistics(req);
        return ApiEntityResponse.ok(globalBroadcast);
    }

    @GetMapping("/warningStatistics")
    @ApiOperation(value = "3.预警源广播数量统计图", notes = "3.预警源广播数量统计图")
    public ApiEntityResponse<GlobalBroadcast> warningStatistics(TimeFrameReq req) {
        GlobalBroadcast globalBroadcast = statisticsService.warningStatistics(req);
        return ApiEntityResponse.ok(globalBroadcast);
    }

    @GetMapping("/warningSourceCount")
    @ApiOperation(value = "4.预警源广播类型统计图", notes = "4.预警源广播类型统计图")
    public ApiListResponse<WarningSourceCount> warningSourceCount(TimeFrameReq req) {
        List<WarningSourceCount> warningSourceCounts = statisticsService.warningSourceCount(req);
        return ApiListResponse.ok(warningSourceCounts);
    }

    @GetMapping("/emergencyBrdCount")
    @ApiOperation(value = "6.应急资源广播情况统计图", notes = "6.应急资源广播情况统计图")
    public ApiListResponse<EmergencyBrdCount> emergencyBrdCount(TimeFrameReq req) {
        List<EmergencyBrdCount> emergencyBrdCounts = emergencyResourceService.emergencyBrdCount(req);
        return ApiListResponse.ok(emergencyBrdCounts);
    }

    @GetMapping("/emergencyResOnlineCount")
    @ApiOperation(value = "7.应急资源在线情况统计图", notes = "7.应急资源在线情况统计图")
    public ApiListResponse<EmergencyResOnlineCount> emergencyResOnlineCount(TimeFrameReq req) {
        List<EmergencyResOnlineCount> emergencyResOnlineCounts = emergencyResourceService.emergencyResOnlineCount(req);
        return ApiListResponse.ok(emergencyResOnlineCounts);
    }

    @GetMapping("/broadcastFrequency")
    @ApiOperation(value = "8.广播频次分布图", notes = "8.广播频次分布图")
    public ApiEntityResponse<BroadcastFrequency> broadcastFrequency(TimeFrameReq req) {
        BroadcastFrequency broadcastFrequency = broadcastStatisticsService.broadcastFrequency(req);
        return ApiEntityResponse.ok(broadcastFrequency);
    }

    @GetMapping("/broadcastDuration")
    @ApiOperation(value = "10.广播时长分布图", notes = "10.广播时长分布图")
    public ApiEntityResponse<BroadcastDuration> broadcastDuration(TimeFrameReq req) {
        BroadcastDuration broadcastDuration = broadcastStatisticsService.broadcastDuration(req);
        return ApiEntityResponse.ok(broadcastDuration);
    }

    @GetMapping("/terminalDistribution")
    @ApiOperation(value = "11.终端在线情况分布图", notes = "11.终端在线情况分布图")
    public ApiListResponse<TerminalDistribution> terminalDistribution(TimeFrameReq req) {
        List<TerminalDistribution> terminalDistributions = statisticsService.terminalDistribution(req);
        return ApiListResponse.ok(terminalDistributions);
    }

    @GetMapping("/emergencyResDistributions")
    @ApiOperation(value = "12.应急资源分布图", notes = "12.应急资源分布图")
    public ApiListResponse<ResourceDistribution> emergencyResDistributions() {
        List<ResourceDistribution> resourceDistributions = emergencyResourceService.emergencyResDistribution();
        return ApiListResponse.ok(resourceDistributions);
    }

    @GetMapping("/terminalResources")
    @ApiOperation(value = "13.终端资源分布图", notes = "13.终端资源分布图")
    public ApiListResponse<ResourceDistribution> terminalResources() {
        List<ResourceDistribution> resourceDistributions = statisticsService.terminalResources();
        return ApiListResponse.ok(resourceDistributions);
    }

}
