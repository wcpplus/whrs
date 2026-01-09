<template>


  <el-drawer class="farm2-data-form" :destroy-on-close="true"
    style="padding: 0px; max-width: 90%;background-color: var(--el-farms-win-bg-color);" v-model="winOpenIsRef"
    :append-to-body="true" :with-header="false" size="400px">
    <template #default>
      <div class="header" style="display: flex;">
        <el-icon style="color: #ff5e34;font-size: 32px;font-weight: 700;margin-right: 20px;">
          <Message />
        </el-icon>
        <span>我的消息</span>
        <div style="margin-left: auto;">
          <el-button @click="() => { winOpenIsRef = false; }" plain icon="Close" circle />
        </div>
      </div>
      <div style="padding: 20px; padding-top: 0px;padding-bottom: 0px;">
        <el-input v-model="form.word" style="max-width: 250px" placeholder="请输入检索词..." class="input-with-select">
          <template #append>
            <el-button icon="Search" @click="doSearch" />
          </template>
        </el-input> <el-button @click="readAll" style="margin-bottom:20px" type="warning">全部已读</el-button>
      </div>
      <div class="skc_content" v-loading="loadingFlag">
        <div class="skc_message_node" v-for="data, index in messages.data" :key="index"
          :style="index != 0 ? '' : 'border:0px'">
          <div @click="readMsg((data as Messages).ID)"
            :class="(data as Messages).READSTATE == '0' ? 'skc_title' : 'skc_title skc_no_read'"> {{
              (data as
                Messages).TITLE }}</div>
          <div class="skc_info">
            <span class="skc_unit"> {{ farmViewUtils.formatDateTimeToNew((data as Messages).CTIME) }}</span>
            <span class="skc_unit"> {{ (data as Messages).USERNAME }}</span>
            <el-button size="small" @click="delMessage((data as Messages).ID)" style="float: right;" type="danger"
              icon="Delete" circle />
          </div>
        </div>
        <Web_null_know v-if="messages.totalSize <= 0" tip="未查询到消息"></Web_null_know>
        <div style="padding: 20px;" v-if="messages.totalSize > 0">
          <el-pagination size="small" @current-change="handleCurrentChange" class="pagination" background
            :page-size="messages.pageSize" :page-count="messages.totalPage" :pager-count="5" layout="prev, pager, next"
            :total="messages.totalSize" />
        </div>
      </div>
    </template>
  </el-drawer>
  <el-dialog v-model="msgVisibleRef" width="700" style="max-width: 90%;background-color: var(--el-farms-win-bg-color);">
    <div style=" padding: 10px; font-weight: 700;font-size: 14px;margin-left: 20px;margin-bottom: 10px;">
      <el-icon style="margin-right: 8px;top:4px;font-size: 18px;color:var(--el-color-primary)">
        <Message />
      </el-icon>{{ message.title }}<el-button style="float: right;margin-right: 8px;" type="danger"
        @click="delMessage(message.id)" size="small" icon="Delete" circle />
    </div>
    <div class="skc_content" style="white-space: pre-wrap;min-height: 100px; padding: 10px;padding-bottom: 20px;padding-top: 20px; color: #666666;
      word-wrap: break-word;">{{ message.content.replace('[AUDIT_TASK]', '') }}
      <el-button style="margin-left: 20px; margin-top: -4px;" v-if="message.content.indexOf('[AUDIT_TASK]') >= 0"
        type="primary" @click="gotoAudit" size="small" round>
        进入我的审核知识
      </el-button>
    </div>
  </el-dialog>
</template>
<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue';
import farm2Request from '@/service/remoteRequests/Farm2Request';
import notice from '@/components/msg/FarmNotice';
import { RequestTypeEnum } from '@/types/commons/RequestTypeEnum';
import type { DataResponse } from '@/types/commons/DataResponse';
import type { DataResult } from '@/types/commons/DataResult';
import farmViewUtils from '@/hook/farmViewUtils';
import Web_null_know from '../../components/web_null_know.vue';
import { ElMessageBox } from 'element-plus';
import { useUserInfoStore } from '@/store/useUserInfoStore';
import type { User } from '@/types/system/user';
import farmRoutUtils from '@/hook/farmRoutUtils';
const useUserInfo = useUserInfoStore();
const winOpenIsRef = ref(false);
const loadingFlag = ref(false);
const msgVisibleRef = ref(false);
const page = ref(1);
const form = reactive({
  word: ''
});
const message = reactive({
  title: '',
  content: '',
  id: '',
});

