package org.farm2.salary.controller;

import jakarta.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.farm2.attendance.dao.DateDataQueryDto;
import org.farm2.attendance.domain.AttendanceSummary;
import org.farm2.auth.service.PostServiceInter;
import org.farm2.base.domain.FarmPost;
import org.farm2.base.elementplus.Icons;
import org.farm2.luser.domain.LocalOrg;
import org.farm2.luser.domain.LocalUser;
import org.farm2.luser.service.LocalOrgServiceInter;
import org.farm2.luser.service.LocalUserServiceInter;
import org.farm2.luser.utils.FarmUserStateEnum;
import org.farm2.salary.domain.SalaryTemplate;
import org.farm2.salary.domain.SalaryUser;
import org.farm2.salary.domain.SalaryUserUnit;
import org.farm2.salary.dto.SalaryUserDto;
import org.farm2.salary.service.SalaryTemplateServiceInter;
import org.farm2.salary.service.SalaryUserServiceInter;
import org.farm2.salary.service.SalaryUserUnitServiceInter;
import org.farm2.tools.bean.FarmBeanUtils;
import org.farm2.tools.caches.FarmCacheKeys;
import org.farm2.tools.caches.FarmCaches;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataQueryDto;
import org.farm2.tools.db.DataResult;
import org.farm2.tools.db.commons.SqlRule;
import org.farm2.tools.db.commons.WhereInRule;
import org.farm2.tools.db.enums.FarmDbLikeModel;
import org.farm2.tools.db.commons.DBSort;
import org.farm2.tools.db.commons.DBRule;
import org.farm2.tools.time.FarmTimeTool;
import org.farm2.tools.web.FarmResponseCode;
import org.farm2.tools.web.FarmResponseResult;
import org.farm2.tools.web.dto.IdAndIdsDto;
import org.farm2.tools.db.ResultDataHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户薪酬WHRS_SALARY_USER
 *
 * @author cbtg自动生成  2026-1-8 11:14:25
 */
@RestController
@RequestMapping("/api/salaryuser")
public class SalaryUserController {
    @Autowired
    private SalaryUserServiceInter salaryUserServiceImpl;
    @Autowired
    private LocalUserServiceInter localUserServiceImpl;
    @Autowired
    private LocalOrgServiceInter localOrgServiceImpl;
    @Autowired
    private PostServiceInter postServiceImpl;
    @Autowired
    private SalaryTemplateServiceInter salaryTemplateServiceImpl;
    @Autowired
    private SalaryUserUnitServiceInter salaryUserUnitServiceImpl;


