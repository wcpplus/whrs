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
            <Ptype_left ref="leftCategoryRef" :choose-handle="categoryChooseHandle"></Ptype_left>
          </div>
          <div style="margin-top: 20px;">
            <Family_left ref="familyRef" :choose-handle="familyChooseHandle"></Family_left>
          </div>
        </el-aside>
        <el-container>
          <el-header>
            <!--普通查询条件  isShowQueryPlus是否展示高级查询-->
            <Data_querys placeholder="名称|KEY" :isShowQueryPlus="true">
            </Data_querys>
            <div class="farm2-data-buttons-operators">
              <el-button type="primary" @click="openDataFormClick" plain><el-icon>
                  <Plus />
                </el-icon>创建</el-button>
              <el-button type="danger" @click="datasDeleteClick" plain><el-icon>
                  <Delete />
                </el-icon>删除</el-button>
            </div>
          </el-header>
          <el-main>
            <!--数据集-->
            <div class="farm2-data-table">
              <el-table v-loading="loadingFlag" :data="dataResult.data" :stripe="true"
                @selection-change="handleSelectionChange" :highlight-current-row="true" class="table"
                @sort-change="(data: SortInfo) => { doDataQuery(tableUtils.getSortQuery(data)) }">
                <el-table-column fixed="left" type="selection" width="55" />
                <el-table-column fixed="left" sortable="custom" prop="NAME" label="名称" width="150" />
                <el-table-column sortable="custom" prop="KEYID" label="编码" width="100" />
                <el-table-column sortable="custom" prop="STATE" :formatter="dic.formatSTATE" label="状态" width="80" />
                <el-table-column sortable="custom" prop="TRACKTYPE_ALT" label="职级序列" width="120" />
                <el-table-column sortable="custom" prop="FAMILYID_ALT" label="岗位族" width="120" />
                <el-table-column sortable="custom" prop="GRADEID_ALT" label="默认职级" width="120" />
                <el-table-column sortable="custom" prop="MAXUNUM" label="编制人数" min-width="150" />
                <el-table-column sortable="custom" prop="NOTE" label="备注" min-width="150" />
                <el-table-column prop="PTYPENUM" label="岗位类别" width="120">
                  <template #default="scope">
                    <el-button v-if="scope.row.PTYPENUM == 0" size="small" type="info"
                      @click="postTypeMngClick(scope.row.ID)">
                      无
                    </el-button>
                    <el-button v-if="scope.row.PTYPENUM > 0" size="small" type="success"
                      @click="postTypeMngClick(scope.row.ID)">
                      {{ scope.row.PTYPENUM }}类别
                    </el-button>
                  </template>
                </el-table-column>
                <el-table-column fixed="right" label="操作" width="260">
                  <template #default="scope">
                    <el-button v-if="false" link type="primary" icon="View" size="small"
                      @click="dataViewClick(scope.row)">
                      查看
                    </el-button>
                    <el-button link type="primary" icon="Edit" size="small" @click="dataEditClick(scope.row)">
                      修改
                    </el-button>
                    <el-button link type="success" icon="User" size="small" @click="userMngClick(scope.row)">
                      用户({{ scope.row.USERNUM }})
                    </el-button>
                    <el-button link type="success" icon="Lock" size="small"
                      @click="openActionsTreeWin(scope.row)">权限</el-button>
                    <el-button link type="danger" icon="Delete" size="small"
                      @click="dataDeleteClick(scope.row)">删除</el-button>
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
  <Post_form_query></Post_form_query>
  <!--数据实体表单-->
  <Post_form_data :category="categoryInfo"></Post_form_data>
  <!--权限树选中表单-->
  <Actions_tree_choose></Actions_tree_choose>
  <!--用户岗位管理-->
  <userpost_page_win :refresh-handle="() => { doDataQuery(); }" ref="userPostRef"></userpost_page_win>
  <!--岗位类别管理-->
  <Authpostptype_mng_win :refresh-handle="() => { doDataQuery(); }" ref="typeMngWinRef"></Authpostptype_mng_win>
