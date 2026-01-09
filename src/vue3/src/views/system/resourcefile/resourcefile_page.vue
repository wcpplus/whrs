<template>
  <el-container>
    <el-header>
      <!--导航条 -->
      <Data_navigate></Data_navigate>
    </el-header>
    <el-main>
      <el-container>
        <el-container>
          <el-header>
            <!--普通查询条件  isShowQueryPlus是否展示高级查询-->
            <Data_querys placeholder="名称" :isShowQueryPlus="true">
            </Data_querys>
            <div class="farm2-data-buttons-operators">
              <el-button type="primary" icon="UploadFilled" @click="openUploadFormClick" plain>上传文件</el-button>
              <!-- <el-button type="primary" @click="openDataFormClick" plain><el-icon>
                  <Plus />
                </el-icon>创建</el-button>-->
              <el-button type="success" icon="GobletSquareFull" @click="datasSubmitClick" plain>注册附件</el-button>
              <el-button type="warning" icon="GobletSquare" @click="datasCancelClick" plain>注销附件</el-button>
              <el-button type="success" icon="Files" @click="runWdap" plain>生成扩展文件</el-button>
              <el-button type="danger" icon="Delete" @click="datasDeleteClick" plain>删除</el-button>
            </div>
          </el-header>
          <el-main>
            <!--数据集-->
            <div class="farm2-data-table">
              <el-table v-loading="loadingFlag" :data="dataResult.data" :stripe="true"
                @selection-change="handleSelectionChange" :highlight-current-row="true" class="table"
                @sort-change="(data: SortInfo) => { doDataQuery(tableUtils.getSortQuery(data)) }">
                <el-table-column fixed="left" type="selection" width="55" />
                <el-table-column fixed="left" sortable="custom" prop="TITLE" label="文件别名" min-width="250" />
                <el-table-column sortable="custom" :formatter="dic.formatSTATE" prop="STATE" label="状态"
                  min-width="80" />
                <el-table-column sortable="custom" :formatter="dic.formatTime" prop="CTIME" label="创建时间"
                  min-width="140" />
                <el-table-column sortable="custom" prop="CUSERKEY" label="创建用户" min-width="120" />
                <el-table-column sortable="custom" prop="NOTE" label="备注" min-width="120" />
                <el-table-column sortable="custom" prop="EXNAME" label="文件扩展名" min-width="120" />
                <el-table-column sortable="custom" prop="RELATIVEPATH" label="相对路径" min-width="120" />
                <el-table-column sortable="custom" prop="FILENAME" label="原文件名" min-width="330" />
                <el-table-column sortable="custom" prop="FILESIZE" :formatter="dic.formatFileUnit" label="文件大小"
                  min-width="120" />
                <el-table-column sortable="custom" prop="RESOURCEKEY" label="资源地址KEY" min-width="140" />
                <el-table-column fixed="right" label="操作" min-width="280">
                  <template #default="scope">
                    <el-button link type="primary" icon="View" size="small" @click="dataViewClick(scope.row)">
                      查看
                    </el-button>
                    <el-button link type="success" icon="Edit" size="small" @click="registeViewClick(scope.row)">
                      注册信息
                    </el-button>
                    <el-link style="margin-left: 8px;margin-right: 8px; font-size: 12px;" :underline="false"
                      @click="downloadFile(scope.row.ID)" type="primary" icon="Link" size="small" li>下载</el-link>
                    <!--<el-button link type="primary" icon="Edit" size="small" @click="dataEditClick(scope.row)">
                      修改
                    </el-button>-->
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
  <ResourceFile_form_query></ResourceFile_form_query>
  <!--数据实体表单-->
  <ResourceFile_form_data></ResourceFile_form_data>
  <!--附件上传表单-->
  <resourcefile_form_upload></resourcefile_form_upload>
  <!--附件注册表-->
  <resourcefile_registe_list ref="registePageRef"></resourcefile_registe_list>
