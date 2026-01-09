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
                <el-button style="margin-left: 20px;" @click="runSummarys()" type="primary" round>计算考勤结果</el-button>
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
                <el-table-column sortable="custom" prop="ORG_ALT" label="组织机构" width="220" />
                <el-table-column v-for="field, index in dateFields" :key="index"
                  :label="field.mm + '月' + field.dd + '日(' + field.week + ')'" min-width="200">
                  <template #default="scope">
                    <div class="whrs-date-day" @click="() => {
                      bitClick(scope.row.LOGINNAME, scope.row.NAME, field.yyyy + field.mm + field.dd);
                    }" :class="field.week == '周日' || field.week == '周六' ? 'week-end' : ''">
                      <div v-if="scope.row.DATE[index] && scope.row.DATE[index].summary">
                        <div class="whrs-day-item">
                          <div class="whrs-val"> {{ farmTimeUtils.format14ToHms(scope.row.DATE[index].summary.sstime) }}
                          </div>~
                          <div class="whrs-val"> {{ farmTimeUtils.format14ToHms(scope.row.DATE[index].summary.sxtime) }}
                          </div>
                        </div>
                        <div class="whrs-day-item">
                          <div class="whrs-val"> {{ farmTimeUtils.format14ToHms(scope.row.DATE[index].summary.xstime)
                          }}</div>~
                          <div class="whrs-val"> {{ farmTimeUtils.format14ToHms(scope.row.DATE[index].summary.xxtime) }}
                          </div>
                        </div>
                        <div v-if="scope.row.DATE[index].summary && scope.row.DATE[index].summary.exceptiontype"
                          style="text-align: center;">
                          <el-tag v-if="scope.row.DATE[index].summary.exceptiontype.includes('1')" type="warning"
                            effect="dark" round>迟</el-tag>
                          <el-tag v-if="scope.row.DATE[index].summary.exceptiontype.includes('2')" type="warning"
                            effect="dark" round>早</el-tag>
                          <el-tag v-if="scope.row.DATE[index].summary.exceptiontype.includes('3')" type="danger"
                            effect="dark" round>缺</el-tag>
                          <el-tag v-if="scope.row.DATE[index].summary.exceptiontype.includes('4')" type="success"
                            effect="dark" round>假</el-tag>
                          <el-tag v-if="scope.row.DATE[index].summary.exceptiontype.includes('5')" type="success"
                            effect="dark" round>远</el-tag>
                          <el-tag v-if="scope.row.DATE[index].summary.exceptiontype.includes('0')" type="success"
                            effect="dark" style="width: 50%;" round>正常</el-tag>
                          <el-tooltip class="box-item" effect="dark" :content="scope.row.DATE[index].summary.exemptnote"
                            placement="top-start">
                            <el-tag v-if="scope.row.DATE[index].summary.state == '2'" type="danger" round>更</el-tag>
                          </el-tooltip>
                        </div>
                      </div>
                    </div>
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
  <el-dialog v-if="state.dialogVisible" :append-to-body="true" style="background-color: rgb(248, 248, 250);"
    v-model="state.dialogVisible" width="800">

  </el-dialog>
  <!--高级查询表单-->
  <Attendance_bit_win ref="bitWinRef" :refresh-handle="() => {
    doDataQuery();
  }"></Attendance_bit_win>
  <User_form_query></User_form_query>
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
import User_form_query from '@/views/system/localuser/components/user_form_query.vue';
import type { AttendanceSummary } from './utils/attendancesummary';
import Attendance_bit_win from './attendance_bit_win.vue';
import farmTimeUtils from '@/hook/farmTimeUtils';
import FarmNotice from '@/components/msg/FarmNotice';
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
const state = reactive({
  dialogVisible: false
})
const eventBus = mitt();//事件总线
const bitWinRef = ref();
const mmRef = ref();
const dataResult: DataResult = reactive(QueryUtils.getDefaultResult());//数据查询结果
const dataQuery: DataQuery = reactive({});//查询条件
const loadingFlag = ref(true);//加载中遮罩
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
const dateFields: Array<{ yyyy: string, mm: string, week: string, dd: number, summary: AttendanceSummary }> = reactive([]);
//---------------------------------------------------------
const bitClick = (userKey: string, userName: string, date8: string) => {
  bitWinRef.value.openWin(true, userKey, userName, date8);
}
//---------------------------------------------------------
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

const runSummarys = () => {
  //计算考勤结果
  farmClientUtils.postObject('api/attendancesummary/run', {}, () => {
    FarmNotice.byInfo('已启动运行计算方法，请刷新本页面查看结果');
  }, loadingFlag);
}

/**
 * 后台检索查询用户--HTTP(POST)
 * @param query
 */
const doDataQuery = async (query?: DataQuery) => {
  farmClientUtils.postObject('api/attendancesummary/dates', { query: Object.assign(dataQuery, query), yyyyMm: mmRef.value }, (remoteResult: object) => {
    Object.assign(dataResult, remoteResult);
    const fields = dataResult.data as Array<{ DATE: Array<{ yyyy: string, mm: string, week: string, dd: number, summary: AttendanceSummary }> }>;
    if (fields.length > 0) {
      Object.assign(dateFields, fields[0].DATE);
    }
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
