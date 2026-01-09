<template>
  <el-dialog v-model="dialogVisibleRef" :title="formTitlelRef" width="800" destroy-on-close>
    <el-form :model="userForm" label-width="auto" ref="ruleFormRef">
      <el-row>
        <el-col :span="12" v-if="['RZ', 'ZZ', 'DD', 'LZ'].includes(formModelRef)">
          <el-form-item label="名称：" prop="name" :rules="v.getRules(true, 2, 8)">
            <el-input disabled v-model="userForm.name" />
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="['RZ', 'ZZ', 'DD'].includes(formModelRef)">
          <el-form-item label="组织机构：" prop="orgid" :rules="v.getRules(true)">
            <el-tree-select :check-strictly='true' v-model="userForm.orgid" lazy clearable
              :load="farmUserFlowUtils.loadOrgidTreeNode" :props="farmUserFlowUtils.orgidProps"
              :default-expanded-keys="['NONE', ...localOrgPath]" />
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="['RZ', 'ZZ', 'DD'].includes(formModelRef)"><el-form-item label="用户岗位：" prop="post">
            <div
              style="display: flex;min-height: 30px; width: 100%;border:1px solid #d4d5da;padding: 1px 10px;border-radius: 4px;"
              :style="{ backgroundColor: '#ffffff' }">
              <div style="flex:1">
                <el-tag size="small" v-for="tag in userPosts" :key="tag.keyid" :closable="true" type="success" @close="() => {
                  const index = userPosts.findIndex(item => item.keyid === tag.keyid);
                  if (index !== -1) {
                    userPosts.splice(index, 1);
                  }
                }">
                  {{ tag.name }}
                </el-tag>
              </div>
              <div style="margin-left: auto;"> <el-button size="small" circle type="success"
                  @click="postChooseRef.openWin(true)" icon="Search" /></div>
            </div>
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="['RZ', 'ZZ', 'DD'].includes(formModelRef)">
          <el-form-item label="职级:" prop="gradeid">
            <el-select v-model="userForm.gradeid" placeholder="可检索职级" filterable clearable>
              <el-option v-for="item in grades" :key="item.id" :label="item.keyid + ':' + item.name" :value="item.id" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="['RZ'].includes(formModelRef)">
          <el-form-item label="入职时间:" prop="emptime" :rules="v.getRules(false, 0, 16)">
            <el-date-picker v-model="infoForm.emptime" type="date" value-format="YYYYMMDD" placeholder="选择日期"
              style="width: 100%;" />
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="['RZ'].includes(formModelRef)">
          <el-form-item label="电子邮箱:" prop="email" :rules="v.getRegExpRules(v.ValidRxs.email, false)">
            <el-input v-model="infoForm.email" />
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="['RZ'].includes(formModelRef)">
          <el-form-item label="身份证:" prop="idcode" :rules="v.getRegExpRules(v.ValidRxs.idCard, false)">
            <el-input v-model="infoForm.idcode" />
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="['RZ'].includes(formModelRef)">
          <el-form-item label="银行卡号:" prop="pan" :rules="v.getRegExpRules(v.ValidRxs.bankCard, false)">
            <el-input v-model="infoForm.pan" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12" v-if="['RZ'].includes(formModelRef)"><el-form-item label="体检报告ID:" prop="merfid">
            <div v-if="userFiles.merf.id" class="whrs-file-box">
              <div class="whrs-title" @click="viewWin.openWin(userFiles.merf.id, true);">{{
                userFiles.merf.title }}</div><el-button type="danger" size="small" icon="Delete" circle @click="() => {
                  userFiles.merf = {} as ResourceFile;
                }" style="margin-left: 8px;" />
            </div>
            <div v-if="!userFiles.merf.id">
              <file_upload_box :max-size="1024 * 1024 * 10" model="FILE" :successHandle="(rfile: Object) => {
                const fileInof = rfile as { data: ResourceFile }
                Object.assign(userFiles.merf, fileInof.data);
              }" :error-handle="(e: { message: string }) => {
                FarmNotice.byError(e.message);
              }" :multiple="false"></file_upload_box>
            </div>
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="['RZ'].includes(formModelRef)"><el-form-item label="身份证复印件ID:" prop="icardfid">
            <div v-if="userFiles.icardf.id" class="whrs-file-box">
              <div class="whrs-title" @click="viewWin.openWin(userFiles.icardf.id, true);">{{
                userFiles.icardf.title }}</div><el-button type="danger" size="small" icon="Delete" circle @click="() => {
                  userFiles.icardf = {} as ResourceFile;
                }" style="margin-left: 8px;" />
            </div>
            <div v-if="!userFiles.icardf.id">
              <file_upload_box :max-size="1024 * 1024 * 10" model="FILE" :successHandle="(rfile: Object) => {
                const fileInof = rfile as { data: ResourceFile }
                Object.assign(userFiles.icardf, fileInof.data);
              }" :error-handle="(e: { message: string }) => {
                FarmNotice.byError(e.message);
              }" :multiple="false"></file_upload_box>
            </div>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12" v-if="['RZ'].includes(formModelRef)"> <el-form-item label="劳动合同ID:" prop="contractfid">
            <div v-if="userFiles.contractf.id" class="whrs-file-box">
              <div class="whrs-title" @click="viewWin.openWin(userFiles.contractf.id, true);"> {{
                userFiles.contractf.title }}</div>
              <el-button type="danger" size="small" icon="Delete" circle style="margin-left: 8px;" @click="() => {
                userFiles.contractf = {} as ResourceFile;
              }" />
            </div>
            <div v-if="!userFiles.contractf || !userFiles.contractf.id">
              <file_upload_box :max-size="1024 * 1024 * 10" model="FILE" :successHandle="(rfile: Object) => {
                const fileInof = rfile as { data: ResourceFile }
                Object.assign(userFiles.contractf, fileInof.data);
              }" :error-handle="(e: { message: string }) => {
                FarmNotice.byError(e.message);
              }" :multiple="false"></file_upload_box>
            </div>
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="['RZ'].includes(formModelRef)"> <el-form-item label="学历证书ID:" prop="eqfid">
            <div v-if="userFiles.eqf.id" class="whrs-file-box">
              <div class="whrs-title" @click="viewWin.openWin(userFiles.eqf.id, true);"> {{
                userFiles.eqf.title }}</div>
              <el-button type="danger" size="small" icon="Delete" circle style="margin-left: 8px;" @click="() => {
                userFiles.eqf = {} as ResourceFile;
              }" />
            </div>
            <div v-if="!userFiles.eqf.id">
              <file_upload_box :max-size="1024 * 1024 * 10" model="FILE" :successHandle="(rfile: Object) => {
                const fileInof = rfile as { data: ResourceFile }
                Object.assign(userFiles.eqf, fileInof.data);
              }" :error-handle="(e: { message: string }) => {
                FarmNotice.byError(e.message);
              }" :multiple="false"></file_upload_box>
            </div>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <div>
      <div></div>
    </div>
    <file_view_win ref="viewWin"></file_view_win>
    <post_choose ref="postChooseRef" :choosehandle="chooseHandle"></post_choose>
    <template #footer>
      <div class="dialog-footer" style="text-align: center;" v-loading="submitLoading">
        <el-button v-if="['RZ'].includes(formModelRef)" type="primary" size="large" style="width: 300px;"
          @click="submitForm('PROBATION')">
          提交入职
        </el-button>
        <el-button v-if="['ZZ'].includes(formModelRef)" type="primary" size="large" style="width: 300px;"
          @click="submitForm('ACTIVE')">
          提交转正
        </el-button>
        <el-button v-if="['DD'].includes(formModelRef)" type="primary" size="large" style="width: 300px;"
          @click="submitForm('NONE')">
          提交调动
        </el-button>
        <el-button v-if="['LZ'].includes(formModelRef)" type="primary" size="large" style="width: 300px;"
          @click="submitForm('LEFT')">
          提交离职
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>
<script setup lang="ts">
import { reactive, ref, type PropType } from 'vue';
import v from '@/hook/farmFormValids';
import farmUserFlowUtils from './utils/farmUserFlowUtils';
import type { user } from '../localuser/utils/user';
import type { minPost } from '../localuser/utils/minPost';
import Post_choose from '../post/post_choose.vue';
import type { AuthGrade } from '../authgrade/utils/authgrade';
import type { ResourceFile } from '../resourcefile/utils/resourcefile';
import FarmNotice from '@/components/msg/FarmNotice';
import File_upload_box from '../resourcefile/components/file_upload_box.vue';
import File_view_win from '@/views/back_console/wdap/file_view_win.vue';
import type { LocalUserInfo } from '../localuserinfo/utils/localuserinfo';
import farmClientUtils from '@/hook/farmClientUtils';
const viewWin = ref();//预览窗口
const grades: Array<AuthGrade> = reactive([]);
const loading = ref(false);
const submitLoading = ref(false);
const postChooseRef = ref();
const ruleFormRef = ref()
const dialogVisibleRef = ref(false);
const userFiles = reactive({
  contractf: {} as ResourceFile,//劳动合同ID
  eqf: {} as ResourceFile,//学历证书ID
  degf: {} as ResourceFile,//学位证书ID
  merf: {} as ResourceFile,//体检报告ID
  icardf: {} as ResourceFile,//身份证复印件ID
});
const formModelRef = ref('1');
const formTitlelRef = ref('');
// 表单默认值
const userForm = reactive({
  id: '',
  post: [] as Array<string>,
  loginname: '',
  name: '',
  type: '1',
  state: '0',
  note: '',
  orgid: '',
  gradeid: '',
});
// 表单默认值
const infoForm = reactive({
  id: '',
  sex: 'O',
  birthdate: '',
  phonecode: '',
  emptime: '',
  email: '',
  idcode: '',
  citycode: '',
  pan: '',
  contractfid: '',
  eqfid: '',
  degfid: '',
  merfid: '',
  icardfid: '',
  process: 1,
  userkey: '',
})
const selectedCity = ref<string[]>([]);
const userPosts: Array<minPost> = reactive([]);
const localOrgPath = ref<string[]>([]);
const props = defineProps({
  refreshHandle: {
    type: Function as PropType<() => void>,
    required: true
  }
});
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

