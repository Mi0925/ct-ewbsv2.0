package cn.comtom.core.main.statistics.service.impl;

import cn.comtom.core.main.statistics.domain.request.TimeFrameReq;
import cn.comtom.core.main.statistics.domain.response.*;
import cn.comtom.core.main.statistics.service.IWarningSourceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author wangbingyan
 * @date 2020/1/3
 * @desc 预警源相关统计接口
 */
@Service
@Slf4j
public class WarningSourceServiceImpl implements IWarningSourceService {

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 预警接入排名统计图
     * @param req
     * @return
     */
    @Override
    public List<WarningAccess> warningAccessOrder(TimeFrameReq req) {
        ArrayList<WarningAccess> list = new ArrayList<>();
        if (Objects.nonNull(req.getIsThisYear()) && req.getIsThisYear()) {
            for (int i = 10; i >= 1; i--) {
                WarningAccess warningAccess = new WarningAccess();
                warningAccess.setWarningSourceName("预警源-" + i);
                warningAccess.setWarningNum((i * 10) + 365);
                list.add(warningAccess);
            }
        } else if (Objects.nonNull(req.getIsThisMonth()) && req.getIsThisMonth()) {
            for (int i = 10; i >= 1; i--) {
                WarningAccess warningAccess = new WarningAccess();
                warningAccess.setWarningSourceName("预警源-" + i);
                warningAccess.setWarningNum((i * 3) + 31);
                list.add(warningAccess);
            }
        } else {
            if (StringUtils.isBlank(req.getStartDate()) || StringUtils.isBlank(req.getEndDate())) {
                return null;
            }
            LocalDate startDate = LocalDate.parse(req.getStartDate(), dateTimeFormatter);
            LocalDate endDate = LocalDate.parse(req.getEndDate(), dateTimeFormatter);
            for (int i = 10; i >= 1; i--) {
                WarningAccess warningAccess = new WarningAccess();
                warningAccess.setWarningSourceName("预警源-" + i);
                warningAccess.setWarningNum((i * 3) + 20);
                list.add(warningAccess);
            }
        }
        return list;
    }

    /**
     * 预警源广播数量统计图：（统计预警源的广播）
     * @param req
     * @return
     */
    @Override
    public GlobalBroadcast warningStatistics(TimeFrameReq req) {
        GlobalBroadcast globalBroadcast = new GlobalBroadcast();
        if (Objects.nonNull(req.getIsThisYear()) && req.getIsThisYear()) {
            globalBroadcast.setTotalDegree(365);
            globalBroadcast.setTotalHours(720f);

            GlobalBrdDegree globalBrdDegree = new GlobalBrdDegree();
            globalBrdDegree.setDailyDegree(200);
            globalBrdDegree.setEmergencyDegree(165);
            globalBrdDegree.setOneLevelDegree(20);
            globalBrdDegree.setTwoLevelDegree(40);
            globalBrdDegree.setThreeLevelDegree(45);
            globalBrdDegree.setFourLevelDegree(60);
            globalBroadcast.setGlobalBrdDegree(globalBrdDegree);
        } else if (Objects.nonNull(req.getIsThisMonth()) && req.getIsThisMonth()) {
            globalBroadcast.setTotalDegree(31);
            globalBroadcast.setTotalHours(62f);

            GlobalBrdDegree globalBrdDegree = new GlobalBrdDegree();
            globalBrdDegree.setDailyDegree(20);
            globalBrdDegree.setEmergencyDegree(11);
            globalBrdDegree.setOneLevelDegree(2);
            globalBrdDegree.setTwoLevelDegree(2);
            globalBrdDegree.setThreeLevelDegree(3);
            globalBrdDegree.setFourLevelDegree(4);
            globalBroadcast.setGlobalBrdDegree(globalBrdDegree);
        } else {

        }
        return globalBroadcast;
    }

