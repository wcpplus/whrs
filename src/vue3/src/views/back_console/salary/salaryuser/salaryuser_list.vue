<template>
  <el-container>
    <el-header>
      <!--导航条 -->
      <Data_navigate></Data_navigate>
    </el-header>
    <el-main>
      <el-container>
        <el-aside width="234px" style="padding-left: 10px;;">
          <div style="">
            <Data_tree :isShowCtrl="true" :treeReloadMittKey="PageEvent.data_do_reload_tree_node"
              :chooseNodeMittKey="PageEvent.data_choose_tree" loadTreeUrl="api/localorg/tree"></Data_tree>
          </div>
          <div style="margin-top: 20px;">
            <div class="whrs-user-state-box">
              <div class="whrs-user-state-item" @click="stateSearch('')"
                style="background-color: var(--el-skc-theme-color);color: #ffffff;">全部 </div>
              <div class="whrs-user-state-item" @click="stateSearch('1')">试用 <div class="whrs-num"><el-button
                    type="primary" size="small" round>{{
                      userNums.probationNum }}人</el-button></div>
              </div>
              <div class="whrs-user-state-item" @click="stateSearch('2')">正式 <div class="whrs-num"><el-button
                    type="success" size="small" round>{{
                      userNums.activeNum }}人</el-button></div>
              </div>
              <div class="whrs-user-state-item" @click="stateSearch('3')">离职 <div class="whrs-num"><el-button
                    type="danger" size="small" round>{{
                      userNums.leftNum }}人</el-button></div>
              </div>
            </div>
          </div>
        </el-aside>
        <el-container>
          <el-header>
            <div style="display: flex;">
              <div style="width: 500px;padding-left: 30px;">
                <el-date-picker v-model="mmRef" type="month" value-format="YYYYMMDDHHmmss" placeholder="请选择日期"
                  @change="monthChangeHandle" />
              </div>
              <div style="margin-left: auto;"> <!--普通查询条件  isShowQueryPlus是否展示高级查询-->
                <Data_querys placeholder="姓名|登录名" :isShowQueryPlus="true">
                </Data_querys>
              </div>
            </div>
          </el-header>
          <el-main>
            <!--数据集-->
            <div class="farm2-data-table">
              <el-table v-loading="loadingFlag" :data="dataResult.data" style="width: 100%" :stripe="true"
                @selection-change="handleSelectionChange" :highlight-current-row="true" class="table"
                @sort-change="(data: SortInfo) => { doDataQuery(tableUtils.getSortQuery(data)) }">
                <el-table-column fixed="left" label="" width="50">
                  <template #default="scope">
                    <img class="skc-photo"
                      :src="'/images/photo/photo' + (scope.row.PHOTOID ? scope.row.PHOTOID : '6') + '.png'"
                      style="width: 28px;height: 28px;margin: 0px;margin-top: 4px;" alt="Logo" />
                  </template>
                </el-table-column>
                <el-table-column fixed="left" sortable="custom" prop="NAME" label="姓名" width="120px" />
                <el-table-column fixed="left" prop="STATE_ALT" label="状态" width="60">
                  <template #default="scope">
                    <el-text class="mx-1" v-if="['0'].includes(scope.row.STATE)" type="info">
                      {{ scope.row.STATE_ALT }}
                    </el-text>
                    <el-text class="mx-1" v-if="['1'].includes(scope.row.STATE)" type="primary">
                      {{ scope.row.STATE_ALT }}
                    </el-text>
                    <el-text class="mx-1" v-if="['2'].includes(scope.row.STATE)" type="success">
                      {{ scope.row.STATE_ALT }}
                    </el-text>
                    <el-text class="mx-1" v-if="['3'].includes(scope.row.STATE)" type="danger">
                      {{ scope.row.STATE_ALT }}
                    </el-text>
                    <el-text class="mx-1" v-if="['4'].includes(scope.row.STATE)" type="warning">
                      {{ scope.row.STATE_ALT }}
                    </el-text>
                  </template>
                </el-table-column>
                <el-table-column sortable="custom" prop="LOGINNAME" label="登录名" width="120" />
                <el-table-column prop="ORG_ALT" label="组织机构" width="220" />
                <el-table-column label="薪酬模板" width="220">
                  <template #default="scope">
                    <el-button link v-if="scope.row.USER_SALARY_TEMPLATE_ID" @click="dataEditClick(scope.row)"
                      type="success" size="small" round>{{ scope.row.USER_SALARY_TEMPLATE_ALT }}</el-button>
                    <el-button v-if="!scope.row.USER_SALARY_TEMPLATE_ID" @click="dataEditClick(scope.row)" type="danger"
                      size="small" round>设置模板</el-button>
                  </template>
                </el-table-column>
                <el-table-column :label="'周期:' + farmTimeUtils.formatDate6(para.yyyymm)" width="220">
                  <template #default="scope">
                    <el-button link v-if="scope.row.USER_SALARY_TEMPLATE_ID && scope.row.UNIT_STATE === '1'"
                      @click="userBaseSalaryWinRef.openWin(true, scope.row.UNIT_ID)" type="primary" size="small" round>
                      重新计算
                    </el-button>
                    <el-button v-if="scope.row.USER_SALARY_TEMPLATE_ID && scope.row.UNIT_STATE === '0'"
                      @click="userBaseSalaryWinRef.openWin(true, scope.row.UNIT_ID)" type="success" size="small" round>
                      计算薪资
                    </el-button>
                    <span v-if="!scope.row.USER_SALARY_TEMPLATE_ID" style="color: #999999;">请先设置模板</span>
                  </template>
                </el-table-column>
                <el-table-column prop="UNIT_GROSSPAY" label="应发工资" width="120" />
                <el-table-column prop="UNIT_NETPAY" label="实发工资" width="120" />
                <el-table-column prop="UNIT_TAXAMOUNT" label="个税" min-width="100" />
                <el-table-column fixed="right" label="操作" min-width="80">
                  <template #default="scope">
                    <el-button link type="primary" icon="View" size="small" @click="() => {
                      userSalaryItemWinRef.openWin(true, scope.row.UNIT_ID);
                    }">
                      明细
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </el-main>
          <el-footer>
            <!--分页框-->
            <Data_page :remoteData="dataResult" />
          </el-footer>
        </el-container>
      </el-container>
    </el-main>
  </el-container>
  <Salaryuseritem_win ref="userSalaryItemWinRef"></Salaryuseritem_win>
  <!--高级查询表单-->
  <Salaryuser_form_query></Salaryuser_form_query>
  <!--数据实体表单-->
  <Salaryuser_form_data></Salaryuser_form_data>
  <!--生成薪资-->
  <Salaryuserbase_win :refresh-handle="() => {
    doDataQuery();
  }" ref="userBaseSalaryWinRef"></Salaryuserbase_win>
