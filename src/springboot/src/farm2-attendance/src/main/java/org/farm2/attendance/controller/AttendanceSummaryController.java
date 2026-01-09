package org.farm2.attendance.controller;

import jakarta.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.farm2.attendance.dao.DateDataQueryDto;
import org.farm2.attendance.service.AttendanceAsyncService;
import org.farm2.auth.dto.KeyValueDto;
import org.farm2.auth.face.FarmParameter;
import org.farm2.auth.service.AuthGradeServiceInter;
import org.farm2.auth.service.PostServiceInter;
import org.farm2.base.domain.FarmPost;
import org.farm2.attendance.domain.AttendanceSummary;
import org.farm2.attendance.dto.AttendanceSummaryDto;
import org.farm2.attendance.service.AttendanceSummaryServiceInter;
import org.farm2.files.service.ResourceFileServiceInter;
import org.farm2.files.utils.FileDownloadUtils;
import org.farm2.luser.domain.LocalOrg;
import org.farm2.luser.service.LocalOrgServiceInter;
import org.farm2.luser.service.LocalUserInfoServiceInter;
import org.farm2.luser.service.LocalUserServiceInter;
import org.farm2.luser.utils.FarmUserStateEnum;
import org.farm2.report.FarmReport;
import org.farm2.tools.bean.FarmBeanUtils;
import org.farm2.tools.caches.FarmCacheKeys;
import org.farm2.tools.caches.FarmCaches;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataQueryDto;
import org.farm2.tools.db.DataResult;
import org.farm2.tools.db.commons.DBRule;
import org.farm2.tools.db.commons.SqlRule;
import org.farm2.tools.db.commons.WhereInRule;
import org.farm2.tools.db.enums.FarmDbLikeModel;
import org.farm2.tools.db.commons.DBSort;
import org.farm2.tools.time.FarmTimeTool;
import org.farm2.tools.web.FarmResponseCode;
import org.farm2.tools.web.FarmResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 考勤结果
 *
 * @author cbtg自动生成  2026-1-6 15:34:44
 */
@RestController
@RequestMapping("/api/attendancesummary")
public class AttendanceSummaryController {
    @Autowired
    private AttendanceSummaryServiceInter attendanceSummaryServiceImpl;
    @Autowired
    private LocalUserServiceInter localUserServiceImpl;
    @Autowired
    private LocalOrgServiceInter localOrgServiceImpl;
    @Autowired
    private PostServiceInter postServiceImpl;
    @Autowired
    private FarmParameter farmParameter;
    @Autowired
    private FileDownloadUtils fileDownloadUtils;
    @Autowired
    private ResourceFileServiceInter resourceFileServiceImpl;
    @Autowired
    private FarmReport farmReport;
    @Autowired
    private AuthGradeServiceInter authGradeServiceImpl;
    @Autowired
    private LocalUserInfoServiceInter localUserInfoServiceImpl;
    @Autowired
    private AttendanceAsyncService asyncService;


