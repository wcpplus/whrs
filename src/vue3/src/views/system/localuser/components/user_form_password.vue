<template>
  <el-dialog v-model="dialogFormVisible" title="修改密码" width="380">
    <el-form ref="formRef" v-loading="isLoading" :inline="true" :model="form" class="demo-form-inline">
      <el-form-item label="当前用户：" style="color: var(--el-color-primary);">
        {{ userInfo.user.name }}
      </el-form-item>
      <el-form-item style="width: 100%;" prop="password" :rules="v.getRules(true)">
        <el-input prefix-icon="Lock" size="large" v-model="form.password" type="password" placeholder="当前用户密码"
          show-password :validate-event="false" />
      </el-form-item>
      <el-divider style="margin-top: 4px;" border-style="dashed"><el-icon>
          <Lock />
        </el-icon><span v-if="forUserKeys.length > 1 || forUserKeys.length < 1">&nbsp;更新{{ forUserKeys.length
          }}人</span></el-divider>
      <el-form-item style="width: 100%;" prop="newPaswword1"
        :rules="v.getRegExpRules(v.ValidRxs.indexKey, true, 6, 32)">
        <el-input prefix-icon="Lock" size="large" v-model="form.newPaswword1" type="password" placeholder="新密码"
          show-password :validate-event="false" />
      </el-form-item>
      <el-form-item style="width: 100%;" prop="newPaswword2"
        :rules="v.getCustomRules(validateNewPaswword2, true, 6, 32)">
        <el-input prefix-icon="Lock" size="large" v-model="form.newPaswword2" type="password" placeholder="重复新密码"
          show-password :validate-event="false" />
      </el-form-item>
      <div style="text-align: center;">
        <el-button style="width: 100%;margin-top:20px  ;" @click="submitForm" size="large"
          type="primary">修改密码</el-button>
      </div>
    </el-form>
  </el-dialog>
</template>
<script setup lang="ts">
import { reactive, ref } from 'vue';
import v from '@/hook/farmFormValids';
import { useUserInfoStore } from '@/store/useUserInfoStore';
import farmPasswordUtils from '@/hook/farmPasswordUtils';
import Farm2Request from '@/service/remoteRequests/Farm2Request';
import type { DataResponse } from '@/types/commons/DataResponse';
import { RequestTypeEnum } from '@/types/commons/RequestTypeEnum';
import notice from '@/components/msg/FarmNotice';
const userInfo = useUserInfoStore();
const dialogFormVisible = ref(false)//显示密码修改窗口
const isLoading = ref(false);
const form = reactive({
  password: '',
  newPaswword1: '',
  newPaswword2: '',
});

const forUserKeys = reactive([]);

let codes = { webcode: { code: "", val: "" }, syscode: "", imgcode: "" }
//表单对象（可调用验证表单，清空表单方法）
const formRef = ref();


const props = defineProps({
  updateUrl: {
    type: String,
    required: true
  },
});

//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
const openWin = (userKeys: Array<string>) => {
  dialogFormVisible.value = true;
  forUserKeys.length = 0;
  Object.assign(forUserKeys, userKeys);
}

/**
 * 校验password2和password1是否相同
 * @param _rule
 * @param _value
 * @param callback
 */
const validateNewPaswword2 = (_rule: never, _value: string | undefined, callback: (arg0?: Error) => void) => {
  if (form.newPaswword1 == form.newPaswword2) {
    callback();
  } else {
    callback(new Error("两次新密码不同"));
  }
};

const submitForm = async () => {
  //验证表单
  await formRef.value.validate((valid: boolean) => {
    if (valid) {
      //获取远程盐
      //加密密码
      Farm2Request.submit("api/login/info", RequestTypeEnum.post, isLoading, {
        loginname: userInfo.$state.user.loginname, password: 'none', code: 'none'
      }).then((dataResponse: DataResponse) => {
        codes = dataResponse.data as { webcode: { code: string, val: string }, syscode: string, imgcode: string };
        farmPasswordUtils.encodeWebPassword(codes.syscode, codes.webcode.val, form.password, (web_Password: string) => {
          farmPasswordUtils.encodeSysPassword(form.newPaswword1, (sysPassword: string) => {
            //提交修改密码
            Farm2Request.submit(props.updateUrl, RequestTypeEnum.post, isLoading,
              { loginname: userInfo.$state.user.loginname, webPassword: web_Password, code: codes.webcode.code, userkeys: forUserKeys, newSysPassword: sysPassword }
            // eslint-disable-next-line @typescript-eslint/no-unused-vars
            ).then((_dataResponse: DataResponse) => {
              //修改成功
              notice.bySuccess("修改成功");
              formRef.value.resetFields();
              dialogFormVisible.value = false;
            }).catch((msg: Error) => {
              notice.byError(msg.message);
            });
          })
        });
      }).catch((msg: Error) => {
        notice.byError(msg.message);
      });
    }
  });
}
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
defineExpose({
  openWin
});
</script>
<style>
.el-divider .el-divider__text {
  background-color: transparent;
}
</style>
