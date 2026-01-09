<template>
  <el-dialog v-model="dialogVisibleRef" title="个人信息修改" width="380">
    <el-row>
      <el-col :span="24" style="padding-top: 10px;padding-left: 20px;">
        <el-form :model="form" label-width="auto" ref="ruleFormRef">
          <el-form-item label="姓名：" prop="name" :rules="v.getRules(true, 2, 8)" style="max-width:290px">
            <el-input v-model="form.name" />
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>
    <el-divider border-style="dashed">
      <el-icon>
        <Goods />
      </el-icon>
    </el-divider>
    <el-row>
      <el-col :span="6" style="text-align: center;padding-top: 6px;">
        <img style="height: 48px;width: 48px;cursor: pointer; border-radius:24px; "
          :src="'/images/photo/photo' + (form.photoid ? form.photoid : 6) + '.png'" />
      </el-col>
      <el-col :span="18"> <el-row>
          <el-col v-for="i in 16" :key="i" :span="6" style="text-align: center;margin-top: 8px;">
            <img class="skc_photos" style="height: 28px;width: 28px;cursor: pointer;border-radius:24px; "
              @click="form.photoid = i.toString()" :src="'/images/photo/photo' + i + '.png'" />
          </el-col>
        </el-row>
      </el-col>
    </el-row>
    <template #footer>
      <div class="dialog-footer" style="text-align: center;margin-top: 20px;margin-bottom: 20px;">
        <el-button v-loading="loadingFlag" style="width: 80%;" type="primary" @click="submitForm">
          提交
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>
<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue';
import v from '@/hook/farmFormValids';
import { useUserInfoStore } from '@/store/useUserInfoStore';
import type { User } from '@/types/system/user';
import farm2Request from '@/service/remoteRequests/Farm2Request';
import notice from '@/components/msg/FarmNotice';
import { RequestTypeEnum } from '@/types/commons/RequestTypeEnum';
import farmRoutUtils from '@/hook/farmRoutUtils';
import { ElMessageBox } from 'element-plus';
const userInfo = useUserInfoStore();
const loadingFlag = ref(false);//加载中遮罩
const form = reactive({
  name: '', photoid: ''
});
const ruleFormRef = ref()
const dialogVisibleRef = ref(false);
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

const submitForm = () => {
  ruleFormRef.value.validate((valid: boolean) => {
    if (valid) {
      ElMessageBox.confirm('是否立即更新?', { icon: 'QuestionFilled' })
        .then(() => {
          farm2Request
            .submit('api/current/eidt', RequestTypeEnum.post, loadingFlag, form)
            .then(() => {
              userInfo.clearUser();
              userInfo.setUser(form.name, form.photoid);
              farmRoutUtils.reLoad();
            })
            .catch((msg: string) => {
              notice.byError(msg); // 报错
            });
        }).catch(() => { });
    }
  });
}



// 定义一个方法
const openWin = () => {
  dialogVisibleRef.value = true;
};
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

onMounted(() => {
  userInfo.getUser().then((user: User) => {
    form.name = user.name!;
    form.photoid = user.photoid!;
  });
})

// 暴露方法给父组件
defineExpose({
  openWin,
});
</script>
<style scoped>
/* 样式可以根据需要添加 */

.skc_photos {
  opacity: 0.5;
}

.skc_photos:hover {
  opacity: 1;
}
</style>
