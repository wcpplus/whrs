package org.farm2.attendance.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.farm2.attendance.dao.AttendanceDateDto;
import org.farm2.attendance.dto.AttendanceSummaryDto;
import org.farm2.auth.face.FarmParameter;
import org.farm2.base.db.FarmDbFields;
import org.farm2.base.domain.FarmUserContextLoader;
import org.farm2.base.exception.FarmExceptionUtils;
import org.farm2.attendance.dao.AttendanceSummaryDao;
import org.farm2.attendance.domain.AttendanceSummary;
import org.farm2.attendance.service.AttendanceSummaryServiceInter;
import org.farm2.tools.bean.FarmBeanUtils;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;
import org.farm2.tools.db.commons.DBRule;
import org.farm2.tools.db.commons.DBRuleList;
import org.farm2.tools.i18n.I18n;
import org.farm2.tools.time.FarmTimeTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 考勤结果
 *
 * @author cbtg自动生成  2026-1-6 15:34:44
 */
@Service
@Slf4j
public class AttendanceSummaryServiceImpl implements AttendanceSummaryServiceInter {
    @Autowired
    private FarmParameter farmParameter;

    @Autowired
    private AttendanceSummaryDao attendanceSummaryDao;

    @Transactional
    @Override
    public AttendanceSummary insertAttendanceSummaryEntity(AttendanceSummary attendanceSummary) {
        FarmDbFields.initInsertBean(attendanceSummary, FarmUserContextLoader.getCurrentUser());
        //FarmBeanUtils.runFunctionByBlank(attendanceSummary.getType(), "1", attendanceSummary::setType);
        attendanceSummaryDao.insert(attendanceSummary);
        //[tree：树形结构使用]
        //initTreeCode(actions.getId());
        return attendanceSummary;
    }

    @Transactional
    @Override
    public AttendanceSummary editAttendanceSummaryEntity(AttendanceSummary attendanceSummary) {
        AttendanceSummary saveAttendanceSummary = getAttendanceSummaryById(attendanceSummary.getId());
        FarmExceptionUtils.throwNullEx(saveAttendanceSummary, I18n.msg("考勤结果不存在:?", attendanceSummary.getId()));
        saveAttendanceSummary.setId(attendanceSummary.getId());
        saveAttendanceSummary.setCtime(attendanceSummary.getCtime());
        saveAttendanceSummary.setUserkey(attendanceSummary.getUserkey());
        saveAttendanceSummary.setAttendancetime(attendanceSummary.getAttendancetime());
        saveAttendanceSummary.setWorkhours(attendanceSummary.getWorkhours());
        saveAttendanceSummary.setLatem(attendanceSummary.getLatem());
        saveAttendanceSummary.setEarlym(attendanceSummary.getEarlym());
        saveAttendanceSummary.setOvertimem(attendanceSummary.getOvertimem());
        saveAttendanceSummary.setAbsentis(attendanceSummary.getAbsentis());
        saveAttendanceSummary.setExceptiontype(attendanceSummary.getExceptiontype());
        saveAttendanceSummary.setState(attendanceSummary.getState());
        saveAttendanceSummary.setExemptnote(attendanceSummary.getExemptnote());
        saveAttendanceSummary.setWorking(attendanceSummary.getWorking());
        saveAttendanceSummary.setBackup(attendanceSummary.getBackup());
        saveAttendanceSummary.setSstime(attendanceSummary.getSstime());
        saveAttendanceSummary.setSxtime(attendanceSummary.getSxtime());
        saveAttendanceSummary.setXstime(attendanceSummary.getXstime());
        saveAttendanceSummary.setXxtime(attendanceSummary.getXxtime());

        FarmDbFields.initUpdateBean(saveAttendanceSummary, FarmUserContextLoader.getCurrentUser());
        attendanceSummaryDao.update(saveAttendanceSummary);
        return saveAttendanceSummary;
    }