</template>
<script setup lang="ts">
import { MittKeysEnum } from './utils/mittKeys'
import resourcefile_registe_list from './components/resourcefile_registe_list.vue';
import dic from './utils/dictinarys'
import Data_navigate from '@/components/datas/data_navigate.vue';
import Data_page from '@/components/datas/data_page.vue';
import { onBeforeMount, onBeforeUnmount, onMounted, provide, reactive, ref } from 'vue';
import farm2Request from '@/service/remoteRequests/Farm2Request';
import Data_querys from '@/components/datas/data_querys.vue';
import notice from '@/components/msg/FarmNotice';
import QueryUtils from '@/hook/farmQueryUtils';
import mitt from 'mitt';
import { PageEvent } from '@/types/commons/PageEvent';
import type { SortInfo } from '@/types/elementplus/SortInfo';
import type { DataQuery } from '@/types/commons/DataQuery';
import type { DataResult } from '@/types/commons/DataResult';
import tableUtils from '@/hook/farmTableUtils';
import ResourceFile_form_query from './components/resourcefile_form_query.vue';
import ResourceFile_form_data from './components/resourcefile_form_data.vue';
import resourcefile_form_upload from './components/resourcefile_form_upload.vue'
import { getViewType, type FormParam } from '@/types/commons/FormType';
import Farm2Request from '@/service/remoteRequests/Farm2Request';
import { RequestTypeEnum } from '@/types/commons/RequestTypeEnum';
import { ElMessageBox } from 'element-plus';
import farmRoutUtils from '@/hook/farmRoutUtils';
//---------------------------------------------------------
//路由信息：
//import resourcefile_page from "@/views/system/resourcefile/resourcefile_page.vue";
// {
//     path: "resourcefile",
//     name: "resourcefile_page",
//     component: resourcefile_page,
//     meta: { title: "附件" },
//   },
//---------------------------------------------------------
const eventBus = mitt();//事件总线
const registePageRef = ref();
const dataResult: DataResult = reactive(QueryUtils.getDefaultResult());//数据查询结果
const dataQuery: DataQuery = reactive({});//查询条件
const loadingFlag = ref(true);//加载中遮罩
const multipleSelection = ref([]);//当前选定行
const handleSelectionChange = (selection: []) => {
  multipleSelection.value = selection;//选定行事件
};
//---------------------------------------------------------
//---------------------------------------------------------

/**
 * 下载附件
 */
const downloadFile = (fileid: string) => {
  if (fileid) {
    farmRoutUtils.downloadFile(fileid)
    notice.byInfo("请等待附件下载完成");
  }
}

/**
 * 后台检索查询--HTTP(POST)
 * @param query
 */
const doDataQuery = async (query?: DataQuery) => {
  farm2Request.search('api/resourcefile/query', Object.assign(dataQuery, query), loadingFlag)
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
    'api/resourcefile/batch',
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
//const dataEditClick = (row: { ID: string }) => {
// eventBus.emit(PageEvent.data_form_open, { isOpen: true, type: <FormParam>{ type: getUpdateType(), id: row.ID, data: row } });
//}

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
 * 生成扩展文件
 * @param row
 */
const runWdap = () => {
  const selection: { ID: string; }[] = multipleSelection.value;
  const ids = selection.map(item => item.ID);
  ElMessageBox.confirm('当前选定' + ids.length + '条数据,确认重新生成扩展文件?', { icon: 'QuestionFilled' })
    .then(() => {
      Farm2Request.submit(
        'api/wdaptask/retask',
        RequestTypeEnum.post,
        null, { ids: ids }
      ).then(() => {
        notice.bySuccess("成功")
      }).catch((msg: string) => {
        notice.byError(msg);
      });
    }).catch(() => { });
}

/**
 * 注册多条-事件
 * @param row
 */
const datasSubmitClick = () => {
  const selection: { ID: string; }[] = multipleSelection.value;
  const ids = selection.map(item => item.ID);
  ElMessageBox.confirm('当前选定' + ids.length + '条数据,确认注册这些附件?', { icon: 'QuestionFilled' })
    .then(() => {
      Farm2Request.submit(
        'api/resourcefile/state',
        RequestTypeEnum.post,
        loadingFlag, { ids: ids, isSubmit: true }
      ).then(() => {
        doDataQuery();
      }).catch((msg: string) => {
        notice.byError(msg);
      });
    }).catch(() => { });
}

/**
 * 注销多条-事件
 * @param row
 */
const datasCancelClick = () => {
  const selection: { ID: string; }[] = multipleSelection.value;
  const ids = selection.map(item => item.ID);
  ElMessageBox.confirm('当前选定' + ids.length + '条数据,确认注销为临时状态?', { icon: 'QuestionFilled' })
    .then(() => {
      Farm2Request.submit(
        'api/resourcefile/state',
        RequestTypeEnum.post,
        loadingFlag, { ids: ids, isSubmit: false }
      ).then(() => {
        doDataQuery();
      }).catch((msg: string) => {
        notice.byError(msg);
      });
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
 * 打开注册信息
 * @param row 选定行数据
 */
const registeViewClick = (row: { ID: string }) => {
  registePageRef.value.openWin(row.ID);
}


/**
 * 打开数据创建窗口-事件
 */
//const openDataFormClick = () => {
// eventBus.emit(PageEvent.data_form_open, { isOpen: true, type: <FormParam>{ type: getCreatType() } });
//}


/**
 * 打开附件上传窗口-事件
 */
const openUploadFormClick = () => {
  eventBus.emit(MittKeysEnum.openUploadForm, { isOpen: true, type: <FormParam>{ type: { key: "creat", title: "上传附件" } } });
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