    /**
     * 条件查询
     *
     * @return
     */
    @PostMapping("/counts")
    public FarmResponseResult queryAll() {
        String t_this_time6 = FarmTimeTool.getTimeDate14().substring(0, 6);
        YearMonth current = YearMonth.now();
        YearMonth lastMonth = current.minusMonths(1);
        String t_last_time6 = lastMonth.format(DateTimeFormatter.ofPattern("yyyyMM"));

        int compSalary = salaryUserUnitServiceImpl.getNum(DataQuery.getInstance()
                .addRule(new DBRule("SALARYTIME", t_this_time6, "="))
                .addRule(new DBRule("STATE", "1", "="))
        );
        BigDecimal lastAllSalary = salaryUserUnitServiceImpl.getSalaryUserUnits(
                        DataQuery.getInstance()
                                .addRule(new DBRule("SALARYTIME", t_last_time6, "="))
                                .addRule(new DBRule("STATE", "1", "="))
                )
                .stream()
                .filter(Objects::nonNull)
                .map(SalaryUserUnit::getGrosspay) // 方法引用更简洁
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, Object> nums = new HashMap<>();
        //        完成计算薪资的人数
        nums.put("finish_salary", compSalary);
        //        上个月工资总额
        nums.put("last_all_Salary", lastAllSalary);
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, nums);
    }

    @PostMapping("/salaryLastSixMonths")
    public FarmResponseResult getSalaryLastSixMonths() {
        List<String> last6Months = new ArrayList<>();
        List<BigDecimal> salaryList = new ArrayList<>();

        // 生成最近6个月的 yyyyMM（从当前月开始，往前推5个月，共6个）
        YearMonth current = YearMonth.now();
        for (int i = 0; i < 6; i++) {
            YearMonth ym = current.minusMonths(i);
            String yyyymm = ym.format(DateTimeFormatter.ofPattern("yyyyMM"));
            last6Months.add(yyyymm);
        }
        // 此时 last6Months = [202601, 202512, 202511, 202510, 202509, 202508]

        // 对每个月查询并累加工资
        for (String salaryTime : last6Months) {
            List<SalaryUserUnit> units = salaryUserUnitServiceImpl.getSalaryUserUnits(
                    DataQuery.getInstance()
                            .addRule(new DBRule("SALARYTIME", salaryTime, "="))
                            .addRule(new DBRule("STATE", "1", "="))
            );

            BigDecimal monthTotal = units.stream()
                    .filter(Objects::nonNull)
                    .map(SalaryUserUnit::getGrosspay)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            salaryList.add(monthTotal);
        }

        // 返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("salaryLastSixMonths", salaryList); // List<BigDecimal>
        result.put("months", last6Months); // 可选：返回对应的月份标签，前端对齐用

        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, result);
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
        String yyyymm = query.getYyyyMm();
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
        result.setPara(Map.of("yyyymm", yyyymm));
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
            SalaryUser userSalary = salaryUserServiceImpl.initSalaryUser(loginname);
            if (StringUtils.isNotBlank(userSalary.getTemplateid())) {
                //薪资模板
                SalaryTemplate template = salaryTemplateServiceImpl.getSalaryTemplateById(userSalary.getTemplateid());
                if (template != null) {
                    node.put("USER_SALARY_TEMPLATE_ALT", template.getName());
                }
                node.put("USER_SALARY_TEMPLATE_ID", userSalary.getTemplateid());
            }
            SalaryUserUnit unit = salaryUserUnitServiceImpl.initUnit(loginname, yyyymm);
            node.put("UNIT_ID", unit.getId());
            node.put("UNIT_STATE", unit.getState());
            node.put("UNIT_GROSSPAY", unit.getGrosspay());
            node.put("UNIT_NETPAY", unit.getNetpay());
            node.put("UNIT_TAXAMOUNT", unit.getTaxamount());
        });
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, result);
    }


    /**
     * 条件查询
     *
     * @param query
     * @return
     */
    @PreAuthorize("@farmAction.has('salaryUser.query')")
    @PostMapping("/query")
    public FarmResponseResult queryAll(@RequestBody DataQueryDto query) {
        DataQuery dataQuery = DataQuery.getInstance(query);
        dataQuery.addDefaultSort(new DBSort("USERKEY", DBSort.SORT_TYPE.DESC));
        dataQuery.setRuleAsSql("default", "and (USERNAME like ?)", FarmDbLikeModel.ALL);
        DataResult result = salaryUserServiceImpl.searchSalaryUser(dataQuery);
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, result);
    }

    /**
     * 添加数据
     *
     * @param salaryUserDto
     * @return
     */
    @PreAuthorize("@farmAction.has('salaryUser.add')")
    @PostMapping
    public FarmResponseResult addSubmit(@Valid @RequestBody SalaryUserDto salaryUserDto) {
        SalaryUser salaryUser = salaryUserServiceImpl.insertSalaryUserEntity(FarmBeanUtils.copyProperties(salaryUserDto, new SalaryUser()));
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, salaryUser.getId());
    }

    /**
     * 修改数据
     *
     * @param id
     * @param salaryUserDto
     * @return
     */
    @PreAuthorize("@farmAction.has('salaryUser.edit')")
    @PutMapping("/{id}")
    public FarmResponseResult editSubmit(@PathVariable String id, @Valid @RequestBody SalaryUserDto salaryUserDto) {
        salaryUserDto.setId(id);
        salaryUserServiceImpl.editSalaryUserEntity(FarmBeanUtils.copyProperties(salaryUserDto, new SalaryUser()));
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS);
    }

    /**
     * 删除数据
     *
     * @param id
     * @return
     */
    @PreAuthorize("@farmAction.has('salaryUser.del')")
    @DeleteMapping("/{id}")
    public FarmResponseResult delSubmit(@PathVariable String id) {
        salaryUserServiceImpl.delSalaryUser(id);
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, id);
    }

    /**
     * 批量删除数据
     *
     * @param ids 要删除的记录ID列表
     * @return 操作结果
     */
    @PreAuthorize("@farmAction.has('salaryUser.del')")
    @DeleteMapping("/batch")
    public FarmResponseResult batchDelSubmit(@RequestBody List<String> ids) {
        for (String id : ids) {
            salaryUserServiceImpl.delSalaryUser(id);
        }
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS);
    }

    /**
     * 数据浏览
     *
     * @param id
     * @return
     */
    @PreAuthorize("@farmAction.has('salaryUser.info')")
    @GetMapping("/{id}")
    public FarmResponseResult info(@PathVariable String id) {
        LocalUser user = localUserServiceImpl.getLocalUserById(id);
        SalaryUser salaryUser = salaryUserServiceImpl.initSalaryUser(user.getLoginname());
        return new FarmResponseResult(FarmResponseCode.SUCESS, FarmBeanUtils.copyProperties(salaryUser, new SalaryUserDto()));
    }
}