    @Transactional
    @Override
    public AttendanceSummary getAttendanceSummaryById(String id) {
        return attendanceSummaryDao.findById(id);
    }

    @Override
    public List<AttendanceSummary> getAttendanceSummarys(DataQuery query) {
        return attendanceSummaryDao.queryData(query.setCount(false)).getData(AttendanceSummary.class);
    }


    @Transactional
    @Override
    public DataResult searchAttendanceSummary(DataQuery query) {
        DataResult result = attendanceSummaryDao.queryData(query);
        return result;
    }

    @Override
    public int getAttendanceSummaryNum(DataQuery query) {
        return attendanceSummaryDao.countData(query);
    }


    @Transactional
    @Override
    public void delAttendanceSummary(String id) {
        /*[tree：树形结构使用]
        if ( attendanceSummaryDao.findByParentId(id).size() > 0) {
            throw new RuntimeException("不能删除该节点，请先删除其子节点");
        }
        */
        attendanceSummaryDao.deleteById(id);
    }

    @Override
    public int getNum(DataQuery query) {
        return attendanceSummaryDao.countData(query);
    }

    @Override
    public List<AttendanceDateDto> getDate(String loginname, String yyyyMm) {

        // 1. 解析 yyyyMm（例如 "202506"）
        if (yyyyMm == null || yyyyMm.length() != 6) {
            throw new IllegalArgumentException("yyyyMm must be in format 'yyyyMM', e.g., '202506'");
        }

        String yyyy = yyyyMm.substring(0, 4);
        String mm = yyyyMm.substring(4, 6);

        YearMonth yearMonth = YearMonth.parse(yyyyMm, DateTimeFormatter.ofPattern("yyyyMM"));

        // 2. 获取该月第一天和最后一天
        LocalDate firstDay = yearMonth.atDay(1);
        LocalDate lastDay = yearMonth.atEndOfMonth();
        // 定义星期映射（Locale.CHINA 确保 Monday=1）
        List<String> weekNames = Arrays.asList("", "周一", "周二", "周三", "周四", "周五", "周六", "周日");

        // 3. 生成每一天的 AttendanceDateDto
        List<AttendanceDateDto> result = new ArrayList<>();
        LocalDate current = firstDay;

        while (!current.isAfter(lastDay)) {
            AttendanceDateDto dto = new AttendanceDateDto();
            dto.setYyyy(String.valueOf(current.getYear()));
            dto.setMm(String.format("%02d", current.getMonthValue()));
            dto.setDd(String.format("%02d", current.getDayOfMonth()));
            // summary 暂时设为 null，后续可由调用方填充

            // 获取星期几（1=周一, 7=周日）
            int dayOfWeek = current.getDayOfWeek().getValue(); // ISO-8601: Monday=1
            dto.setWeek(weekNames.get(dayOfWeek));
            AttendanceSummary summary = attendanceSummaryDao.queryOne(DBRuleList.getInstance()
                    .add(new DBRule("USERKEY", loginname, "="))
                    .add(new DBRule("ATTENDANCETIME", dto.getYyyy() + dto.getMm() + dto.getDd(), "="))
                    .toList());
            if (summary != null) {
                dto.setSummary(FarmBeanUtils.copyProperties(summary, new AttendanceSummaryDto()));
            }
            result.add(dto);
            current = current.plusDays(1);
        }
        return result;
    }