const chooseHandle = (keys: [string], choosePosts: [{ KEYID: string; NAME: string; ID: string }]) => {
  const existingIds = new Set(userPosts.map(p => p.id));
  const newPosts = choosePosts.filter(post => !existingIds.has(post.ID));
  if (newPosts.length > 0) {
    userPosts.push(...newPosts.map(node => ({ keyid: node.KEYID, name: node.NAME, id: node.ID })));
    const newIds = newPosts.map(p => p.ID);
    userForm.post.push(...newIds); // ✅ 现在不会报错了
  }
}

const submitForm = (state: string) => {
  ruleFormRef.value.validate((valid: boolean) => {
    if (valid) {
      if (userFiles.contractf.id) {
        infoForm.contractfid = userFiles.contractf.id
      } else {
        infoForm.contractfid = '';
      }
      if (userFiles.degf.id) {
        infoForm.degfid = userFiles.degf.id
      } else {
        infoForm.degfid = '';
      }
      if (userFiles.eqf.id) {
        infoForm.eqfid = userFiles.eqf.id
      } else {
        infoForm.eqfid = '';
      }
      if (userFiles.icardf.id) {
        infoForm.icardfid = userFiles.icardf.id
      } else {
        infoForm.icardfid = '';
      }
      if (userFiles.merf.id) {
        infoForm.merfid = userFiles.merf.id
      } else {
        infoForm.merfid = '';
      }
      farmClientUtils.postObject('api/wuser/submit', { user: userForm, info: infoForm, state: state }, () => {
        dialogVisibleRef.value = false;
        props.refreshHandle();
      }, submitLoading);
    }
  });
}
// 定义一个方法
const openWin = (isOpen: boolean, userid: string, model: string, title: string) => {
  formModelRef.value = model;
  formTitlelRef.value = title;
  userFiles.contractf = {} as ResourceFile;
  userFiles.eqf = {} as ResourceFile;
  userFiles.degf = {} as ResourceFile;
  userFiles.merf = {} as ResourceFile;
  userFiles.icardf = {} as ResourceFile;
  farmUserFlowUtils.loadUserViewData(userid, (user: user) => {
    Object.assign(userForm, user);
    farmUserFlowUtils.loadOrgPath((orgPath: Array<string>) => {
      localOrgPath.value = orgPath;
    }, userForm.orgid, loading);
    farmUserFlowUtils.loadUserInfoData(user.loginname!, (userRemoteInfo: LocalUserInfo) => {
      Object.assign(infoForm, userRemoteInfo);
      if (userRemoteInfo.contractfFile) {
        userFiles.contractf = userRemoteInfo.contractfFile!;
      }
      if (userRemoteInfo.degfFile) {
        userFiles.degf = userRemoteInfo.degfFile!;
      }
      if (userRemoteInfo.eqfFile) {
        userFiles.eqf = userRemoteInfo.eqfFile!;
      }

      if (userRemoteInfo.icardfFile) {
        userFiles.icardf = userRemoteInfo.icardfFile!;
      }
      if (userRemoteInfo.merfFile) {
        userFiles.merf = userRemoteInfo.merfFile!;
      }
      if (infoForm.citycode) {
        selectedCity.value.length = 0;
        selectedCity.value = infoForm.citycode.split(",");
      } else {
        selectedCity.value = [];
      }
    });

  }, (posts: Array<minPost>) => {
    Object.assign(userPosts, posts);
  }, loading);
  farmUserFlowUtils.loadGrads((rgrades: Array<AuthGrade>) => {
    Object.assign(grades, rgrades);
  })




  dialogVisibleRef.value = isOpen;
};

//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// 暴露方法给父组件
defineExpose({
  openWin,
});
</script>
<style scoped>
/* 样式可以根据需要添加 */
.el-form-item {
  padding-right: 20px;
}

.whrs-file-box {
  display: flex;
  background-color: #ffffff;
  padding: 4px 20px;
  border-radius: 4px;
  color: #999999;

  .whrs-title {
    flex: 1;
    cursor: pointer;
    font-size: 14px;
    width: 170px;
    overflow: hidden;
    /* 👇 必须添加以下两行 */
    white-space: nowrap;
    /* 禁止换行 */
    text-overflow: ellipsis;
    /* 溢出显示省略号 */
  }

  .el-button {
    margin-top: 4px;
    margin-left: auto;
  }
}

.whrs-title:hover {
  cursor: pointer;
  color: #666666;
}
</style>
