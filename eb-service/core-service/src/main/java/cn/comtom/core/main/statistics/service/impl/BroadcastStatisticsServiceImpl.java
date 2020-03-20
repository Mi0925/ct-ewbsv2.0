package cn.comtom.core.main.statistics.service.impl;

import cn.comtom.core.main.statistics.domain.request.TimeFrameReq;
import cn.comtom.core.main.statistics.domain.response.*;
import cn.comtom.core.main.statistics.service.IBroadcastStatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

/**
 * @author wangbingyan
 * @date 2020/1/8
 * @desc 广播统计相关接口
 */
@Service
@Slf4j
public class BroadcastStatisticsServiceImpl implements IBroadcastStatisticsService {

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 应急广播统计图
     * @param req
     * @return
     */
    @Override
    public GlobalBroadcast embsStatistics(TimeFrameReq req) {
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

            GlobalBrdDuration globalBrdDuration = new GlobalBrdDuration();
            globalBrdDuration.setDailyDuration(394.52f);
            globalBrdDuration.setEmergencyDuration(325.48f);
            globalBrdDuration.setOneLevelDuration(39.45f);
            globalBrdDuration.setTwoLevelDuration(78.90f);
            globalBrdDuration.setThreeLevelDuration(88.77f);
            globalBrdDuration.setFourLevelDuration(325.48f - 39.45f - 78.90f - 88.77f);
            globalBroadcast.setGlobalBrdDuration(globalBrdDuration);
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

            GlobalBrdDuration globalBrdDuration = new GlobalBrdDuration();
            globalBrdDuration.setDailyDuration(40f);
            globalBrdDuration.setEmergencyDuration(22f);
            globalBrdDuration.setOneLevelDuration(4f);
            globalBrdDuration.setTwoLevelDuration(4f);
            globalBrdDuration.setThreeLevelDuration(6f);
            globalBrdDuration.setFourLevelDuration(8f);
            globalBroadcast.setGlobalBrdDuration(globalBrdDuration);
        } else {
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

            GlobalBrdDuration globalBrdDuration = new GlobalBrdDuration();
            globalBrdDuration.setDailyDuration(40f);
            globalBrdDuration.setEmergencyDuration(22f);
            globalBrdDuration.setOneLevelDuration(4f);
            globalBrdDuration.setTwoLevelDuration(4f);
            globalBrdDuration.setThreeLevelDuration(6f);
            globalBrdDuration.setFourLevelDuration(8f);
            globalBroadcast.setGlobalBrdDuration(globalBrdDuration);
        }
        return globalBroadcast;
    }

    /**
     * 广播数量频次图
     * @param req
     * @return
     */
    @Override
    public BroadcastFrequency broadcastFrequency(TimeFrameReq req) {
        BroadcastFrequency broadcastFrequency = new BroadcastFrequency();
        if (Objects.nonNull(req.getIsThisYear()) && req.getIsThisYear()) {
            Integer dailyCount = 0;
            Integer emergencyCount = 0;
            ArrayList<BroadcastTypeFrequency> typeFrequencys = new ArrayList<>();
            ArrayList<BroadcastLevelFrequency> levelFrequencies = new ArrayList<>();
            for (int i = 1; i <= 12; i++) {
                BroadcastTypeFrequency typeFrequency = new BroadcastTypeFrequency();
                typeFrequency.setBroadcastDate(i + "月");
                typeFrequency.setDailyBroadcast((int) Math.ceil(Math.random() * 200));
                dailyCount += typeFrequency.getDailyBroadcast();
                typeFrequency.setEmergencyBroadcast((int) Math.ceil(Math.random() * 100));
                emergencyCount += typeFrequency.getEmergencyBroadcast();
                typeFrequency.setTotalBroadcast(typeFrequency.getDailyBroadcast() + typeFrequency.getEmergencyBroadcast());
                typeFrequencys.add(typeFrequency);

                BroadcastLevelFrequency levelFrequency = new BroadcastLevelFrequency();
                levelFrequency.setBroadcastDate(i + "月");
                levelFrequency.setOneLevelCount((int) Math.ceil(typeFrequency.getEmergencyBroadcast() * 0.1));
                levelFrequency.setTwoLevelCount((int) Math.ceil(typeFrequency.getEmergencyBroadcast() * 0.2));
                levelFrequency.setThreeLevelCount((int) Math.ceil(typeFrequency.getEmergencyBroadcast() * 0.3));
                levelFrequency.setFourLevelCount((int) Math.ceil(typeFrequency.getEmergencyBroadcast() * 0.4));
                levelFrequencies.add(levelFrequency);
            }
            broadcastFrequency.setDailyBroadcast(dailyCount);
            broadcastFrequency.setEmergencyBroadcast(emergencyCount);
            broadcastFrequency.setTotalBroadcast(dailyCount + emergencyCount);
            broadcastFrequency.setTypeFrequencies(typeFrequencys);
            broadcastFrequency.setLevelFrequencies(levelFrequencies);
        } else if (Objects.nonNull(req.getIsThisMonth()) && req.getIsThisMonth()) {
            Integer dailyCount = 0;
            Integer emergencyCount = 0;
            ArrayList<BroadcastTypeFrequency> typeFrequencys = new ArrayList<>();
            ArrayList<BroadcastLevelFrequency> levelFrequencies = new ArrayList<>();
            for (int i = 1; i <= 31; i++) {
                BroadcastTypeFrequency typeFrequency = new BroadcastTypeFrequency();
                typeFrequency.setBroadcastDate(i + "日");
                typeFrequency.setDailyBroadcast((int) Math.ceil(Math.random() * 20));
                dailyCount += typeFrequency.getDailyBroadcast();
                typeFrequency.setEmergencyBroadcast((int) Math.ceil(Math.random() * 10));
                emergencyCount += typeFrequency.getEmergencyBroadcast();
                typeFrequency.setTotalBroadcast(typeFrequency.getDailyBroadcast() + typeFrequency.getEmergencyBroadcast());
                typeFrequencys.add(typeFrequency);

                BroadcastLevelFrequency levelFrequency = new BroadcastLevelFrequency();
                levelFrequency.setBroadcastDate(i + "日");
                levelFrequency.setOneLevelCount((int) Math.ceil(typeFrequency.getEmergencyBroadcast() * 0.1));
                levelFrequency.setTwoLevelCount((int) Math.ceil(typeFrequency.getEmergencyBroadcast() * 0.2));
                levelFrequency.setThreeLevelCount((int) Math.ceil(typeFrequency.getEmergencyBroadcast() * 0.3));
                levelFrequency.setFourLevelCount((int) Math.ceil(typeFrequency.getEmergencyBroadcast() * 0.4));
                levelFrequencies.add(levelFrequency);
            }
            broadcastFrequency.setDailyBroadcast(dailyCount);
            broadcastFrequency.setEmergencyBroadcast(emergencyCount);
            broadcastFrequency.setTotalBroadcast(dailyCount + emergencyCount);
            broadcastFrequency.setTypeFrequencies(typeFrequencys);
            broadcastFrequency.setLevelFrequencies(levelFrequencies);
        } else {
            LocalDate startDate = LocalDate.parse(req.getStartDate(), dateTimeFormatter);
            LocalDate endDate = LocalDate.parse(req.getEndDate(), dateTimeFormatter);
            int len = endDate.getDayOfYear() - startDate.getDayOfYear();
            Integer dailyCount = 0;
            Integer emergencyCount = 0;
            ArrayList<BroadcastTypeFrequency> typeFrequencys = new ArrayList<>();
            ArrayList<BroadcastLevelFrequency> levelFrequencies = new ArrayList<>();
            for (int i = 1; i <= len; i++) {
                BroadcastTypeFrequency typeFrequency = new BroadcastTypeFrequency();
                typeFrequency.setBroadcastDate(startDate.toString());
                typeFrequency.setDailyBroadcast((int) Math.ceil(Math.random() * 20));
                dailyCount += typeFrequency.getDailyBroadcast();
                typeFrequency.setEmergencyBroadcast((int) Math.ceil(Math.random() * 10));
                emergencyCount += typeFrequency.getEmergencyBroadcast();
                typeFrequency.setTotalBroadcast(typeFrequency.getDailyBroadcast() + typeFrequency.getEmergencyBroadcast());
                typeFrequencys.add(typeFrequency);

                BroadcastLevelFrequency levelFrequency = new BroadcastLevelFrequency();
                levelFrequency.setBroadcastDate(startDate.toString());
                levelFrequency.setOneLevelCount((int) Math.ceil(typeFrequency.getEmergencyBroadcast() * 0.1));
                levelFrequency.setTwoLevelCount((int) Math.ceil(typeFrequency.getEmergencyBroadcast() * 0.2));
                levelFrequency.setThreeLevelCount((int) Math.ceil(typeFrequency.getEmergencyBroadcast() * 0.3));
                levelFrequency.setFourLevelCount((int) Math.ceil(typeFrequency.getEmergencyBroadcast() * 0.4));
                levelFrequencies.add(levelFrequency);
                startDate.plusDays(1);
            }
            broadcastFrequency.setDailyBroadcast(dailyCount);
            broadcastFrequency.setEmergencyBroadcast(emergencyCount);
            broadcastFrequency.setTotalBroadcast(dailyCount + emergencyCount);
            broadcastFrequency.setTypeFrequencies(typeFrequencys);
            broadcastFrequency.setLevelFrequencies(levelFrequencies);
        }
        return broadcastFrequency;
    }

    /**
     * 广播时长频次图
     * @param req
     * @return
     */
    @Override
    public BroadcastDuration broadcastDuration(TimeFrameReq req) {
        BroadcastDuration broadcastDuration = new BroadcastDuration();
        if (Objects.nonNull(req.getIsThisYear()) && req.getIsThisYear()) {
            Float dailyCount = 0f;
            Float emergencyCount = 0f;
            ArrayList<BroadcastTypeDuration> typeDurations = new ArrayList<>();
            ArrayList<BroadcastLevelDuration> levelDurations = new ArrayList<>();
            for (int i = 1; i <= 12; i++) {
                BroadcastTypeDuration typeDuration = new BroadcastTypeDuration();
                typeDuration.setBroadcastDate(i + "月");
                typeDuration.setDailyDuration((float) (Math.random() * 3000));
                dailyCount += typeDuration.getDailyDuration();
                typeDuration.setEmergencyDuration((float) (Math.random() * 2000));
                emergencyCount += typeDuration.getEmergencyDuration();
                typeDuration.setTotalDuration(typeDuration.getDailyDuration() + typeDuration.getEmergencyDuration());
                typeDurations.add(typeDuration);

                BroadcastLevelDuration levelDuration = new BroadcastLevelDuration();
                levelDuration.setBroadcastDate(i + "月");
                levelDuration.setOneLevelDuration(typeDuration.getEmergencyDuration() * 0.1f);
                levelDuration.setTwoLevelDuration(typeDuration.getEmergencyDuration() * 0.2f);
                levelDuration.setThreeLevelDuration(typeDuration.getEmergencyDuration() * 0.3f);
                levelDuration.setFourLevelDuration(typeDuration.getEmergencyDuration() * 0.4f);
                levelDurations.add(levelDuration);
            }
            broadcastDuration.setDailyDuration(dailyCount);
            broadcastDuration.setEmergencyDuration(emergencyCount);
            broadcastDuration.setTotalDuration(dailyCount + emergencyCount);
            broadcastDuration.setTypeDurations(typeDurations);
            broadcastDuration.setLevelDurations(levelDurations);
        } else if (Objects.nonNull(req.getIsThisMonth()) && req.getIsThisMonth()) {
            Float dailyCount = 0f;
            Float emergencyCount = 0f;
            ArrayList<BroadcastTypeDuration> typeDurations = new ArrayList<>();
            ArrayList<BroadcastLevelDuration> levelDurations = new ArrayList<>();
            for (int i = 1; i <= 31; i++) {
                BroadcastTypeDuration typeDuration = new BroadcastTypeDuration();
                typeDuration.setBroadcastDate(i + "日");
                typeDuration.setDailyDuration((float) (Math.random() * 600));
                dailyCount += typeDuration.getDailyDuration();
                typeDuration.setEmergencyDuration((float) (Math.random() * 200));
                emergencyCount += typeDuration.getEmergencyDuration();
                typeDuration.setTotalDuration(typeDuration.getDailyDuration() + typeDuration.getEmergencyDuration());
                typeDurations.add(typeDuration);

                BroadcastLevelDuration levelDuration = new BroadcastLevelDuration();
                levelDuration.setBroadcastDate(i + "日");
                levelDuration.setOneLevelDuration(typeDuration.getEmergencyDuration() * 0.1f);
                levelDuration.setTwoLevelDuration(typeDuration.getEmergencyDuration() * 0.2f);
                levelDuration.setThreeLevelDuration(typeDuration.getEmergencyDuration() * 0.3f);
                levelDuration.setFourLevelDuration(typeDuration.getEmergencyDuration() * 0.4f);
                levelDurations.add(levelDuration);
            }
            broadcastDuration.setDailyDuration(dailyCount);
            broadcastDuration.setEmergencyDuration(emergencyCount);
            broadcastDuration.setTotalDuration(dailyCount + emergencyCount);
            broadcastDuration.setTypeDurations(typeDurations);
            broadcastDuration.setLevelDurations(levelDurations);
        } else {
            LocalDate startDate = LocalDate.parse(req.getStartDate(), dateTimeFormatter);
            LocalDate endDate = LocalDate.parse(req.getEndDate(), dateTimeFormatter);
            int len = endDate.getDayOfYear() - startDate.getDayOfYear();
            Float dailyCount = 0f;
            Float emergencyCount = 0f;
            ArrayList<BroadcastTypeDuration> typeDurations = new ArrayList<>();
            ArrayList<BroadcastLevelDuration> levelDurations = new ArrayList<>();
            for (int i = 1; i <= len; i++) {
                BroadcastTypeDuration typeDuration = new BroadcastTypeDuration();
                typeDuration.setBroadcastDate(startDate.toString());
                typeDuration.setDailyDuration((float) (Math.random() * 600));
                dailyCount += typeDuration.getDailyDuration();
                typeDuration.setEmergencyDuration((float) (Math.random() * 200));
                emergencyCount += typeDuration.getEmergencyDuration();
                typeDuration.setTotalDuration(typeDuration.getDailyDuration() + typeDuration.getEmergencyDuration());
                typeDurations.add(typeDuration);

                BroadcastLevelDuration levelDuration = new BroadcastLevelDuration();
                levelDuration.setBroadcastDate(startDate.toString());
                levelDuration.setOneLevelDuration(typeDuration.getEmergencyDuration() * 0.1f);
                levelDuration.setTwoLevelDuration(typeDuration.getEmergencyDuration() * 0.2f);
                levelDuration.setThreeLevelDuration(typeDuration.getEmergencyDuration() * 0.3f);
                levelDuration.setFourLevelDuration(typeDuration.getEmergencyDuration() * 0.4f);
                levelDurations.add(levelDuration);
                startDate.plusDays(1);
            }
            broadcastDuration.setDailyDuration(dailyCount);
            broadcastDuration.setEmergencyDuration(emergencyCount);
            broadcastDuration.setTotalDuration(dailyCount + emergencyCount);
            broadcastDuration.setTypeDurations(typeDurations);
            broadcastDuration.setLevelDurations(levelDurations);
        }
        return broadcastDuration;
    }
}