interface Messages {
  ID: string;
  TITLE: string;
  CTIME: string;
  READSTATE: string;
  USERNAME: string;
}
const messages: DataResult = reactive({ totalSize: 1, totalPage: 1, page: 1, pageSize: 1 })
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
const openWin = (isOpen: boolean) => {
  winOpenIsRef.value = isOpen;
}

const gotoAudit = () => {
  farmRoutUtils.gotoPage("/my/audit");
}

const handleCurrentChange = (cpage: number) => {
  page.value = cpage;
  loadMyMessages();
};
/**
 * 读取消息
 * @param cpage
 */
const readMsg = (id: string) => {
  farm2Request
    .submit('api/wmsg/read', RequestTypeEnum.post, loadingFlag, { id: id })
    .then((response: DataResponse) => {
      Object.assign(message, response.data);
      msgVisibleRef.value = true;
    })
    .catch((msg: string) => {
      notice.byError(msg); // 报错
    });
};
const doSearch = () => {
  loadMyMessages();
};
const delMessage = (id: string) => {
  ElMessageBox.confirm('确认删除该消息?', { icon: 'QuestionFilled' })
    .then(() => {
      farm2Request
        .submit('api/wmsg/del', RequestTypeEnum.post, loadingFlag, { id: id })
        .then(() => {
          Object.assign(message, {
            title: '',
            content: '',
            id: '',
          });
          loadMyMessages();
          msgVisibleRef.value = false;
        })
        .catch((msg: string) => {
          notice.byError(msg); // 报错
        });
    }).catch(() => { });
};
/**
 * 全部已读
 */
const readAll = () => {
  farm2Request
    .submit('api/wmsg/readall', RequestTypeEnum.post, loadingFlag)
    .then(() => {
      page.value = 1;
      loadMyMessages();
      notice.bySuccess("全部消息已读")
    })
    .catch((msg: string) => {
      notice.byError(msg); // 报错
    });
};

const loadMyMessages = () => {
  farm2Request
    .submit('api/wmsg/msg', RequestTypeEnum.post, loadingFlag, { page: page.value, rules: [{ key: 'TITLE', value: form.word, compara: 'like' }] })
    .then((response: DataResponse) => {
      Object.assign(messages, response.data);
    })
    .catch((msg: string) => {
      notice.byError(msg); // 报错
    });
}
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
onMounted(() => {
  useUserInfo.getUser().then((user: User) => {
    if (user.loginname) {
      loadMyMessages();
    }
  });
})
// 暴露方法给父组件
defineExpose({
  openWin,
});
</script>
<style scoped>
/* 样式可以根据需要添加 */
.header {
  padding: 20px;
  font-size: 18px;
  display: flex;
  align-items: center;
  color: #555555;
  font-weight: 700;

  img {
    height: 32px;
    width: 32px;
    margin-right: 20px;
  }
}

.skc_content {
  background-color: #ffffff;
  border-radius: 10px;
  margin: 10px;
  margin-top: 0px;
  max-height: calc(100vh - 150px);
  overflow: auto;
  line-height: 2em;
}

.skc_message_node {
  padding: 20px;
  padding-bottom: 20px;
  border-top: 2px solid var(--el-farms-win-bg-color);

  .skc_title {
    color: #444444;
    font-size: 14px;
    cursor: pointer;
    font-weight: 700;
  }

  .skc_title.skc_no_read {
    color: #999999;
    font-weight: 500;
  }

  .skc_title:hover {}

  .skc_info {
    margin-top: 4px;
    font-size: 12px;
    color: #999999;

    .skc_unit {
      margin-right: 8px;
    }
  }
}
</style>
