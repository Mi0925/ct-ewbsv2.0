package cn.comtom.core.main.statistics.service.impl;

import cn.comtom.core.main.statistics.domain.request.TimeFrameReq;
import cn.comtom.core.main.statistics.domain.response.EmergencyBrdCount;
import cn.comtom.core.main.statistics.domain.response.ResourceDistribution;
import cn.comtom.core.main.statistics.domain.response.EmergencyResOnlineCount;
import cn.comtom.core.main.statistics.service.IEmergencyResourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author wangbingyan
 * @date 2020/1/6
 * @desc 应急资源统计
 */
@Service
@Slf4j
public class EmergencyResourceServiceImpl implements IEmergencyResourceService {

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public List<EmergencyBrdCount> emergencyBrdCount(TimeFrameReq req) {
        ArrayList<EmergencyBrdCount> list = new ArrayList<>();
        if (Objects.nonNull(req.getIsThisYear()) && req.getIsThisYear()) {
            for (int i = 0; i < 10; i++) {
                EmergencyBrdCount emergencyBrdCount = new EmergencyBrdCount();
                emergencyBrdCount.setEmergencyName("应急资源-" + i);
                emergencyBrdCount.setBroadcastCount((int) Math.ceil(Math.random() * 100));
                emergencyBrdCount.setBroadcastDuration(Math.ceil(Math.random() * 200));
                list.add(emergencyBrdCount);
            }
        } else if (Objects.nonNull(req.getIsThisMonth()) && req.getIsThisMonth()) {
            for (int i = 0; i < 10; i++) {
                EmergencyBrdCount emergencyBrdCount = new EmergencyBrdCount();
                emergencyBrdCount.setEmergencyName("应急资源-" + i);
                emergencyBrdCount.setBroadcastCount((int) Math.ceil(Math.random() * 30));
                emergencyBrdCount.setBroadcastDuration(Math.ceil(Math.random() * 60));
                list.add(emergencyBrdCount);
            }
        } else {
            LocalDate startDate = LocalDate.parse(req.getStartDate(), dateTimeFormatter);
            LocalDate endDate = LocalDate.parse(req.getEndDate(), dateTimeFormatter);
            int len = endDate.getDayOfYear() - startDate.getDayOfYear();
            for (int i = 0; i < 10; i++) {
                EmergencyBrdCount emergencyBrdCount = new EmergencyBrdCount();
                emergencyBrdCount.setEmergencyName("应急资源-" + i);
                emergencyBrdCount.setBroadcastCount((int) Math.ceil(Math.random() * len));
                emergencyBrdCount.setBroadcastDuration(Math.ceil(Math.random() * (2 * len)));
                list.add(emergencyBrdCount);
            }
        }
        return list;
    }

    @Override
    public List<EmergencyResOnlineCount> emergencyResOnlineCount(TimeFrameReq req) {
        ArrayList<EmergencyResOnlineCount> list = new ArrayList<>();
        if (Objects.nonNull(req.getIsThisYear()) && req.getIsThisYear()) {
            for (int i = 0; i < 10; i++) {
                EmergencyResOnlineCount onlineCount = new EmergencyResOnlineCount();
                onlineCount.setEmergencyName("应急资源-" + i);
                onlineCount.setOnlineDuration(Math.ceil(Math.random() * 365));
                list.add(onlineCount);
            }
        } else if (Objects.nonNull(req.getIsThisMonth()) && req.getIsThisMonth()) {
            for (int i = 0; i < 10; i++) {
                EmergencyResOnlineCount onlineCount = new EmergencyResOnlineCount();
                onlineCount.setEmergencyName("应急资源-" + i);
                onlineCount.setOnlineDuration(Math.ceil(Math.random() * 30));
                list.add(onlineCount);
            }
        } else {
            LocalDate startDate = LocalDate.parse(req.getStartDate(), dateTimeFormatter);
            LocalDate endDate = LocalDate.parse(req.getEndDate(), dateTimeFormatter);
            int len = endDate.getDayOfYear() - startDate.getDayOfYear();
            for (int i = 0; i < 10; i++) {
                EmergencyResOnlineCount onlineCount = new EmergencyResOnlineCount();
                onlineCount.setEmergencyName("应急资源-" + i);
                onlineCount.setOnlineDuration(Math.ceil(Math.random() * len));
                list.add(onlineCount);
            }
        }
        return list;
    }

    /**
     * 应急资源分布图
     * @return
     */
    @Override
    public List<ResourceDistribution> emergencyResDistribution() {
        ArrayList<ResourceDistribution> resDistributions = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            ResourceDistribution resDistribution = new ResourceDistribution();
            resDistribution.setResName("应急资源-" + i);
            if (i % 4 == 0) {
                resDistribution.setResStatus(2);
            } else if (i % 5 == 0) {
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
