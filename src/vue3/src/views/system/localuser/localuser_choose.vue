<template>
  <el-dialog v-model="showDialogRef" title="选择用户" width="1000"
    style="height: 570px;background-color: var(--el-skc-theme-gray-color);max-width: 90%;">
    <el-container>
      <el-main>
        <el-container>
          <el-aside width="284px" style="padding-left: 10px;height: 510px;">
            <Data_tree :isShowCtrl="true" :treeReloadMittKey="PageEvent.data_do_reload_tree_node"
              :chooseNodeMittKey="PageEvent.data_choose_tree" loadTreeUrl="api/localorg/tree"></Data_tree>
          </el-aside>
          <el-container>
            <el-header>
              <!--普通查询条件  isShowQueryPlus是否展示高级查询-->
              <Data_querys placeholder="姓名|登录名" :isShowQueryPlus="true">
                <!--角色查询-->
                <el-select clearable v-model="queryRulePlus.postId" filterable placeholder="用户岗位"
                  style="width: 160px;margin-right: 10px; margin-top: -20px;">
                  <el-option v-for="item in posts" :key="item.id" :label="item.name" :value="item.id" />
                </el-select>
              </Data_querys>
              <div class="farm2-data-buttons-operators">
                <el-button type="primary" @click="datasChooseClick" icon="Select" plain>批量选择</el-button>
              </div>
            </el-header>
            <el-main>
              <!--数据集-->
              <div class="farm2-data-table" style="height:350px;">
                <el-table v-loading="loadingFlag" :data="dataResult.data" style="width: 100%" :stripe="true"
                  @selection-change="handleSelectionChange" :highlight-current-row="true" class="table"
                  @sort-change="(data: SortInfo) => { doDataQuery(tableUtils.getSortQuery(data)) }">
                  <el-table-column fixed="left" type="selection" width="55" />
                  <el-table-column fixed="left" sortable="custom" prop="NAME" label="姓名" min-width="150px" />
                  <el-table-column sortable="custom" prop="LOGINNAME" label="登录名" min-width="120" />
                  <el-table-column sortable="custom" prop="ORGNAME" label="组织机构" min-width="120" />
                  <el-table-column prop="TYPE" :formatter="dic.formatTYPE" label="类型" min-width="120" />
                  <el-table-column prop="STATE" :formatter="dic.formatSTATE" label="状态" min-width="120" />
                  <el-table-column sortable="custom" prop="LOGINTIME" :formatter="dic.formatTime" label="登录时间"
                    width="140" />
                  <el-table-column prop="NOTE" label="备注" width="300" />
                  <el-table-column fixed="right" label="操作" width="100">
                    <template #default="scope">
                      <el-button link type="primary" icon="Select" size="small" @click="datasChooseClick(scope.row)">
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
  </el-dialog>
  <!--高级查询表单-->
  <User_form_query></User_form_query>
</template>
<script setup lang="ts">
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
import dic from './utils/dictinarys'
import { getViewType, type FormParam } from '@/types/commons/FormType';
import { ElMessageBox } from 'element-plus';
import type { Post } from '../post/utils/post';
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
const queryRulePlus = reactive({
  postId: ''
});
const posts: Array<Post> = reactive([]);
const props = defineProps({
  chooseHandle: {
    type: Function,
    required: true,
  }
});
const eventBus = mitt();//事件总线
const dataResult: DataResult = reactive(QueryUtils.getDefaultResult());//数据查询结果
const dataQuery: DataQuery = reactive({});//查询条件
const loadingFlag = ref(true);//加载中遮罩
const showDialogRef = ref(false);
const multipleSelection = ref([]);//当前选定行
const handleSelectionChange = (selection: []) => {
  multipleSelection.value = selection;//选定行事件
};
//---------------------------------------------------------
//---------------------------------------------------------
const loadPosts = () => {
  farmClientUtils.postList('api/post/posts', {}, (list: []) => {
    Object.assign(posts, list);
  });
}
/**
 * 删除多条-事件
 * @param row
 */
const datasChooseClick = (row?: { LOGINNAME: string }) => {
  if (row?.LOGINNAME) {
    props.chooseHandle([row.LOGINNAME]);
  } else {
    const selection: { LOGINNAME: string; }[] = multipleSelection.value;
    const ids = selection.map(item => item.LOGINNAME);
    ElMessageBox.confirm('当前选定' + ids.length + '条数据,确认选择?', { icon: 'QuestionFilled' })
      .then(() => {
        props.chooseHandle(ids);
      }).catch(() => { });
  }
}


/**
 * 后台检索查询用户--HTTP(POST)
 * @param query
 */
const doDataQuery = async (query?: DataQuery) => {
  Object.assign(dataQuery, query)
  QueryUtils.initRule(dataQuery, "postid", queryRulePlus.postId, "=");
  farm2Request.search('api/localuser/query', dataQuery, loadingFlag)
    .then((remoteResult: DataResult) => {
      Object.assign(dataResult, remoteResult);
    })
    .catch((msg: Error) => {
      notice.byError(msg.message);
    });
}

/**
 * 打开数据查看窗口-事件
 * @param row 选定行数据
 */
// eslint-disable-next-line @typescript-eslint/no-unused-vars
const dataViewClick = (row: { ID: string }) => {
  eventBus.emit(PageEvent.data_form_open, { isOpen: true, type: <FormParam>{ type: getViewType(), id: row.ID, data: row } });
}



// 定义一个方法
const openWin = (isOpen: boolean) => {
  showDialogRef.value = isOpen
};
/**
 * 选中树节点
 * @param row
 */
const chooseTreeNode = (row: { NAME: string, ID: string }) => {
  doDataQuery({ rules: [{ key: 'ORGID', value: row.ID, compara: '=' }] });
}
// 暴露方法给父组件
defineExpose({
  openWin,
});
//---------------------------------------------------------
//---------------------------------------------------------
onMounted(() => {
  //页面加载后调用默认查询事件
  doDataQuery();


  loadPosts();
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
