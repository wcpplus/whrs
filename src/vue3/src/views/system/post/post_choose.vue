<template>
  <el-drawer v-model="isOpenWin" class="farm2-data-form" :append-to-body="true" :with-header="false" size="1000px">
    <div class="title">选择角色</div>
    <el-container style="height: calc(100vh - 66px);background-color: var(--el-farms-win-bg-color);">
      <el-header>
        <!--导航条 -->
        <Data_navigate></Data_navigate>
      </el-header>
      <el-main>
        <el-container>
          <el-aside width="284px" style="padding-left: 10px;;">
            <div style="">
              <Ptype_left ref="leftCategoryRef" :is-view="true" :choose-handle="categoryChooseHandle"></Ptype_left>
            </div>
            <div style="margin-top: 20px;">
              <Family_left ref="familyRef" :is-view="true" :choose-handle="familyChooseHandle"></Family_left>
            </div>
          </el-aside>
          <el-container>
            <el-header>
              <!--普通查询条件  isShowQueryPlus是否展示高级查询-->
              <Data_querys placeholder="名称|KEY" :isShowQueryPlus="false">
              </Data_querys>
              <div class="farm2-data-buttons-operators">
                <el-button type="primary" @click="choosePosts" plain><el-icon>
                    <Plus />
                  </el-icon>选择</el-button>
              </div>
            </el-header>
            <el-main>
              <!--数据集-->
              <div class="farm2-data-table">
                <el-table v-loading="loadingFlag" :data="dataResult.data" :stripe="true"
                  @selection-change="handleSelectionChange" :highlight-current-row="true" class="table"
                  @sort-change="(data: SortInfo) => { doDataQuery(tableUtils.getSortQuery(data)) }">
                  <el-table-column fixed="left" type="selection" width="55" />
                  <el-table-column fixed="left" sortable="custom" prop="NAME" label="名称" min-width="120" />
                  <el-table-column fixed="left" label="用户" width="100">
                    <template #default="scope">
                      <el-button type="success" @click="userMngClick(scope.row)" size="small" round>{{ scope.row.USERNUM
                      }}人</el-button>
                    </template>
                  </el-table-column>
                  <el-table-column sortable="custom" prop="TRACKTYPE_ALT" label="职级序列" width="120" />
                  <el-table-column sortable="custom" prop="FAMILYID_ALT" label="岗位族" width="120" />
                  <el-table-column sortable="custom" prop="GRADEID_ALT" label="默认职级" width="120" />
                  <el-table-column sortable="custom" prop="MAXUNUM" label="编制人数" min-width="150" />
                  <el-table-column sortable="custom" prop="KEYID" label="编码" width="120" />
                  <el-table-column v-if="false" sortable="custom" prop="STATE" :formatter="dic.formatSTATE" label="状态"
                    width="80" />
                  <el-table-column fixed="right" label="操作" width="100">
                    <template #default="scope">
                      <el-button link type="primary" icon="View" size="small" @click="chooseClick(scope.row)">
                        选择
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
  </el-drawer>
  <!--高级查询表单-->
  <Post_form_query></Post_form_query>
  <!--数据实体表单-->
  <Post_form_data></Post_form_data>
  <!--权限树选中表单-->
  <Actions_tree_choose></Actions_tree_choose>
  <!--用户岗位管理-->
  <Userpost_page_win :user-mng-able="false" :refresh-handle="() => { }" ref="userPostRef"></userpost_page_win>
</template>
<script setup lang="ts">
import Data_querys from '@/components/datas/data_querys.vue';
import Actions_tree_choose from '@/views/system/post/components/post_actions_tree_choose.vue'
import Data_navigate from '@/components/datas/data_navigate.vue';
import dic from './utils/dictinarys'
import Data_page from '@/components/datas/data_page.vue';
import { onBeforeMount, onBeforeUnmount, onMounted, provide, reactive, ref } from 'vue';
import farm2Request from '@/service/remoteRequests/Farm2Request';
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
import Userpost_page_win from '../userpost/userpost_page_win.vue';
import { ElMessage } from 'element-plus';
import Ptype_left from '../authptype/ptype_left.vue';
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
const isOpenWin = ref(false);
const dataResult: DataResult = reactive(QueryUtils.getDefaultResult());//数据查询结果
const dataQuery: DataQuery = reactive({});//查询条件
const loadingFlag = ref(true);//加载中遮罩
const multipleSelection = ref([]);//当前选定行
const handleSelectionChange = (selection: []) => {
  multipleSelection.value = selection;//选定行事件
};
const userPostRef = ref();
const categoryInfo = reactive({
  id: '',
  name: '',
});
//---------------------------------------------------------
//---------------------------------------------------------

const categoryChooseHandle = (categoryId: string, name: string) => {
  categoryInfo.id = categoryId;
  categoryInfo.name = name;
  doDataQuery();
}

const props = defineProps({
  choosehandle: {
    type: Function,
    required: true
  }
});
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
 * 删除多条-事件
 * @param row
 */
const choosePosts = () => {
  const selection: { KEYID: string; NAME: string; ID: string }[] = multipleSelection.value;
  const keys = selection.map(item => item.KEYID);
  if (keys.length <= 0) {
    ElMessage.info('请至少选中一条角色');
    return;
  }
  props.choosehandle(keys, selection);
  isOpenWin.value = false;
}

/**
 * 选择角色
 * @param row 选定行数据
 */
const chooseClick = (row: { KEYID: string; NAME: string; ID: string }) => {
  props.choosehandle([row.KEYID], [row]);
  isOpenWin.value = false;
  //console.log(row);
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


const openWin = () => {
  isOpenWin.value = true;
};
//---------------------------------------------------
defineExpose({
  openWin
});

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
