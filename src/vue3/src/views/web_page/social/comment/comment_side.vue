<template><!--知识预览-->
  <el-drawer class="farm2-data-form" :destroy-on-close="true"
    style="padding: 0px; background-color: var(--el-farms-win-bg-color);" v-model="formCtrl.isDrawerOpen"
    :append-to-body="true" :with-header="false" size="400px">
    <div class="header" style="display: flex;">
      <el-icon style="color: #ff5e34;font-size: 32px;font-weight: 700;margin-right: 20px;">
        <ChatDotSquare />
      </el-icon>
      <span>评论</span>

      <div style="margin-left: auto;">
        <el-button @click="() => { formCtrl.isDrawerOpen = false }" plain icon="Close" circle />
      </div>
    </div>
    <div style="padding: 10px;padding-top: 0px;">
      <div ref="socialListRef" class="skc_social_list"
        style="width: 100%;  height: calc(100vh - 230px);overflow: auto;">
        <div class="skc_comment_node" v-for="data, index in comments.data" :key="index"
          :style="index != 0 ? '' : 'border:0px'">
          <div style="padding-right: 10px;">
            <web_user_photo :user-key="(data as Comment).CUSERKEY" :photo-id="(data as Comment).PHOTOID" :width="28">
            </web_user_photo>
          </div>
          <div style="width: 100%;">
            <div class="skc_user"> {{ (data as Comment).USERNAME }}</div>
            <div class="skc_time"> {{ farmViewUtils.formatDateTimeToNew((data as Comment).CTIME) }}</div>
            <div class="skc_comment"> {{ farmEmojiUtils.unescapeEmoji((data as Comment).CONTENT) }}</div>
            <div class="skc_buttons">
              <span class="skc_button" title="好评" @click="doUp((data as Comment).ID)">赞 {{ (data as Comment).UPNUM
              }}</span>
              <span class="skc_button" title="差评" @click="doDown((data as Comment).ID)">踩 {{ (data as Comment).DOWNNUM
              }}</span>

              <span v-if="commentForm.parentId == (data as Comment).ID" title="回复" class="skc_button_check"
                @click="reply((data as Comment).ID)"><el-icon>
                  <ChatRound />
                </el-icon>&nbsp;下方评论区回复，点击取消</span>
              <span v-if="commentForm.parentId != (data as Comment).ID" title="回复" class="skc_button"
                @click="reply((data as Comment).ID)"><el-icon>
                  <ChatRound />
                </el-icon></span>
            </div>
            <div>
              <!--子消息------------------------------------------------------->
              <div class="skc_comment_sub_node" v-for="subData, subindex in (data as Comment).CHILDRENS.data"
                :key="subindex">
                <div class="skc_user"> {{ (subData as Comment).USERNAME }}</div>
                <div class="skc_time"> {{ farmViewUtils.formatDateTimeToNew((subData as Comment).CTIME) }}</div>
                <div class="skc_comment"> {{ farmEmojiUtils.unescapeEmoji((subData as Comment).CONTENT) }}</div>
                <div class="skc_buttons">
                  <span class="skc_button" title="好评" @click="doUp((subData as Comment).ID)">赞 {{ (subData as
                    Comment).UPNUM
                  }}</span>
                  <span class="skc_button" title="差评" @click="doDown((subData as Comment).ID)">踩 {{ (subData as
                    Comment).DOWNNUM
                  }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div style="padding: 20px;" v-if="comments.totalSize <= 0">
          <Web_null_know tip="当前暂无评论信息"></Web_null_know>
        </div>
        <div style="padding: 20px;" v-if="comments.totalSize > 0">
          <el-pagination size="small" @current-change="handleCurrentChange" class="pagination" background
            :page-size="comments.pageSize" :page-count="comments.totalPage" :pager-count="5" layout="prev, pager, next"
            :total="comments.totalSize" />
        </div>
      </div>
      <div style="text-align: center;">
        <div style="display: flex; align-items: center; justify-content: space-between;">
          <div style="font-size: 12px;color:#ff5e34;padding-top: 10px;padding-left: 4px;">
            <span v-if="commentForm.parentId">回复评论</span>
            <span v-if="!commentForm.parentId">发表新评论</span>
          </div>
          <el-popover :disabled="!isLoginRef" class="box-item" style="font-size: 18px;margin-top: 10px;" width="200">
            <span class="skc_conmment_emoj" title="笑脸" @click="addEmoji('😀')">😀 </span>
            <span class="skc_conmment_emoj" title="心形" @click="addEmoji('😍')">😍 </span>
            <span class="skc_conmment_emoj" title="戴墨镜" @click="addEmoji('😎')">😎 </span>
            <span class="skc_conmment_emoj" title="思考" @click="addEmoji('🤔')">🤔 </span>
            <span class="skc_conmment_emoj" title="程序员" @click="addEmoji('👩‍💻')">👩‍💻 </span>
            <span class="skc_conmment_emoj" title="猫脸" @click="addEmoji('🐱')">🐱 </span>
            <span class="skc_conmment_emoj" title="狗脸" @click="addEmoji('🐶')">🐶 </span>
            <span class="skc_conmment_emoj" title="星星" @click="addEmoji('🌟')">🌟 </span>
            <span class="skc_conmment_emoj" title="庆祝" @click="addEmoji('🎉')">🎉 </span>
            <span class="skc_conmment_emoj" title="披萨" @click="addEmoji('🍕')">🍕 </span>
            <template #reference>
              <img src="\icon\expression.png" class="skc_buttong_img" />
            </template>
          </el-popover>
        </div>
        <el-input :disabled="!isLoginRef" v-model="commentForm.msg"
          style="width: 100%;border-radius: 18px;resize: none;" clearable maxlength="256"
          :autosize="{ minRows: 2, maxRows: 2 }" resize="none" type="textarea" placeholder="发评论..."
          :show-word-limit="true" />
        <el-button :disabled="!isLoginRef" v-loading="loadingFlag" type="primary" @click="submitComment"
          style="width: 90%;margin-top: 10px;" plain>发表评论</el-button>
      </div>
    </div>
  </el-drawer>
</template>
<script setup lang="ts">
import web_user_photo from '../../components/web_user_photo.vue';
import { onMounted, reactive, ref, type PropType } from 'vue';
import farm2Request from '@/service/remoteRequests/Farm2Request';
import notice from '@/components/msg/FarmNotice';
import { RequestTypeEnum } from '@/types/commons/RequestTypeEnum';
import type { DataResponse } from '@/types/commons/DataResponse';
import farmEmojiUtils from '@/hook/farmEmojiUtils';
import type { DataResult } from '@/types/commons/DataResult';
import farmViewUtils from '@/hook/farmViewUtils';
import { ElMessageBox } from 'element-plus';
import Web_null_know from '../../components/web_null_know.vue';
import { useUserInfoStore } from '@/store/useUserInfoStore';
import type { User } from '@/types/system/user';
const userInfo = useUserInfoStore();
const isLoginRef = ref(false);
const socialListRef = ref();
const props = defineProps({
  appId: {
    type: String,
    required: true
  },
  appModel: {
    type: String,
    required: true
  },
  numHandle: {
    type: Function as PropType<(num: number) => void>,
    required: false
  }
});
const loadingFlag = ref(false);//加载中遮罩
const commentForm = reactive({
  msg: '',
  appId: props.appId,
  appModel: props.appModel,
  parentId: '',
});
interface Comment {
  ID: string; // 根据实际数据结构调整字段
  CTIME: string;
  CONTENT: string;
  USERNAME: string;
  UPNUM: number;
  DOWNNUM: number;
  CUSERKEY: string;
  CHILDRENS: DataResult;
  PHOTOID: string;
  // 其他字段...
}
const comments: DataResult = reactive({ totalSize: 1, totalPage: 1, page: 1, pageSize: 1 })
//页面控制
const formCtrl = reactive({
  isDrawerOpen: false,//当前窗口是否打开
});

//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

const handleCurrentChange = (cpage: number) => {
  loadData(cpage, true);
};


const doUp = (id: string) => {
  if (!userInfo.isLogin) {
    notice.byInfo('请登录用户');
    return;
  }
  farm2Request
    .submit('api/social/commentup', RequestTypeEnum.post, loadingFlag, { id: id })
    .then(() => {
      notice.bySuccess("提交成功");
      loadData(comments.page, false)
    })
    .catch((msg: string) => {
      notice.byError(msg); // 报错
    });
}
const doDown = (id: string) => {
  if (!userInfo.isLogin) {
    notice.byInfo('请登录用户');
    return;
  }
  farm2Request
    .submit('api/social/commentdown', RequestTypeEnum.post, loadingFlag, { id: id })
    .then(() => {
      notice.bySuccess("提交成功");
      loadData(comments.page, false)
    })
    .catch((msg: string) => {
      notice.byError(msg); // 报错
    });
}
const reply = (id: string) => {
  if (!userInfo.isLogin) {
    notice.byInfo('请登录用户');
    return;
  }
  if (commentForm.parentId == id) {
    commentForm.parentId = '';
  } else {
    commentForm.parentId = id;
  }
}

//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
const addEmoji = (emoji: string) => {
  commentForm.msg = commentForm.msg + emoji;
}
//提交评论
const submitComment = () => {
  if (!userInfo.isLogin) {
    notice.byInfo('请登录用户');
    return;
  }
  if (farmEmojiUtils.escapeEmoji(commentForm.msg)) {
    ElMessageBox.confirm('确认发表评论', { icon: 'QuestionFilled' })
      .then(() => {
        //转义emoji表情
        commentForm.msg = farmEmojiUtils.escapeEmoji(commentForm.msg);
        farm2Request
          .submit('api/social/addComment', RequestTypeEnum.post, loadingFlag, commentForm)
          .then(() => {
            commentForm.msg = '';
            notice.bySuccess("评论成功发表!");
            if (commentForm.parentId) {
              loadData(1, false);
              commentForm.parentId = '';
            } else {
              loadData(1, true);
            }
          })
          .catch((msg: string) => {
            notice.byError(msg); // 报错
          });
      }).catch(() => { });
  }
}
const loadData = (cpage: number, isScroll: boolean) => {
  farm2Request
    .submit('api/social/comments', RequestTypeEnum.post, loadingFlag, { word: props.appId, page: cpage })
    .then((response: DataResponse) => {
      Object.assign(comments, response.data);
      if (isScroll) {
        socialListRef.value.scrollTop = 0;
      }
    })
    .catch((msg: string) => {
      notice.byError(msg); // 报错
    });
}

const loadNum = () => {
  farm2Request
    .submit('api/social/commentNum', RequestTypeEnum.post, loadingFlag, { id: props.appId })
    .then((response: DataResponse) => {
      if (props.numHandle) {
        props.numHandle(response.data as number);
      }
    })
    .catch((msg: string) => {
      notice.byError(msg); // 报错
    });
}

/**
 * 出口演示函数
 * 该函数用于展示导出功能的示例，具体实现细节根据需求而定
 */
const openWin = (isOpen: boolean) => {
  formCtrl.isDrawerOpen = isOpen;
  loadData(1, false);
}
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

onMounted(() => {
  userInfo.getUser().then((user: User) => {
    if (user.loginname) {
      isLoginRef.value = true;
    } else {
      isLoginRef.value = false;
    }
  });
  loadNum();
});
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

.skc_social_list {
  background-color: #ffffff;
  border-radius: 10px;
}

.skc_buttong_img {
  width: 22px;
  height: 22px;
  margin-top: 10px;
  margin-right: 20px;
  float: right;
  margin-bottom: 4px;
  cursor: pointer;
  opacity: 1;
}

.skc_buttong_img:hover {
  opacity: 0.8;
}

.skc_conmment_emoj {
  font-size: 18px;
  cursor: pointer;
}

.skc_conmment_emoj:hover {
  opacity: 0.5;
}



.skc_comment_node {
  display: flex;
  padding: 20px;
  padding-bottom: 20px;
  border-top: 2px solid var(--el-farms-win-bg-color);

  .skc_user {
    font-size: 12px;
    font-weight: 500;
    color: #666666;
  }

  .skc_time {
    font-size: 12px;
    font-weight: 500;
    color: #999999;
  }

  .skc_comment {
    font-size: 14px;
    padding-top: 10px;
    padding-bottom: 10px;
    white-space: pre-wrap;
    /* 保留空格和换行符，并支持自动换行 */
    word-wrap: break-word;
    /* 确保长单词也能换行 */
  }

  .skc_buttons {
    font-size: 12px;
    font-weight: 500;
    color: #999999;

    .skc_button {
      font-size: 12px;
      font-weight: 200;
      color: #555555;
      margin-right: 20px;
      cursor: pointer;
    }

    .skc_button:hover {
      color: #aaaaaa;
    }

    .skc_button_check {
      font-size: 12px;
      font-weight: 500;
      color: chocolate;
      margin-right: 20px;
      cursor: pointer;
    }

    .skc_button_check:hover {
      color: #000000;
    }
  }
}


.skc_comment_sub_node {
  background-color: var(--el-color-warning-light-9);
  border-radius: 8px;
  border: 1px solid var(--el-color-warning-light-5);
  padding: 10px;
  margin-top: 10px;

  .skc_comment {
    color: #666666;
    font-size: 14px;
    padding-top: 4px;
    padding-bottom: 4px;
    white-space: pre-wrap;
    /* 保留空格和换行符，并支持自动换行 */
    word-wrap: break-word;
    /* 确保长单词也能换行 */
  }
}
</style>
