<template>
  <el-col :lg="6" :sm="12" :xs="12">
    <div class="farm2-block-box" style="overflow: hidden;">
      <el-row>
        <el-col :span="9" style="text-align: center;">
          <el-avatar :class="'farm2_home_row_icon ' + props.colorType"
            :src="'/images/photo/photo' + (cuser.photoid ? cuser.photoid : 6) + '.png'" :icon="props.icon" />
        </el-col>
        <el-col :span="15" style="text-align: left;padding-left: 8px;padding-top: 10px;">
          <div style="color: #7c8db5;">{{ userInfoStore.$state.user.name }} </div>
          <div style="font-size: 12px;">{{ userInfoStore.$state.user.loginname }}</div>
          <div class="farm2_user_tags" style="font-weight: 700;font-size: 12px;text-align: left;">
            <el-tag v-for="tag in userInfoStore.$state.user.tags" :key="tag" class="tag" round type="success">
              {{ tag }}
            </el-tag>
          </div>
        </el-col>
      </el-row>
    </div>
  </el-col>
</template>
<script setup lang="ts">
import { useUserInfoStore } from '@/store/useUserInfoStore';
import type { User } from '@/types/system/user';
import { onMounted, reactive } from 'vue';
const userInfoStore = useUserInfoStore();
const userInfo = useUserInfoStore();
const cuser: User = reactive({});
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
const props = defineProps({
  colorType: {
    type: String,
    required: true
  },
  icon: {
    type: String,
    required: true
  },
  title: {
    type: String,
    required: true
  },
})
onMounted(() => {
  userInfo.getUser().then((user: User) => {
    Object.assign(cuser, user)
  });
})

//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
</script>
<style scoped>
.farm2-block-box {
  background-color: #ffffff;
  text-align: center;
  border-radius: 1em;
  margin-left: 20px;
  margin-bottom: 20px;
  padding: 10px;
  padding-left: 20px;
  padding-right: 10px;
}

.farm2_home_row_icon {
  height: 48px;
  width: 48px;
  font-size: 28px;
  margin-top: 16px;
}

.farm2_user_tags .tag {
  margin-left: 0px;
  margin-top: 4px;
}
</style>
