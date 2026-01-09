<template>
  <el-container>
    <el-header>
      <!--导航条 -->
      <Data_navigate></Data_navigate>
    </el-header>
    <el-main>
      <el-container>
        <el-aside width="284px" style="padding-left: 10px;;">
          <div style="">
            <Data_tree :isShowCtrl="true" :treeReloadMittKey="PageEvent.data_do_reload_tree_node"
              :chooseNodeMittKey="PageEvent.data_choose_tree" loadTreeUrl="api/localorg/tree"></Data_tree>
          </div>
          <div style="margin-top: 20px;">
            <div class="whrs-user-state-box">
              <div class="whrs-user-state-item" @click="stateSearch('')" style="background-color: var(--el-skc-theme-color);color: #ffffff;">全部 </div>
              <div class="whrs-user-state-item" @click="stateSearch('0')">待入职 <div class="whrs-num"><el-button
                    type="info" size="small" round>{{
                      userNums.pendingNum }}人</el-button></div>
              </div>
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
              <div class="whrs-user-state-item" @click="stateSearch('4')">退休 <div class="whrs-num"><el-button
                    type="warning" size="small" round>{{
                      userNums.retiredNum }}人</el-button></div>
              </div>
            </div>
          </div>
        </el-aside>
        <el-container>
          <el-header>
            <!--普通查询条件  isShowQueryPlus是否展示高级查询-->
            <Data_querys placeholder="姓名|登录名" :isShowQueryPlus="true">
            </Data_querys>
            <div class="farm2-data-buttons-operators">
              <el-button type="primary" @click="openDataFormClick" icon="Plus" plain>创建</el-button>
              <el-dropdown style="margin-left: 20px;margin-right: 20px;">
                <el-button icon="Unlock" type="primary" plain>
                  修改密码<el-icon class="el-icon--right"><arrow-down /></el-icon>
                </el-button>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item @click="resetPasswordClick">重置密码</el-dropdown-item>
                    <el-dropdown-item @click="updatePasswordClick">修改密码</el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
              <el-dropdown style="margin-right: 20px;">
                <el-button icon="Collection" type="primary" plain>
                  导入导出<el-icon class="el-icon--right"><arrow-down /></el-icon>
                </el-button>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item @click="exportUserClick">导出用户</el-dropdown-item>
                    <el-divider style="margin-top: 4px;margin-bottom: 4px;">
                    </el-divider>
                    <el-dropdown-item @click="downloadTemplateClick">下载模板</el-dropdown-item>
                    <el-dropdown-item @click="openUploadFormClick">导入用户</el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
              <el-button type="danger" icon="Delete" @click="datasDeleteClick" plain>删除</el-button>
            </div>
          </el-header>
          <el-main>
            <!--数据集-->
            <div class="farm2-data-table">
              <el-table v-loading="loadingFlag" :data="dataResult.data" style="width: 100%" :stripe="true"
                @selection-change="handleSelectionChange" :highlight-current-row="true" class="table"
                @sort-change="(data: SortInfo) => { doDataQuery(tableUtils.getSortQuery(data)) }">
                <el-table-column fixed="left" type="selection" width="55" />
                <el-table-column fixed="left" label="" width="50">
                  <template #default="scope">
                    <img class="skc-photo"
                      :src="'/images/photo/photo' + (scope.row.PHOTOID ? scope.row.PHOTOID : '6') + '.png'"
                      style="width: 28px;height: 28px;margin: 0px;margin-top: 4px;" alt="Logo" />
                  </template>
                </el-table-column>
                <el-table-column fixed="left" sortable="custom" prop="NAME" label="姓名" width="120px" />
                <el-table-column fixed="left" label="资料完善度" width="100">
                  <template #default="scope">
                    <el-progress @click="openUserInfo(scope.row.LOGINNAME)" style="cursor: pointer;" :text-inside="true"
                      :stroke-width="26" :percentage="scope.row.PROCESS"
                      :color="scope.row.PROCESS == 0 ? '#f56c6c' : (scope.row.PROCESS > 60 ? '#4bac7b' : '#409eff')" />
                  </template>
                </el-table-column>
                <el-table-column fixed="left" label="操作" width="80">
                  <template #default="scope">
                    <el-button @click="openUserInfo(scope.row.LOGINNAME)"
                      :type="scope.row.PROCESS == 0 ? 'danger' : (scope.row.PROCESS > 60 ? 'success' : 'primary')" link>
                      完善资料
                    </el-button>
                  </template>
                </el-table-column>
                <el-table-column fixed="left" prop="STATE_ALT" label="状态" min-width="100">
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
                <el-table-column sortable="custom" prop="POST_ALT" label="岗位" min-width="120" />
                <el-table-column prop="TYPE" :formatter="dic.formatTYPE" label="类型" min-width="120" />
                <el-table-column prop="GRADEID_ALT" label="职级" min-width="120" />
                <el-table-column sortable="custom" prop="LOGINTIME" :formatter="dic.formatTime" label="登录时间"
                  width="140" />
                <el-table-column prop="NOTE" label="备注" width="300" />
                <el-table-column fixed="right" label="操作" width="200">
                  <template #default="scope">
                    <div style="text-align: right;">
                      <el-button link type="primary" size="small" v-if="false" @click="dataViewClick(scope.row)">
                        查看
                      </el-button>
                      <el-button link type="primary" size="small" v-if="scope.row.STATE == '0'"
                        @click="userOnboardingWinRef.openWin(true, scope.row.ID, 'RZ', '员工入职')">
                        入职
                      </el-button>
                      <el-button link type="primary" size="small" v-if="scope.row.STATE == '1'"
                        @click="userOnboardingWinRef.openWin(true, scope.row.ID, 'ZZ', '员工转正')">
                        转正
                      </el-button>
                      <el-button link type="primary" size="small" v-if="['1', '2'].includes(scope.row.STATE)"
                        @click="userOnboardingWinRef.openWin(true, scope.row.ID, 'DD', '员工调动')">
                        调动
                      </el-button>
                      <el-button link type="primary" size="small" v-if="['1', '2'].includes(scope.row.STATE)"
                        @click="userOnboardingWinRef.openWin(true, scope.row.ID, 'LZ', '员工离职')">
                        离职
                      </el-button>
                      <el-button link type="danger" icon="Edit" size="small" @click="dataEditClick(scope.row)">
                      </el-button>
                      <el-button link type="danger" icon="Delete" size="small"
                        @click="dataDeleteClick(scope.row)"></el-button>
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
  <!--高级查询表单-->
  <User_form_query></User_form_query>
  <!--数据实体表单-->
  <User_form_data></User_form_data>
  <!--修改密码-->
  <User_from_password update-url="api/localuser/resetpassword" ref="rePasswordForm"></user_from_password>
  <!--导入用户-->
  <User_data_upload></User_data_upload>
  <!--数据实体表单-->
  <Localuserinfo_form_data></Localuserinfo_form_data>
  <Local_user_onboarding_win ref="userOnboardingWinRef" :refresh-handle="() => { doDataQuery() }">
  </Local_user_onboarding_win>