    @Override
    public void bitByTest(String userkey, String date14) {
        if (StringUtils.isBlank(userkey)) {
            //非管理员修正
            userkey = FarmUserContextLoader.getCurrentUserKey();
            date14 = FarmTimeTool.getTimeDate14();
        }
        if (StringUtils.isBlank(date14)) {
            date14 = FarmTimeTool.getTimeDate14();
        }
        if (StringUtils.isBlank(userkey)) {
            userkey = FarmUserContextLoader.getCurrentUserKey();
        }

        // 获取配置
        String ssConfig = farmParameter.getStringParameter("farm2.config.attendance.s.s.time"); // 上午上班
        String sxConfig = farmParameter.getStringParameter("farm2.config.attendance.s.x.time"); // 上午下班
        String xsConfig = farmParameter.getStringParameter("farm2.config.attendance.x.s.time"); // 下午上班
        String xxConfig = farmParameter.getStringParameter("farm2.config.attendance.x.x.time"); // 下午下班

        AttendanceSummary summary = initSummary(userkey, date14);

        // 🔥 关键：如果配置为 "0"，则对应字段强制设为 "0"（表示无需考勤）
        summary.setSstime("0".equals(ssConfig) ? "0" : summary.getSstime());
        summary.setSxtime("0".equals(sxConfig) ? "0" : summary.getSxtime());
        summary.setXstime("0".equals(xsConfig) ? "0" : summary.getXstime());
        summary.setXxtime("0".equals(xxConfig) ? "0" : summary.getXxtime());

        // 提取打卡时间 HHmm
        String punchHHmm = date14.substring(8, 12);
        int punchMinutes = timeToMinutes(punchHHmm);

        // 查找最匹配的有效（非 "0"）配置项
        String targetField = null;
        int minDiff = Integer.MAX_VALUE;

        if (isTimeValidAndNotZero(ssConfig)) {
            int diff = Math.abs(punchMinutes - timeToMinutes(ssConfig));
            if (diff < minDiff) {
                minDiff = diff;
                targetField = "ss";
            }
        }
        if (isTimeValidAndNotZero(sxConfig)) {
            int diff = Math.abs(punchMinutes - timeToMinutes(sxConfig));
            if (diff < minDiff) {
                minDiff = diff;
                targetField = "sx";
            }
        }
        if (isTimeValidAndNotZero(xsConfig)) {
            int diff = Math.abs(punchMinutes - timeToMinutes(xsConfig));
            if (diff < minDiff) {
                minDiff = diff;
                targetField = "xs";
            }
        }
        if (isTimeValidAndNotZero(xxConfig)) {
            int diff = Math.abs(punchMinutes - timeToMinutes(xxConfig));
            if (diff < minDiff) {
                minDiff = diff;
                targetField = "xx";
            }
        }

        if (targetField == null) {
            log.warn("打卡时间 {} 无法匹配任何有效考勤时段（可能所有时段已禁用），用户: {}", date14, userkey);
            // 即使无匹配，也要保存可能被重置为 "0" 的字段
            attendanceSummaryDao.update(summary);
            return;
        }

        // 根据字段类型决定保留最早（上班）还是最晚（下班）
        switch (targetField) {
            case "ss": // 上午上班 → 保留最早
                if (!"0".equals(summary.getSstime())) { // 只有未被禁用才处理
                    if (summary.getSstime() == null || "0".equals(summary.getSstime()) || date14.compareTo(summary.getSstime()) < 0) {
                        summary.setSstime(date14);
                    }
                }
                break;
            case "xs": // 下午上班 → 保留最早
                if (!"0".equals(summary.getXstime())) {
                    if (summary.getXstime() == null || "0".equals(summary.getXstime()) || date14.compareTo(summary.getXstime()) < 0) {
                        summary.setXstime(date14);
                    }
                }
                break;
            case "sx": // 上午下班 → 保留最晚
                if (!"0".equals(summary.getSxtime())) {
                    if (summary.getSxtime() == null || "0".equals(summary.getSxtime()) || date14.compareTo(summary.getSxtime()) > 0) {
                        summary.setSxtime(date14);
                    }
                }
                break;
            case "xx": // 下午下班 → 保留最晚
                if (!"0".equals(summary.getXxtime())) {
                    if (summary.getXxtime() == null || "0".equals(summary.getXxtime()) || date14.compareTo(summary.getXxtime()) > 0) {
                        summary.setXxtime(date14);
                    }
                }
                break;
        }
        summary.setState("0");
        attendanceSummaryDao.update(summary);
    }

