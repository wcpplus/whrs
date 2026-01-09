<template>
  <div style="padding: 10px;padding-top: 20px;">
    <User_point_info :show-shop="true"></User_point_info>
    <div v-loading="loadingFlag"
      style="border-radius: 20px;overflow: hidden;margin-top: 20px;padding: 20px 0px;background-color: #ffffff;">
      <el-menu :default-active="activePath">
        <el-menu-item index="/my/home" @click="farmRoutUtils.routToPage(router, '/my/home')">
          <div>
            <el-icon>
              <Avatar />
            </el-icon>
            <span>我的空间</span>
          </div>
        </el-menu-item>
        <el-menu-item index="/my/know" @click="farmRoutUtils.routToPage(router, '/my/know')">
          <div>
            <el-icon>
              <Collection />
            </el-icon>
            <span>我的知识</span>
          </div>
          <div class="num-tip" v-if="state.MY_KNOW > 0">{{ state.MY_KNOW }}</div>
        </el-menu-item>
        <el-menu-item index="/my/tag" @click="farmRoutUtils.routToPage(router, '/my/tag')">
          <div>
            <el-icon>
              <img src="/icon/tag.png" />
            </el-icon>
            <span>收藏标签</span>
          </div>
          <div class="num-tip" v-if="state.MY_JOIN_TAG > 0">{{ state.MY_JOIN_TAG }}</div>
        </el-menu-item>
        <el-menu-item index="/my/audit" @click="farmRoutUtils.routToPage(router, '/my/audit')">
          <div>
            <el-icon>
              <img src="/icon/audit.png" />
            </el-icon>
            <span>审核知识</span>
          </div>
          <div class="num-tip" style="font-weight: 700;" v-if="state.AUDIT_KNOW > 0">
            <el-tag type="danger" round effect="dark" size="small">
              {{ state.AUDIT_KNOW }}
            </el-tag>
          </div>
        </el-menu-item>
        <el-menu-item index="/my/joinknow" @click="farmRoutUtils.routToPage(router, '/my/joinknow')">
          <div>
            <el-icon>
              <img src="/icon/collect3.png" />
            </el-icon>
            <span>收藏知识</span>
          </div>
          <div class="num-tip" v-if="state.MY_JOIN_KNOW > 0">{{ state.MY_JOIN_KNOW }}</div>
        </el-menu-item>
        <el-menu-item index="/my/fqa" @click="farmRoutUtils.routToPage(router, '/my/fqa')">
          <div>
            <el-icon>
              <img style="opacity: 0.7;" src="/icon/fqa.png" />
            </el-icon>
            <span>我的问答</span>
          </div>
          <div class="num-tip" v-if="state.MY_FQA > 0">{{ state.MY_FQA }}</div>
        </el-menu-item>
        <hr style="color: #aaaaaa;margin: 8px 0px;" v-if="showColumn.course" />
        <el-menu-item index="/my/course/mng" v-if="state.LMS_MNG_CATGORY > 0 && showColumn.course"
          @click="farmRoutUtils.routToPage(router, '/my/course/mng')">
          <div>
            <el-icon>
              <img src="/icon/course.png" />
            </el-icon>
            <span>课程发布</span>
          </div>
          <div class="num-tip">{{ state.LMS_MNG_CATGORY }}板块</div>
        </el-menu-item>

        <el-menu-item index="/my/course/learn" v-if="showColumn.course"
          @click="farmRoutUtils.routToPage(router, '/my/course/learn')">
          <div>
            <el-icon>
              <img src="/icon/course.png" />
            </el-icon>
            <span>我的课程</span>
          </div>
          <div class="num-tip">{{ state.LMS_LEARN_COURSE }}</div>
        </el-menu-item>
        <el-menu-item index="/my/course/join" v-if="showColumn.course"
          @click="farmRoutUtils.routToPage(router, '/my/course/join')">
          <div>
            <el-icon>
              <img src="/icon/course.png" />
            </el-icon>
            <span>课程收藏</span>
          </div>
          <div class="num-tip">{{ state.LMS_JOIN }}</div>
        </el-menu-item>
        <el-menu-item index="/my/course/plan" v-if="state.WTS_PLAN_CATGORY > 0 && showColumn.course"
          @click="farmRoutUtils.routToPage(router, '/my/course/plan')">
          <div>
            <el-icon>
              <School />
            </el-icon>
            <span>培训发布</span>
          </div>
          <div class="num-tip">{{ state.WTS_PLAN_CATGORY }}板块</div>
        </el-menu-item>
        <el-menu-item index="/my/course/myplan" v-if="showColumn.course"
          @click="farmRoutUtils.routToPage(router, '/my/course/myplan')">
          <div>
            <el-icon>
              <School />
            </el-icon>
            <span>我的培训</span>
          </div>
          <div class="num-tip">{{ state.LMS_PLAN }}</div>
        </el-menu-item>
        <hr style="color: #aaaaaa;margin: 8px 0px;" />
        <el-menu-item index="/wtsmng/room" v-if="wtsCategoryNums.WTS_MNG > 0 && showColumn.wts"
          @click="farmRoutUtils.routToPage(router, '/wtsmng/room')">
          <div>
            <el-icon>
              <img src="/icon/exam.png" />
            </el-icon>
            <span>答题管理</span>
          </div>
          <div class="num-tip"><b>活动发布</b></div>
        </el-menu-item>
        <el-menu-item index="/my/wts/gradings" v-if="wtsCategoryNums.WTS_GRADING > 0 && showColumn.wts"
          @click="farmRoutUtils.routToPage(router, '/my/wts/gradings')">
          <div>
            <el-icon>
              <img src="/icon/exam.png" />
            </el-icon>
            <span>阅卷管理</span>
          </div>
          <div class="num-tip"><b>考试阅卷</b></div>
        </el-menu-item>
        <el-menu-item index="/my/wts/cards" v-if="showColumn.wts"
          @click="farmRoutUtils.routToPage(router, '/my/wts/cards')">
          <div>
            <el-icon>
              <img src="/icon/exam.png" />
            </el-icon>
            <span>我的答题</span>
          </div>
          <div class="num-tip"><b>{{ wtsCategoryNums.WTS_CARD }}</b></div>
        </el-menu-item>
      </el-menu>
    </div>
  </div>