    /**
     * 条件查询
     *
     * @param query
     * @return
     */
    @PreAuthorize("@farmAction.has('attendanceSummary.query')")
    @PostMapping("/query")
    public FarmResponseResult queryAll(@RequestBody DataQueryDto query) {
        DataQuery dataQuery = DataQuery.getInstance(query);
        dataQuery.addDefaultSort(new DBSort("ATTENDANCETIME", DBSort.SORT_TYPE.DESC));
        dataQuery.setRuleAsSql("default", "and (NAME like '%?%')", FarmDbLikeModel.ALL);
        DataResult result = attendanceSummaryServiceImpl.searchAttendanceSummary(dataQuery);
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, result);
    }


    /**
     * 条件查询
     *
     * @return
     */
    @PostMapping("/counts")
    public FarmResponseResult queryAll() {
        LocalDate firstDay = LocalDate.now().withDayOfMonth(1);
        LocalTime midnight = LocalTime.MIDNIGHT;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String day1_14 = firstDay.atTime(midnight).format(formatter);
        List<AttendanceSummary> summarys = attendanceSummaryServiceImpl.getAttendanceSummarys(DataQuery.getInstance().setPageSize(5000).setCount(false)
                .addRule(new DBRule("ATTENDANCETIME", day1_14, ">"))
        );
        Integer s_zc = 0;
        Integer s_zt = 0;
        Integer s_cd = 0;
        Integer s_qq = 0;
        Integer s_qj = 0;
        Integer s_yc = 0;

        for (AttendanceSummary sum : summarys) {
            if (sum.getExceptiontype() == null) {
                continue;
            }
            if (sum.getExceptiontype().contains("0")) {
                s_zc++;
            }
            if (sum.getExceptiontype().contains("1")) {
                s_cd++;
            }
            if (sum.getExceptiontype().contains("2")) {
                s_zt++;
            }
            if (sum.getExceptiontype().contains("3")) {
                s_qq++;
            }
            if (sum.getExceptiontype().contains("4")) {
                s_qq++;
            }
            if (sum.getExceptiontype().contains("5")) {
                s_yc++;
            }
        }

        Map<String, Integer> nums = new HashMap<>();
        //        正常s_zc
        nums.put("s_zc", s_zc);
        //        早退s_zt
        nums.put("s_zt", s_zt);
        //        迟到s_cd
        nums.put("s_cd", s_cd);
        //        缺勤s_qq
        nums.put("s_qq", s_qq);
        //        请假s_qj
        nums.put("s_qj", s_qj);
        //        远程s_yc
        nums.put("s_yc", s_yc);
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, nums);
    }


    /**
     * 条件查询
     *
     * @param query
     * @return
     */
    @PostMapping("/systime")
    public FarmResponseResult systime(@RequestBody DataQueryDto query) {
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, "", FarmTimeTool.getTimeDate14());
    }

    /**
     * 手动打卡
     *
     * @return
     */
    @PostMapping("/bitTest")
    public FarmResponseResult bitTest(@RequestBody KeyValueDto timeAndUser) {
        String userkey = timeAndUser.getKey();
        String date14 = timeAndUser.getVal();
        attendanceSummaryServiceImpl.bitByTest(userkey, date14);
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, "", FarmTimeTool.getTimeDate14());
    }

    @PostMapping("/updateState")
    public FarmResponseResult updateState(@RequestBody KeyValueDto timeAndUser) {
        String userkey = timeAndUser.getKey();
        String state = timeAndUser.getVal();
        String note = timeAndUser.getNote();
        String time = timeAndUser.getTime();
        attendanceSummaryServiceImpl.updateState(userkey, time, state, note);
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, "", FarmTimeTool.getTimeDate14());
    }

    /**
     * 计算考勤结果
     *
     * @return
     */
    @PostMapping("/run")
    public FarmResponseResult run(@RequestBody KeyValueDto timeAndUser) {
        asyncService.runAllSummary();
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, "", FarmTimeTool.getTimeDate14());
    }

    /**
     * 考勤日历
     *
     * @param query
     * @return
     */
    @PostMapping("/dates")
    public FarmResponseResult dates(@RequestBody DateDataQueryDto query) {
        if (StringUtils.isBlank(query.getYyyyMm())) {
            query.setYyyyMm(FarmTimeTool.getTimeDate14().substring(0, 6));
        } else {
            query.setYyyyMm(query.getYyyyMm().substring(0, 6));
        }
        DataQuery dataQuery = DataQuery.getInstance(query.getQuery());
        if ("NONE".equals(dataQuery.getRuleValue("ORGID")) || dataQuery.getRuleValue("ORGID") == null) {
            dataQuery.removeRule("ORGID");
        } else {
            LocalOrg org = localOrgServiceImpl.getLocalOrgById((String) dataQuery.getRuleValue("ORGID"));
            dataQuery.removeRule("ORGID");
            dataQuery.addRule(new SqlRule(" and (TREECODE like ?)", org.getTreecode() + "%"));
        }
        dataQuery.addRule(new WhereInRule("A.STATE", Set.of("1", "2", "3")));
        dataQuery.addDefaultSort(new DBSort("LOGINNAME", DBSort.SORT_TYPE.DESC));
        dataQuery.setRuleAsSql("default", " and (A.NAME like ?  or LOGINNAME like ?)", FarmDbLikeModel.ALL);
        DataResult result = localUserServiceImpl.searchLocalUser(dataQuery);
        result.runDataHandle(node -> {
            String orgId = (String) node.get("ORGID");
            String loginname = (String) node.get("LOGINNAME");
            String state = (String) node.get("STATE");
            node.put("STATE_ALT", FarmUserStateEnum.valueOfType(state).getTitle());
            if (StringUtils.isNotBlank(orgId)) {
                List<LocalOrg> orgs = localOrgServiceImpl.getOrgPath(orgId);
                if (!orgs.isEmpty()) {
                    node.put("ORG_ALT", orgs.stream().map(LocalOrg::getName).collect(Collectors.joining("/")));
                }
            }
            List<FarmPost> posts = postServiceImpl.getUserPosts(loginname, true);
            if (!posts.isEmpty()) {
                node.put("POST_ALT", posts.stream().map(FarmPost::getName).collect(Collectors.joining("|")));
            }

            node.put("DATE", attendanceSummaryServiceImpl.getDate(loginname, query.getYyyyMm()));
        });
        //result.runFormatTime("CTIME", "yyyy-MM-dd HH:mm");
        //刷新权限相关缓存
        {
            FarmCaches.getInstance().clearCache(FarmCacheKeys.PERMISSION_USER_KNOW_POPS);
            FarmCaches.getInstance().clearCache(FarmCacheKeys.USER_POSTS);
        }
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, result);
    }


    /**[tree：树形结构使用]
     * 条件查询(树形结构)
     *
     * @param query
     * @return
     @PreAuthorize("@farmAction.has('attendanceSummary.query')")
     @PostMapping("/query") public FarmResponseResult queryAll(@RequestBody DataQueryDto query) {
     DataQuery dataQuery = DataQuery.getInstance(query);
     dataQuery.addDefaultSort(new DBSort("SORTCODE", DBSort.SORT_TYPE.ASC));
     dataQuery.setRuleAsSql("default", "and (NAME like '%?%')");
     if(!dataQuery.hasRules()){
     dataQuery.addRule(new DBRule("PARENTID","NONE","="));
     }
     DataResult result = attendanceSummaryServiceImpl.searchAttendanceSummary(dataQuery);
     result.runDataHandle(new ResultDataHandle() {
     @Override public void handle(Map<String, Object> row) {
     row.put("isLeaf", attendanceSummaryServiceImpl.getNum(DataQuery.getInstance().addRule(new DBRule("PARENTID", row.get("ID"), "="))) <= 0);
     }
     });
     return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, result);
     }
     */


    /**[tree：树形结构使用]
     * 查询树
     *
     * @param query
     * @return

     @PreAuthorize("@farmAction.has('attendanceSummary.query')")
     @PostMapping("/tree") public FarmResponseResult treeload(@RequestBody DataQueryDto query) {
     DataQuery dataQuery = DataQuery.getInstance(query);
     dataQuery.addDefaultSort(new DBSort("SORTCODE", DBSort.SORT_TYPE.ASC));
     dataQuery.setPageSizeAll();
     dataQuery.setRuleAsSql("default", "and (NAME like '%?%')");
     Object parentid = dataQuery.getRuleValue("PARENTID");
     if (parentid == null || StringUtils.isBlank(parentid.toString())) {
     List<Map<String, Object>> list = new ArrayList<>();
     Map<String, Object> node = new HashMap<>();
     node.put("ID", "NONE");
     node.put("value", "NONE");
     node.put("ICON", Icons.HomeFilled.getSvg());
     node.put("NAME", "考勤结果");
     node.put("isLeaf", attendanceSummaryServiceImpl.getNum(DataQuery.getInstance().addRule(new DBRule("PARENTID", "NONE", "="))) <= 0);
     list.add(node);
     DataResult result = new DataResult(list, 1);
     return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, result);
     }
     DataResult result = attendanceSummaryServiceImpl.searchAttendanceSummary(dataQuery);
     result.runDataHandle(new ResultDataHandle() {
     @Override public void handle(Map<String, Object> row) {
     row.put("isLeaf", attendanceSummaryServiceImpl.getNum(DataQuery.getInstance().addRule(new DBRule("PARENTID", row.get("ID"), "="))) <= 0);
     row.put("value", row.get("ID"));
     }
     });
     return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, result);
     }
     */


    /**[tree：树形结构使用]
     * 移动节点结构
     *
     * @param paras
     * @return

     @PreAuthorize("@farmAction.has('attendanceSummary.move')")
     @PostMapping("/move") public FarmResponseResult move(@RequestBody IdAndIdsDto paras) {
     attendanceSummaryServiceImpl.moveTreeNode(paras.getIds(), paras.getId());
     return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, null);
     }
     */

    /**[tree：树形结构使用]
     * 自动设置排序
     *
     * @param ids
     * @return

     @PreAuthorize("@farmAction.has('attendanceSummary.autosort')")
     @PostMapping("/autosort") public FarmResponseResult autosort(@RequestBody List<String> ids) {
     attendanceSummaryServiceImpl.autoSort(ids);
     return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, null);
     }*/


    /**
     * 添加数据
     *
     * @param attendanceSummaryDto
     * @return
     */
    @PreAuthorize("@farmAction.has('attendanceSummary.add')")
    @PostMapping
    public FarmResponseResult addSubmit(@Valid @RequestBody AttendanceSummaryDto attendanceSummaryDto) {
        AttendanceSummary attendanceSummary = attendanceSummaryServiceImpl.insertAttendanceSummaryEntity(FarmBeanUtils.copyProperties(attendanceSummaryDto, new AttendanceSummary()));
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, attendanceSummary.getId());
    }

    /**
     * 修改数据
     *
     * @param id
     * @param attendanceSummaryDto
     * @return
     */
    @PreAuthorize("@farmAction.has('attendanceSummary.edit')")
    @PutMapping("/{id}")
    public FarmResponseResult editSubmit(@PathVariable String id, @Valid @RequestBody AttendanceSummaryDto attendanceSummaryDto) {
        attendanceSummaryDto.setId(id);
        attendanceSummaryServiceImpl.editAttendanceSummaryEntity(FarmBeanUtils.copyProperties(attendanceSummaryDto, new AttendanceSummary()));
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS);
    }

    /**
     * 删除数据
     *
     * @param id
     * @return
     */
    @PreAuthorize("@farmAction.has('attendanceSummary.del')")
    @DeleteMapping("/{id}")
    public FarmResponseResult delSubmit(@PathVariable String id) {
        attendanceSummaryServiceImpl.delAttendanceSummary(id);
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, id);
    }

    /**
     * 批量删除数据
     *
     * @param ids 要删除的记录ID列表
     * @return 操作结果
     */
    @PreAuthorize("@farmAction.has('attendanceSummary.del')")
    @DeleteMapping("/batch")
    public FarmResponseResult batchDelSubmit(@RequestBody List<String> ids) {
        for (String id : ids) {
            attendanceSummaryServiceImpl.delAttendanceSummary(id);
        }
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS);
    }

    /**
     * 数据浏览
     *
     * @param id
     * @return
     */
    @PreAuthorize("@farmAction.has('attendanceSummary.info')")
    @GetMapping("/{id}")
    public FarmResponseResult info(@PathVariable String id) {
        AttendanceSummary attendanceSummary = attendanceSummaryServiceImpl.getAttendanceSummaryById(id);
        return new FarmResponseResult(FarmResponseCode.SUCESS, FarmBeanUtils.copyProperties(attendanceSummary, new AttendanceSummaryDto()));
    }
}