</template>
<script setup lang="ts">
import Data_navigate from '@/components/datas/data_navigate.vue';
import Data_page from '@/components/datas/data_page.vue';
import Data_tree from '@/components/datas/data_tree.vue';
import { onBeforeMount, onBeforeUnmount, onMounted, provide, reactive, ref } from 'vue';
import Data_querys from '@/components/datas/data_querys.vue';
import QueryUtils from '@/hook/farmQueryUtils'
import mitt from 'mitt';
import { PageEvent } from '@/types/commons/PageEvent';
import type { SortInfo } from '@/types/elementplus/SortInfo';
import type { DataQuery } from '@/types/commons/DataQuery';
import type { DataResult } from '@/types/commons/DataResult';
import tableUtils from '@/hook/farmTableUtils';
import farmClientUtils from '@/hook/farmClientUtils';
import Salaryuser_form_query from './components/salaryuser_form_query.vue';
import Salaryuser_form_data from './components/salaryuser_form_data.vue';
import { getUpdateType, type FormParam } from '@/types/commons/FormType';
import farmTimeUtils from '@/hook/farmTimeUtils';
import Salaryuserbase_win from '../salaryuserbase/salaryuserbase_win.vue';
import Salaryuseritem_win from '../salaryuseritem/salaryuseritem_win.vue';
//---------------------------------------------------------
//路由信息：
//import user_page from "@/views/home/user/user_page.vue";
// {
//     path: "user",
//     name: "user_page",
//     component: user_page,
//     meta: { title: "用户管理" },
//   },
//---------------------------------------------------------
const eventBus = mitt();//事件总线
const mmRef = ref();
const userSalaryItemWinRef = ref();
const dataResult: DataResult = reactive(QueryUtils.getDefaultResult());//数据查询结果
const dataQuery: DataQuery = reactive({});//查询条件
const loadingFlag = ref(true);//加载中遮罩
const userBaseSalaryWinRef = ref();
const multipleSelection = ref([]);//当前选定行
const handleSelectionChange = (selection: []) => {
  multipleSelection.value = selection;//选定行事件
};
const userNums = reactive({
  pendingNum: 0,
  probationNum: 0,
  activeNum: 0,
  leftNum: 0,
  retiredNum: 0,
});
const para = reactive({
  yyyymm: ''
})
const dateFields: Array<{ yyyy: string, mm: string, week: string, dd: number }> = reactive([]);
//---------------------------------------------------------
//---------------------------------------------------------
/**
 * 打开数据更新窗口-事件
 * @param row 选定行数据
 */
