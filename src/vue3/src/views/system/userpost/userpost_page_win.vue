<template>
  <el-drawer @closed="closeHandle" v-model="isDrawerOpenRef" class="farm2-data-form" :append-to-body="true"
    :with-header="false" size="800px">
    <el-container>
      <el-header>
        <div class="title" style="padding: 0px;padding-left: 20px;margin-bottom: 20px;"> {{ currentPost.postName }}
        </div>
      </el-header>
      <el-main>
        <el-container>
          <el-container>
            <el-header>
              <!--普通查询条件  isShowQueryPlus是否展示高级查询-->
              <Data_querys placeholder="名称" :isShowQueryPlus="true">
              </Data_querys>
              <div class="farm2-data-buttons-operators">
                <el-button icon="Plus" type="primary" v-if="props.userMngAble === true" @click="openDataFormClick"
                  plain>添加</el-button>
                <el-button icon="Delete" type="danger" v-if="props.userMngAble === true" @click="datasDeleteClick"
                  plain>删除</el-button>
              </div>
            </el-header>
            <el-main>
              <!--数据集-->
              <div class="farm2-data-table">
                <el-table v-loading="loadingFlag" :data="dataResult.data" :stripe="true"
                  @selection-change="handleSelectionChange" :highlight-current-row="true" class="table"
                  @sort-change="(data: SortInfo) => { doDataQuery(tableUtils.getSortQuery(data)) }">
                  <el-table-column fixed="left" type="selection" width="55" />
                  <el-table-column sortable="custom" prop="USERKEY" label="用户key" min-width="120" />
                  <el-table-column sortable="custom" prop="USERNAME" label="用户名称" min-width="120" />
                  <el-table-column v-if="false" sortable="custom" prop="POSTID" label="角色id" min-width="120" />
                  <el-table-column fixed="right" label="操作" min-width="50">
                    <template #default="scope">
                      <el-button v-if="false" link type="primary" icon="View" size="small"
                        @click="dataViewClick(scope.row)">
                        查看
                      </el-button>
                      <el-button v-if="false" link type="primary" icon="Edit" size="small"
                        @click="dataEditClick(scope.row)">
                        修改
                      </el-button>
                      <el-button link v-if="props.userMngAble === true" type="danger" icon="Delete" size="small"
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
    <!--选择用户窗口-->
    <Localuser_choose ref="chooseUserWinRef" :choose-handle="chooseUserKeyHandle"></Localuser_choose>
  </el-drawer>
  <!--高级查询表单-->
  <UserPost_form_query></UserPost_form_query>
  <!--数据实体表单-->
  <UserPost_form_data></UserPost_form_data>
</template>
<script setup lang="ts">
import Data_page from '@/components/datas/data_page.vue';
import { onBeforeMount, onBeforeUnmount, onMounted, provide, reactive, ref } from 'vue';
import farm2Request from '@/service/remoteRequests/Farm2Request';
import Data_querys from '@/components/datas/data_querys.vue';
import notice from '@/components/msg/FarmNotice';
import Localuser_choose from '@/views/system/localuser/localuser_choose.vue';
import QueryUtils from '@/hook/farmQueryUtils'
import mitt from 'mitt';
import { PageEvent } from '@/types/commons/PageEvent';
import type { SortInfo } from '@/types/elementplus/SortInfo';
import type { DataQuery } from '@/types/commons/DataQuery';
import type { DataResult } from '@/types/commons/DataResult';
import tableUtils from '@/hook/farmTableUtils';
import UserPost_form_query from './components/userpost_form_query.vue';
import UserPost_form_data from './components/userpost_form_data.vue';
import { getUpdateType, getViewType, type FormParam } from '@/types/commons/FormType';
import Farm2Request from '@/service/remoteRequests/Farm2Request';
import { RequestTypeEnum } from '@/types/commons/RequestTypeEnum';
import { ElMessageBox } from 'element-plus';
import farmQueryUtils from '@/hook/farmQueryUtils';
//---------------------------------------------------------
//路由信息：
//import userpost_page from "@/views/system/userpost/userpost_page.vue";
// {
//     path: "userpost",
//     name: "userpost_page",
//     component: userpost_page,
//     meta: { title: "用户岗位" },
//   },
//---------------------------------------------------------
const props = defineProps({
  userMngAble: {//默认可管理角色用户
    type: Boolean,
    required: false,
    default: true,
  },
  refreshHandle: {
    type: Function,
    required: true
  }
});
const eventBus = mitt();//事件总线
const isDrawerOpenRef = ref(false);
const chooseUserWinRef = ref();
const dataResult: DataResult = reactive(QueryUtils.getDefaultResult());//数据查询结果
const dataQuery: DataQuery = reactive({});//查询条件
const loadingFlag = ref(true);//加载中遮罩
const multipleSelection = ref([]);//当前选定行
const handleSelectionChange = (selection: []) => {
  multipleSelection.value = selection;//选定行事件
};
const currentPost = reactive({
  postId: '',
  postName: '',
}
)
//---------------------------------------------------------
//---------------------------------------------------------
const closeHandle = () => {
  props.refreshHandle();
}
/**
 * 后台检索查询--HTTP(POST)
 * @param query
 */
const doDataQuery = async (query?: DataQuery) => {
  if (!query) { query = {} };
  farmQueryUtils.initRule(query, 'postid', currentPost.postId, "=");
  farm2Request.search('api/userpost/query', Object.assign(dataQuery, query), loadingFlag)
    .then((remoteResult: DataResult) => {
      Object.assign(dataResult, remoteResult);
    })
    .catch((msg: string) => {
      notice.byError(msg);
    });
}

/**
 * 后台批量删除--HTTP(DELETE)
 * @param ids
 */
const doDataDelete = (ids: string[]) => {
  Farm2Request.submit(
    'api/userpost/batch',
    RequestTypeEnum.delete,
    loadingFlag, ids
  ).then(() => {
    doDataQuery();
  }).catch((msg: string) => {
    notice.byError(msg);
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
  chooseUserWinRef.value.openWin(true);
  // eventBus.emit(PageEvent.data_form_open, { isOpen: true, type: <FormParam>{ type: getCreatType() } });
}

const chooseUserKeyHandle = (keys: Array<string>) => {
  if (keys.length > 0) {
    chooseUserWinRef.value.openWin(false);
    farm2Request
      .submit('api/userpost', RequestTypeEnum.post, loadingFlag, { ids: keys, id: currentPost.postId })
      .then(() => {
        doDataQuery();
      })
      .catch((msg: string) => {
        notice.byError(msg); // 报错
      });
  }
}


const openWin = (postid: string, postname: string) => {
  isDrawerOpenRef.value = true;
  currentPost.postId = postid;
  currentPost.postName = postname;
  //页面加载后调用默认查询事件
  doDataQuery();
}

//---------------------------------------------------------
//---------------------------------------------------------
// 暴露方法给父组件
defineExpose({
  openWin,
});
onMounted(() => {

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
