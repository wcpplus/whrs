<template>
  <div style="padding: 10px 30px;padding-bottom: 0px;">
    <div style="background-color: #ffffff;padding: 10px;border-radius: 20px;">
      <el-descriptions :border="true">
        <el-descriptions-item :rowspan="4" :width="140" label="个人信息" :align="'center'">
          <img class="skc-photo" @click="myInfoRef.openWin(true)"
            :src="'/images/photo/photo' + (currentUser.LocalUser.photoid ? currentUser.LocalUser.photoid : '6') + '.png'"
            style="padding-right: 4px;margin-top: 0px;margin-left: 8px;" alt="Logo" />
        </el-descriptions-item>
        <el-descriptions-item label="部门">{{ currentUser.LocalOrg.note }}</el-descriptions-item>
        <el-descriptions-item label="状态">{{ currentUser.LocalUser.state }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ currentUser.Userinfo.phonecode }}</el-descriptions-item>
        <el-descriptions-item label="入职日期">{{ currentUser.Userinfo.emptime
          ? farmTimeUtils.formatDate8Stype2(currentUser.Userinfo.emptime || '') : '' }}
        </el-descriptions-item>
        <el-descriptions-item label="生日">{{ currentUser.Userinfo.birthdate ?
          farmTimeUtils.formatDate8Stype2(currentUser.Userinfo.birthdate || '') : '' }}
        </el-descriptions-item>
        <el-descriptions-item label="银行卡号">  <span v-if="currentUser.Userinfo.pan">**************</span>
        </el-descriptions-item>
        <el-descriptions-item label="劳动合同">
          <div class="whrs-title" v-if="currentUser.ContractfFile.id"
            @click="viewWin.openWin(currentUser.ContractfFile.id, true);"> <el-button link type="primary"
              icon="Search">{{
                currentUser.ContractfFile.title }}</el-button> </div>
        </el-descriptions-item>
        <el-descriptions-item label="学历证书">
          <div class="whrs-title" v-if="currentUser.EqfFile.id" @click="viewWin.openWin(currentUser.EqfFile.id, true);">
            <el-button link type="primary" icon="Search">{{
              currentUser.EqfFile.title }}</el-button>
          </div>
        </el-descriptions-item>
        <el-descriptions-item label="资料完善度" :align="'center'">
          <el-progress :percentage="currentUser.Userinfo.process" />
        </el-descriptions-item>
      </el-descriptions>
    </div>
  </div>
  <File_view_win ref="viewWin"></File_view_win>
  <My_info_setting ref="myInfoRef"></My_info_setting>
</template>
<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue';
import { useUserInfoStore } from "@/store/useUserInfoStore";
import My_info_setting from '@/views/web_page/user/my_info_setting.vue';
import type { user } from '@/views/system/localuser/utils/user';
import type { LocalUserInfo } from '@/views/system/localuserinfo/utils/localuserinfo';
import type { LocalOrg } from '@/views/system/localorg/utils/localorg';
import farmClientUtils from '@/hook/farmClientUtils';
import farmTimeUtils from '@/hook/farmTimeUtils';
import File_view_win from '@/views/back_console/wdap/file_view_win.vue';
import type { ResourceFile } from '@/views/system/resourcefile/utils/resourcefile';
const viewWin = ref();//预览窗口
const myInfoRef = ref();
const userInfo = useUserInfoStore();
const menusPops = reactive({
  userMng: false,
  salaryMng: false,
  orgMng: false,
  attendanceMng: false,
});
const currentUser = reactive({
  LocalUser: {} as user,
  Userinfo: {} as LocalUserInfo,
  LocalOrg: {} as LocalOrg,
  ContractfFile: {} as ResourceFile,
  EqfFile: {} as ResourceFile,
});

onMounted(() => {
  farmClientUtils.postObject('api/localuser/myinfo', {}, (obj: object) => {
    Object.assign(currentUser, obj);
  });
  userInfo.containAppMenu('/system/localuser', (isContain: boolean) => {
    menusPops.userMng = isContain;
  });
  userInfo.containAppMenu('/system/salaryuser', (isContain: boolean) => {
    menusPops.salaryMng = isContain;
  });
  userInfo.containAppMenu('/system/localorg', (isContain: boolean) => {
    menusPops.orgMng = isContain;
  });
  userInfo.containAppMenu('/system/attendancesummary', (isContain: boolean) => {
    menusPops.attendanceMng = isContain;
  });
})

</script>
<style scoped>
.skc-photo {
  cursor: pointer;
}

.skc-photo:hover {
  opacity: 0.8;
}
</style>
