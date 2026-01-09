<template>
  <span style="display: none;"></span>
</template>
<script setup lang="ts">
import Farm2Request from '@/service/remoteRequests/Farm2Request';
import { RequestTypeEnum } from '@/types/commons/RequestTypeEnum';
import { useUserInfoStore } from '@/store/useUserInfoStore';
import { ElMessageBox } from 'element-plus';
import notice from '@/components/msg/FarmNotice';
import { useRouter } from 'vue-router';
const userInfo = useUserInfoStore();
const router = useRouter();
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
const goToLogin = () => {
  ElMessageBox.confirm('是否立即退出系统?')
    .then(() => {
      Farm2Request.submit("api/login/out", RequestTypeEnum.get, null, {}).then(() => {
        userInfo.logout();
        router.push('/login');
      }).catch((msg: Error) => {
        notice.byError(msg.message);
        userInfo.logout();
      });
    }).catch(() => {
      // 取消
    })
}
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
defineExpose({
  goToLogin
});
</script>
<style scoped>
/* 样式可以根据需要添加 */
</style>