</template>
<script setup lang="ts">
import farmRoutUtils from '@/hook/farmRoutUtils';
const router = useRouter()
import { useRouter } from 'vue-router';
import { computed, onMounted, reactive, ref } from 'vue';
import Farm2Request from '@/service/remoteRequests/Farm2Request';
import { RequestTypeEnum } from '@/types/commons/RequestTypeEnum';
import type { DataResponse } from '@/types/commons/DataResponse';
import notice from '@/components/msg/FarmNotice';
import farmClientUtils from '@/hook/farmClientUtils';
import farmConfig from '@/hook/farmConfig';
import { useRoute } from 'vue-router';
const route = useRoute();
const activePath = computed(() => route.path);
const loadingFlag = ref(false);
/**
 * 答题系统相关菜单数量
 */
const wtsCategoryNums = reactive({
  WTS_GRADING: 0,
  WTS_MNG: 0,
  WTS_CARD: 0,
});

const showColumn = reactive({
  course: false,
  wts: false,
});
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
const state = reactive({
  MY_KNOW: 0,
  MY_JOIN_TAG: 0,
  AUDIT_KNOW: 0,
  MY_JOIN_KNOW: 0,
  MY_FQA: 0,
  LMS_MNG_CATGORY: 0,
  WTS_PLAN_CATGORY: 0,
  LMS_JOIN: 0,
  LMS_LEARN_COURSE: 0,
  LMS_PLAN: 0,
});
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
const loadCategoryNums = () => {
  farmClientUtils.postObject('api/wtsHome/catgoryNums',
    {}, (obj: object) => {
      Object.assign(wtsCategoryNums, obj);
    }, loadingFlag);
}
onMounted(() => {
  Farm2Request
    .submit('api/myknow/myNums', RequestTypeEnum.post, loadingFlag)
    .then((response: DataResponse) => {
      Object.assign(state, response.data);
      loadCategoryNums();
    })
    .catch((msg: string) => {
      notice.byError(msg); // 报错
    });

  farmConfig.loadPara('farm2.config.column.head.show.course', (val) => {
    showColumn.course = val as boolean;
  })
  farmConfig.loadPara('farm2.config.column.head.show.wts', (val) => {
    showColumn.wts = val as boolean;
  })
})
</script>
<style scoped>
/* 样式可以根据需要添加 */
.el-menu {
  border-right: 1px solid #ffffff;

  img {
    width: 18px;
    height: 18px;
  }

  .el-icon {
    color: darkorange;
  }

  .num-tip {
    float: right;
    font-weight: 200;
    font-size: 14px;
    margin-left: auto;
    color: var(--bs-orange);
  }
}

.el-menu-item {
  display: flex;
}

.el-menu :deep(.el-sub-menu__title) {
  color: #666666;
  font-weight: 700;
}

.el-menu :deep(.el-menu-item) {
  color: #666666;
  font-size: 15px;
  line-height: 42px;
  height: 42px;
  cursor: pointer;
}

.el-menu :deep(.el-menu-item.is-active) {
  color: #666666;
  font-weight: 700;
  background-color: #f4f4f4;
}
</style>