const dataEditClick = (row: { ID: string }) => {
  eventBus.emit(PageEvent.data_form_open, { isOpen: true, type: <FormParam>{ type: getUpdateType(), id: row.ID, data: row } });
}

const stateSearch = (state: string) => {
  if (state) {
    QueryUtils.initRule(dataQuery, "A.STATE", state, "=");
  } else {
    if (dataQuery.rules) {
      QueryUtils.removeRuleByKey(dataQuery.rules, "A.STATE");
    }
  }
  doDataQuery();
}
const monthChangeHandle = () => {
  doDataQuery();
}


/**
 * 后台检索查询用户--HTTP(POST)
 * @param query
 */
const doDataQuery = async (query?: DataQuery) => {
  farmClientUtils.postObject('api/salaryuser/dates', { query: Object.assign(dataQuery, query), yyyyMm: mmRef.value }, (remoteResult: object) => {
    Object.assign(dataResult, remoteResult);
    const fields = dataResult.data as Array<{ DATE: Array<{ yyyy: string, mm: string, week: string, dd: number }> }>;
    if (fields.length > 0) {
      Object.assign(dateFields, fields[0].DATE);
    }
    const ym8 = dataResult.para as { yyyymm: string };
    para.yyyymm = ym8.yyyymm;
  }, loadingFlag);
  loadNums();
}



/**
 * 选中树节点
 * @param row
 */
const chooseTreeNode = (row: { NAME: string, ID: string }) => {
  doDataQuery({ rules: [{ key: 'ORGID', value: row.ID, compara: '=' }] });
}
const loadNums = () => {
  farmClientUtils.postObject('api/wuser/nums', {}, (nums: object) => {
    Object.assign(userNums, nums);
  })

}

//---------------------------------------------------------
//---------------------------------------------------------
onMounted(() => {
  //页面加载后调用默认查询事件
  doDataQuery();
  loadNums();
  //事件注册
  eventBus.on(PageEvent.data_do_query, (query) => {
    doDataQuery(query as DataQuery);//数据查询方法
  });
  eventBus.on(PageEvent.data_choose_tree, (row) => {
    chooseTreeNode(row as { NAME: string, ID: string });//用户点击树
  })
});
onBeforeMount(() => {
  provide('eventBus', eventBus);//子组件注入事件总线
});
onBeforeUnmount(() => {
  eventBus.all.clear();//移除事件总线
})
</script>

<style scoped>
.whrs-date-day {
  height: 100%;
  padding: 10px;
  border-radius: 10px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  margin: 8px;
  min-height: 100px;
  background-color: var(--el-skc-theme-body-back-c);
  cursor: pointer;
  color: #666666;

  .el-tag {
    margin-top: 8px;
    margin-left: 4px;
  }

  /* 添加阴影 */
}

.whrs-date-day:hover {
  opacity: 0.6;
}

.week-end {
  color: #ffffff;
  background-color: var(--el-skc-theme-primary-back-c);

}

.whrs-day-item {
  display: flex;

  .whrs-title {
    flex: 1;
  }

  .whrs-val {
    flex: 1;
    border-bottom: 1px solid #cccccc;
  }
}

.whrs-user-state-box {
  background-color: #ffffff;
  border-radius: 15px;
  padding: 20px 10px;

  .whrs-user-state-item {
    padding: 10px 20px;
    font-size: 14px;
    color: #999999;
    cursor: pointer;
    border-radius: 4px;

    .whrs-num {
      float: right;
      color: chocolate;
    }
  }

  .whrs-user-state-item:hover {
    background-color: #f1f1f1;
  }
}
</style>