</template>
<script setup lang="ts">
import Data_navigate from '@/components/datas/data_navigate.vue';
import User_data_upload from './components/user_data_upload.vue';
import Data_page from '@/components/datas/data_page.vue';
import Data_tree from '@/components/datas/data_tree.vue';
import { onBeforeMount, onBeforeUnmount, onMounted, provide, reactive, ref } from 'vue';
import farm2Request from '@/service/remoteRequests/Farm2Request';
import Data_querys from '@/components/datas/data_querys.vue';
import notice from '@/components/msg/FarmNotice';
import QueryUtils from '@/hook/farmQueryUtils'
import mitt from 'mitt';
import { PageEvent } from '@/types/commons/PageEvent';
import type { SortInfo } from '@/types/elementplus/SortInfo';
import type { DataQuery } from '@/types/commons/DataQuery';
import type { DataResult } from '@/types/commons/DataResult';
import tableUtils from '@/hook/farmTableUtils';
import User_form_query from './components/user_form_query.vue';
import User_form_data from './components/user_form_data.vue';
import User_from_password from './components/user_form_password.vue';
import dic from './utils/dictinarys'
import { getCreatType, getUpdateType, getViewType, type FormParam } from '@/types/commons/FormType';
import Farm2Request from '@/service/remoteRequests/Farm2Request';
import { RequestTypeEnum } from '@/types/commons/RequestTypeEnum';
import { ElMessageBox } from 'element-plus';
import { MittKeysEnum } from './utils/mittKeys'
import Localuserinfo_form_data from '../localuserinfo/components/localuserinfo_form_data.vue';
import Local_user_onboarding_win from '../localuserFlow/local_user_onboarding_win.vue';
import farmClientUtils from '@/hook/farmClientUtils';
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
const dataResult: DataResult = reactive(QueryUtils.getDefaultResult());//数据查询结果
const dataQuery: DataQuery = reactive({});//查询条件
const loadingFlag = ref(true);//加载中遮罩
const userOnboardingWinRef = ref();
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
//修改密码窗口
const rePasswordForm = ref();
//---------------------------------------------------------
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
/**
 * 后台检索查询用户--HTTP(POST)
 * @param query
 */