    @Override
    public void updateState(String userkey, String time8, String state, String note) {
        AttendanceSummary summary = initSummary(userkey, time8);
        summary.setExceptiontype(state);
        summary.setExemptnote(note);
        summary.setState("2");
        attendanceSummaryDao.update(summary);
    }

    /**
     * 判断配置是否有效且不为 "0"
     */
    private boolean isTimeValidAndNotZero(String timeStr) {
        return StringUtils.isNotBlank(timeStr) && !"0".equals(timeStr);
    }

    /**
     * 判断时间配置是否有效（非空且不等于 "0"）
     */
    private boolean isTimeValid(String timeStr) {
        return StringUtils.isNotBlank(timeStr) && !"0".equals(timeStr);
    }

    /**
     * 将 "HHmm" 格式字符串转为从 00:00 起的分钟数
     * 例如 "0830" → 8*60 + 30 = 510
     */
    private int timeToMinutes(String hhmm) {
        if (hhmm == null || hhmm.length() != 4) {
            return 0;
        }
        int hour = Integer.parseInt(hhmm.substring(0, 2));
        int minute = Integer.parseInt(hhmm.substring(2, 4));
        return hour * 60 + minute;
    }

    private AttendanceSummary initSummary(String userkey, String date8) {
        AttendanceSummary summary = attendanceSummaryDao.queryOne(DBRuleList.getInstance()
                .add(new DBRule("USERKEY", userkey, "="))
                .add(new DBRule("ATTENDANCETIME", date8.substring(0, 8), "="))
                .toList());
        if (summary == null) {
            summary = new AttendanceSummary();
            summary.setCtime(FarmTimeTool.getTimeDate14());
            summary.setUserkey(userkey);
            summary.setAttendancetime(date8.substring(0, 8));
            summary.setWorkhours(Float.intBitsToFloat(0));
            summary.setState("0");
            attendanceSummaryDao.insert(summary);
        }
        return summary;
    }
    
    /*[tree：树形结构使用]
    @Transactional
    @Override
    public void moveTreeNode(List<String> sourceIds, String targetId) {
        for (String sourceId : sourceIds) {
            // 移动节点
            AttendanceSummary node = getAttendanceSummaryById(sourceId);
            if (!"NONE".equals(targetId)) {
                AttendanceSummary target = getAttendanceSummaryById(targetId);
                if (target.getTreecode().indexOf(node.getTreecode()) >= 0) {
                    throw new RuntimeException("不能够移动到其子节点下!");
                }
            }
            node.setParentid(targetId);
            attendanceSummaryDao.update(node);
            // 构造所有树TREECODE
            List<AttendanceSummary> list = attendanceSummaryDao.findSubNodes(sourceId);
            for (AttendanceSummary treenode : list) {
                initTreeCode(treenode.getId());
            }
        }
    }*/

    /**[tree：树形结构使用]
     * 构造treecode字段
     * @param treeNodeId
    private void initTreeCode(String treeNodeId) {
    AttendanceSummary node = attendanceSummaryDao.findById(treeNodeId);
    if (node.getParentid().equals("NONE")) {
    node.setTreecode(node.getId());
    } else {
    node.setTreecode(attendanceSummaryDao.findById(node.getParentid()).getTreecode() + node.getId());
    }
    attendanceSummaryDao.update(node);
    }
     */
    /* [tree：树形结构使用]
    @Transactional
    @Override
    public void autoSort(List<String> ids) {
        int sort = 0;
        for (String id : ids) {
            AttendanceSummary node =  attendanceSummaryDao.findById(id);
            if (sort == 0) {
                sort = node.getSortcode();
            }
            node.setSortcode(sort++);
            attendanceSummaryDao.update(node);
        }
    }*/
}
