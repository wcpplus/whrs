package org.farm2.attendance.service;

import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.farm2.attendance.dao.AttendanceSummaryDao;
import org.farm2.attendance.domain.AttendanceSummary;
import org.farm2.auth.face.FarmParameter;
import org.farm2.tools.db.commons.DBRule;
import org.farm2.tools.db.commons.DBRuleList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntConsumer;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AttendanceAsyncService {
    @Autowired
    private FarmParameter farmParameter;

    @Autowired
    private AttendanceSummaryDao attendanceSummaryDao;

    @Async
    public void runAllSummary() {
        try {
            // 你的考勤计算逻辑
            List<AttendanceSummary> summarys = attendanceSummaryDao.find(DBRuleList.getInstance()
                    .add(new DBRule("STATE", "0", "="))
                    .toList());
            for (AttendanceSummary summary : summarys) {
                runSummary(summary);
            }
        } catch (Exception e) {
            // 日志记录
            log.error(e.getMessage(), e);
        }
    }

    public void runSummary(AttendanceSummary summary) {
        // 获取配置
        String ssConfig = farmParameter.getStringParameter("farm2.config.attendance.s.s.time"); // 上午上班
        String sxConfig = farmParameter.getStringParameter("farm2.config.attendance.s.x.time"); // 上午下班
        String xsConfig = farmParameter.getStringParameter("farm2.config.attendance.x.s.time"); // 下午上班
        String xxConfig = farmParameter.getStringParameter("farm2.config.attendance.x.x.time"); // 下午下班

        StringBuilder exceptionTypes = new StringBuilder();
        AtomicInteger totalLateMinutes = new AtomicInteger();
        AtomicInteger totalEarlyMinutes = new AtomicInteger();
        AtomicBoolean hasAnyValidPunch = new AtomicBoolean(false); // 是否有任何一次有效打卡（用于 absentis）

        // 辅助方法：处理一个“上班”类型时段（需最早打卡，可能迟到或缺勤）
        processPunchTime(summary.getSstime(), ssConfig, true, exceptionTypes, late -> {
            totalLateMinutes.addAndGet(late);
            if (late >= 0) hasAnyValidPunch.set(true);
        });

        // 处理上午下班（最晚打卡，可能早退或缺勤）
        processPunchTime(summary.getSxtime(), sxConfig, false, exceptionTypes, early -> {
            totalEarlyMinutes.addAndGet(early);
            if (early >= 0) hasAnyValidPunch.set(true);
        });

        // 处理下午上班
        processPunchTime(summary.getXstime(), xsConfig, true, exceptionTypes, late -> {
            totalLateMinutes.addAndGet(late);
            if (late >= 0) hasAnyValidPunch.set(true);
        });

        // 处理下午下班
        processPunchTime(summary.getXxtime(), xxConfig, false, exceptionTypes, early -> {
            totalEarlyMinutes.addAndGet(early);
            if (early >= 0) hasAnyValidPunch.set(true);
        });

        // 设置异常类型（去重并排序，可选）
        String exceptionTypeStr = exceptionTypes.toString();
        if (!exceptionTypeStr.isEmpty()) {
            // 可选：去重（如 "112" → "12"）
            exceptionTypeStr = exceptionTypeStr.chars()
                    .distinct()
                    .mapToObj(c -> String.valueOf((char) c))
                    .sorted()
                    .collect(Collectors.joining());
        }

        summary.setExceptiontype(exceptionTypeStr);
        if (StringUtils.isBlank(summary.getExceptiontype().trim())) {
            summary.setExceptiontype("0");
        }
        summary.setLatem(totalLateMinutes.get());
        summary.setEarlym(totalEarlyMinutes.get());
        summary.setAbsentis(hasAnyValidPunch.get() ? "1" : "0");
        summary.setState("1"); // 已计算
        attendanceSummaryDao.update(summary);
    }

    /**
     * 处理单个打卡时段
     *
     * @param punchTime14    打卡时间（14位，如 "20260106080500"），可能为 null 或 "0"
     * @param configTime4    配置时间（4位，如 "0800"），"0" 表示不启用
     * @param isArrival      true=上班（检查迟到），false=下班（检查早退）
     * @param exceptions     异常类型收集器（追加 '1','2','3'）
     * @param minuteConsumer 消费迟到/早退分钟数（>=0 表示有效打卡）
     */
    private void processPunchTime(
            String punchTime14,
            String configTime4,
            boolean isArrival,
            StringBuilder exceptions,
            IntConsumer minuteConsumer) {

        // 如果配置为 "0"，跳过
        if ("0".equals(configTime4)) {
            return;
        }

        // 未打卡：punchTime14 为 null、空、或 "0"
        if (StringUtils.isBlank(punchTime14) || "0".equals(punchTime14)) {
            exceptions.append("3"); // 缺勤
            minuteConsumer.accept(-1); // 无效打卡
            return;
        }

        if (punchTime14.length() < 14) {
            exceptions.append("3");
            minuteConsumer.accept(-1);
            return;
        }

        try {
            // 提取 HHmm
            String punchHHmm = punchTime14.substring(8, 12);
            int punchMinutes = timeToMinutes(punchHHmm);
            int configMinutes = timeToMinutes(configTime4);

            if (isArrival) {
                // 上班：打卡时间 > 配置时间 → 迟到
                if (punchMinutes > configMinutes) {
                    int lateMinutes = punchMinutes - configMinutes;
                    exceptions.append("1"); // 迟到
                    minuteConsumer.accept(lateMinutes);
                } else {
                    // 正常
                    minuteConsumer.accept(0);
                }
            } else {
                // 下班：打卡时间 < 配置时间 → 早退
                if (punchMinutes < configMinutes) {
                    int earlyMinutes = configMinutes - punchMinutes;
                    exceptions.append("2"); // 早退
                    minuteConsumer.accept(earlyMinutes);
                } else {
                    // 正常
                    minuteConsumer.accept(0);
                }
            }
        } catch (Exception e) {
            // 时间格式错误，视为缺勤
            exceptions.append("3");
            minuteConsumer.accept(-1);
        }
    }

    private int timeToMinutes(String hhmm) {
        if (hhmm == null || hhmm.length() != 4) {
            throw new IllegalArgumentException("Invalid time format: " + hhmm);
        }
        int hours = Integer.parseInt(hhmm.substring(0, 2));
        int minutes = Integer.parseInt(hhmm.substring(2, 4));
        return hours * 60 + minutes;
    }
}