const doDataQuery = async (query?: DataQuery) => {
  farm2Request.search('api/localuser/query', Object.assign(dataQuery, query), loadingFlag)
    .then((remoteResult: DataResult) => {
      Object.assign(dataResult, remoteResult);
    })
    .catch((msg: Error) => {
      notice.byError(msg.message);
    });
  loadNums();
}

/**
 * 打开附件上传窗口-事件
 */
const openUploadFormClick = () => {
  eventBus.emit(MittKeysEnum.openUploadForm, { isOpen: true, type: <FormParam>{ type: { key: "creat", title: "上传导入数据" } } });
}

/**
 * 后台批量删除--HTTP(DELETE)
 * @param ids
 */
const doDataDelete = (ids: string[]) => {
  Farm2Request.submit(
    'api/localuser/batch',
    RequestTypeEnum.delete,
    loadingFlag, ids
  ).then(() => {
    doDataQuery();
  }).catch((msg: Error) => {
    notice.byError(msg.message);
  });
}

/**
 * 导出用户
 */
const exportUserClick = () => {
  Farm2Request.downloadReport("api/localuser/report",
    dataQuery, "users.xlsx",
    () => { notice.bySuccess("报表导出中..."); },
    () => { notice.bySuccess("导出成功"); });
}
const openUserInfo = (userkey: string) => {
  eventBus.emit(PageEvent.data_form_open + "_USERINFO", { isOpen: true, type: <FormParam>{ type: getUpdateType(), id: userkey, data: {} } });
}
/**
 * 下载模板
 */
const downloadTemplateClick = () => {
  Farm2Request.downloadReport("api/localuser/downtemplate",
    {}, "userImportTemplate.xls",
    () => { notice.bySuccess("模板下载中..."); },
    () => { notice.bySuccess("下载成功"); });
}

/**
 *重置默认密码-事件
 * @param row
 */
const resetPasswordClick = () => {
  const selection: { ID: string; }[] = multipleSelection.value;
  const ids = selection.map(item => item.ID);
  ElMessageBox.confirm('当前选定' + ids.length + '条数据,该操作无法恢复,确认重置密码?', { icon: 'QuestionFilled' })
    .then(() => {
      Farm2Request.submit(
        'api/localuser/defaultpassword',
        RequestTypeEnum.post,
        loadingFlag, ids
      ).then(() => {
        notice.bySuccess("密码已重置");
      }).catch((msg: Error) => {
        notice.byError(msg.message);
      });
    }).catch(() => { });
}

/**
 * 重置指定密码-事件
 * @param row
 */
const updatePasswordClick = () => {
  const selection: { LOGINNAME: string; }[] = multipleSelection.value;
  const loginnames = selection.map(item => item.LOGINNAME);
  if (loginnames.length <= 0) {
    ElMessageBox.alert("请选择数据");
    return null;
  }
  rePasswordForm.value.openWin(loginnames);
}



/**
 * 打开数据更新窗口-事件
 * @param row 选定行数据
 */
const dataEditClick = (row: { ID: string }) => {
  eventBus.emit(PageEvent.data_form_open, { isOpen: true, type: <FormParam>{ type: getUpdateType(), id: row.ID, data: row } });
}

/**
 * 删除一条-事件
 * @param row 选定行数据
 */
const dataDeleteClick = (row: { ID: string }) => {
  ElMessageBox.confirm('确认删除?', { icon: 'QuestionFilled' })
    .then(() => {
      doDataDelete([row.ID]);
    }).catch(() => { });
}

/**
 * 删除多条-事件
 * @param row
 */
const datasDeleteClick = () => {
  const selection: { ID: string; }[] = multipleSelection.value;
  const ids = selection.map(item => item.ID);
  ElMessageBox.confirm('当前选定' + ids.length + '条数据,确认删除?', { icon: 'QuestionFilled' })
    .then(() => {
      doDataDelete(ids);
    }).catch(() => { });
}



/**
 * 打开数据查看窗口-事件
 * @param row 选定行数据
 */
const dataViewClick = (row: { ID: string }) => {
  eventBus.emit(PageEvent.data_form_open, { isOpen: true, type: <FormParam>{ type: getViewType(), id: row.ID, data: row } });
}


/**
 * 打开数据创建窗口-事件
 */
const openDataFormClick = () => {
  eventBus.emit(PageEvent.data_form_open, { isOpen: true, type: <FormParam>{ type: getCreatType() } });
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