</template>
<script setup lang="ts">
import userpost_page_win from '../userpost/userpost_page_win.vue';
import Actions_tree_choose from '@/views/system/post/components/post_actions_tree_choose.vue'
import Data_navigate from '@/components/datas/data_navigate.vue';
import dic from './utils/dictinarys'
import Data_page from '@/components/datas/data_page.vue';
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
import Post_form_query from './components/post_form_query.vue';
import Post_form_data from './components/post_form_data.vue';
import { getCreatType, getUpdateType, getViewType, type FormParam } from '@/types/commons/FormType';
import Farm2Request from '@/service/remoteRequests/Farm2Request';
import { RequestTypeEnum } from '@/types/commons/RequestTypeEnum';
import { ElMessageBox } from 'element-plus';
import { MittKesEnum } from './utils/mittKeys';
import Ptype_left from '../authptype/ptype_left.vue';
import Authpostptype_mng_win from '../authpostptype/authpostptype_mng_win.vue';
import Family_left from '../authfamily/family_left.vue';
//---------------------------------------------------------
//路由信息：
//import post_page from "@/views/apps/post/post_page.vue";
// {
//     path: "post",
//     name: "post_page",
//     component: post_page,
//     meta: { title: "角色" },
//   },
//---------------------------------------------------------
const eventBus = mitt();//事件总线
const userPostRef = ref();
const familyRef = ref();
const dataResult: DataResult = reactive(QueryUtils.getDefaultResult());//数据查询结果
const dataQuery: DataQuery = reactive({});//查询条件
const loadingFlag = ref(true);//加载中遮罩
const typeMngWinRef = ref();
const multipleSelection = ref([]);//当前选定行
const handleSelectionChange = (selection: []) => {
  multipleSelection.value = selection;//选定行事件
};
const categoryInfo = reactive({
  id: '',
  name: '',
});
//---------------------------------------------------------
//---------------------------------------------------------
const postTypeMngClick = (postId: string) => {
  typeMngWinRef.value.openWin(true, [postId]);
}

const categoryChooseHandle = (categoryId: string, name: string) => {
  categoryInfo.id = categoryId;
  categoryInfo.name = name;
  doDataQuery();
}

const familyChooseHandle = (id: string) => {
  if (id) {
    QueryUtils.initRule(dataQuery, "FAMILYID", id, "=");
  } else {
    if (dataQuery.rules) {
      QueryUtils.removeRuleByKey(dataQuery.rules, "FAMILYID");
    }
  }
  doDataQuery();
}


/**
 * 管理角色用户
 */
const userMngClick = (row: { ID: string, NAME: string }) => {
  userPostRef.value.openWin(row.ID, row.NAME);
}
/**
 * 后台检索查询--HTTP(POST)
 * @param query
 */
const doDataQuery = async (query?: DataQuery) => {
  Object.assign(dataQuery, query)
  if (categoryInfo.id) {
    QueryUtils.initRule(dataQuery, "categoryId", categoryInfo.id, "=");
  } else {
    if (dataQuery.rules) {
      QueryUtils.removeRuleByKey(dataQuery.rules, "categoryId");
    }
  }
  farm2Request.search('api/post/query', Object.assign(dataQuery, query), loadingFlag)
    .then((remoteResult: DataResult) => {
      Object.assign(dataResult, remoteResult);
    })
    .catch((msg: Error) => {
      notice.byError(msg.message);
    });
}

/**
 * 后台批量删除--HTTP(DELETE)
 * @param ids
 */
const doDataDelete = (ids: string[]) => {
  Farm2Request.submit(
    'api/post/batch',
    RequestTypeEnum.delete,
    loadingFlag, ids
  ).then(() => {
    doDataQuery();
  }).catch((msg: Error) => {
    notice.byError(msg.message);
  });
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
 *打开树窗口
 * @param row 选定行数据
 */
const openActionsTreeWin = (row: { ID: string }) => {
  eventBus.emit(MittKesEnum.openActionsSettingForm, row.ID);
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

//---------------------------------------------------------
//---------------------------------------------------------
onMounted(() => {
  //页面加载后调用默认查询事件
  doDataQuery();
  //事件注册
  eventBus.on(PageEvent.data_do_query, (query) => {
    doDataQuery(query as DataQuery);//数据查询方法
  })
});
onBeforeMount(() => {
  provide('eventBus', eventBus);//子组件注入事件总线
});
onBeforeUnmount(() => {
  eventBus.all.clear();//移除事件总线
})
</script>