    /**
     * 预警源广播类型统计图：
     * @param req
     * @return
     */
    @Override
    public List<WarningSourceCount> warningSourceCount(TimeFrameReq req) {
        ArrayList<WarningSourceCount> list = new ArrayList<>();
        if (Objects.nonNull(req.getIsThisYear()) && req.getIsThisYear()) {
            for (int i = 0; i < 10; i++) {
                WarningSourceCount warningSourceCount = new WarningSourceCount();
                warningSourceCount.setWarnigSourceName("预警源-" + i);
                ArrayList<WarningSourceCount.BroadcastTypeCount> counts = new ArrayList<>();
                for (int j = 1; j <= 5; j++) {
                    WarningSourceCount.BroadcastTypeCount typeCount = new WarningSourceCount.BroadcastTypeCount();
                    if (j < 5) {
                        typeCount.setBroadcastType(j);
                    } else {
                        typeCount.setBroadcastType(10);
                    }
                    typeCount.setBroadcastCount((int) Math.ceil(Math.random() * 365));
                    counts.add(typeCount);
                }
                list.add(warningSourceCount);
            }
        } else if (Objects.nonNull(req.getIsThisMonth()) && req.getIsThisMonth()) {
            for (int i = 0; i < 10; i++) {
                WarningSourceCount warningSourceCount = new WarningSourceCount();
                warningSourceCount.setWarnigSourceName("预警源-" + i);
                ArrayList<WarningSourceCount.BroadcastTypeCount> counts = new ArrayList<>();
                for (int j = 1; j <= 5; j++) {
                    WarningSourceCount.BroadcastTypeCount typeCount = new WarningSourceCount.BroadcastTypeCount();
                    if (j < 5) {
                        typeCount.setBroadcastType(j);
                    } else {
                        typeCount.setBroadcastType(10);
                    }
                    typeCount.setBroadcastCount((int) Math.ceil(Math.random() * 30));
                    counts.add(typeCount);
                }
                list.add(warningSourceCount);
            }
        } else {
            if (StringUtils.isBlank(req.getStartDate()) || StringUtils.isBlank(req.getEndDate())) {
                return null;
            }
            LocalDate startDate = LocalDate.parse(req.getStartDate(), dateTimeFormatter);
            LocalDate endDate = LocalDate.parse(req.getEndDate(), dateTimeFormatter);
            int len = endDate.getDayOfYear() - startDate.getDayOfYear();
            for (int i = 0; i < 10; i++) {
                WarningSourceCount warningSourceCount = new WarningSourceCount();
                warningSourceCount.setWarnigSourceName("预警源-" + i);
                ArrayList<WarningSourceCount.BroadcastTypeCount> counts = new ArrayList<>();
                for (int j = 1; j <= 5; j++) {
                    WarningSourceCount.BroadcastTypeCount typeCount = new WarningSourceCount.BroadcastTypeCount();
                    if (j < 5) {
                        typeCount.setBroadcastType(j);
                    } else {
                        typeCount.setBroadcastType(10);
                    }
                    typeCount.setBroadcastCount((int) Math.ceil(Math.random() * len));
                    counts.add(typeCount);
                }
                list.add(warningSourceCount);
            }
        }
        return null;
    }

    /**
     * 终端在线情况分布图
     * @param req
     * @return
     */
    @Override
    public List<TerminalDistribution> terminalDistribution(TimeFrameReq req) {
        ArrayList<TerminalDistribution> terminalDistributions = new ArrayList<>();
        if (Objects.nonNull(req.getIsThisYear()) && req.getIsThisYear()) {
            for (int i = 1; i <= 12; i++) {
                TerminalDistribution terminalDistribution = new TerminalDistribution();
                terminalDistribution.setDate(i + "月");
                terminalDistribution.setTotalCount((int) (2000 + Math.ceil(Math.random() * 100)));
                terminalDistribution.setOnlineCount((int) (terminalDistribution.getTotalCount() - Math.ceil(Math.random() * 30)));
                terminalDistributions.add(terminalDistribution);
            }
        } else if (Objects.nonNull(req.getIsThisMonth()) && req.getIsThisMonth()) {
            for (int i = 1; i <= 31; i++) {
                TerminalDistribution terminalDistribution = new TerminalDistribution();
                terminalDistribution.setDate(i + "日");
                terminalDistribution.setTotalCount((int) (2000 + Math.ceil(Math.random() * 100)));
                terminalDistribution.setOnlineCount((int) (terminalDistribution.getTotalCount() - Math.ceil(Math.random() * 30)));
                terminalDistributions.add(terminalDistribution);
            }
        } else {
            LocalDate startDate = LocalDate.parse(req.getStartDate(), dateTimeFormatter);
            LocalDate endDate = LocalDate.parse(req.getEndDate(), dateTimeFormatter);
            int len = endDate.getDayOfYear() - startDate.getDayOfYear();
            for (int i = 1; i <= len; i++) {
                TerminalDistribution terminalDistribution = new TerminalDistribution();
                terminalDistribution.setDate(startDate.toString());
                terminalDistribution.setTotalCount((int) (2000 + Math.ceil(Math.random() * 100)));
                terminalDistribution.setOnlineCount((int) (terminalDistribution.getTotalCount() - Math.ceil(Math.random() * 30)));
                terminalDistributions.add(terminalDistribution);
                startDate.plusDays(1);
            }
        }
        return terminalDistributions;
    }

    /**
     * 终端资源分布图
     * @return
     */
    @Override
    public List<ResourceDistribution> terminalResources() {
        ArrayList<ResourceDistribution> resDistributions = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            ResourceDistribution resDistribution = new ResourceDistribution();
            resDistribution.setResName("终端-" + i);
            if (i % 50 == 0) {
                resDistribution.setResStatus(2);
            } else if (i % 60 == 0) {
                resDistribution.setResStatus(3);
            } else {
                resDistribution.setResStatus(1);
            }
            resDistribution.setLongitude((float) (116.5690f + Math.random()));
            resDistribution.setLatitude((float) (31.2222f + Math.random()));
        }
        return resDistributions;
    }
}
