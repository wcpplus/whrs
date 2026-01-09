<template>
  <div class="farm2LoginForm">
    <div class="title">
      <img :src="config.logoPath" alt="Logo" /> {{ config.title }}
    </div>
    <el-form @keydown.enter="handleSubmit" ref="formRef" v-loading="isLoading" :inline="true" :model="form"
      class="demo-form-inline">
      <el-form-item style="width: 100%;" prop="loginname" :rules="v.getRegExpRules(v.ValidRxs.loginname, true)">
        <el-input prefix-icon="User" size="large" v-model="form.loginname" placeholder="登录名" clearable
          :validate-event="false" />
      </el-form-item>
      <el-form-item style="width: 100%;" prop="password" :rules="v.getRules(true)">
        <el-input prefix-icon="Lock" size="large" v-model="form.password" type="password" placeholder="登录密码"
          show-password :validate-event="false" />
      </el-form-item>
      <el-form-item style="width: 100%;" v-if="isShowImgCode" prop="imgcode" :rules="v.getRules(true)">
        <el-input prefix-icon="Picture" show-word-limit v-model="form.imgcode" maxlength="4" type="text"
          style="width: 100%;" placeholder="验证码">
          <template #suffix>
            <img @click="refreshCaptcha" :src="captchaUrl"
              style="height: 100%;width: 100px;padding: 2px;cursor: pointer;" alt="Logo" />
          </template>
        </el-input>
      </el-form-item>
      <div style="text-align: right;">
        <el-switch v-model="isSaveLogin" class="mt-2" style="margin-left: 24px" inline-prompt active-icon="Check"
          inactive-icon="Close" /><span style="font-size: 12px;color: #999999;">&nbsp;保持登录</span>
      </div>
      <div style="text-align: center;">
        <el-button style="width: 100%;margin-top:20px  ;" @click="doLogin" size="large" type="primary">登录</el-button>
      </div>
      <div style="text-align: center;" v-if="isNeedLoginRef && false">
        <el-button style="width: 100%;margin-top:20px  ;" @click="farmRoutUtils.goToHome()" size="large" type="info"
          plain>免登录进入</el-button>
      </div>
    </el-form>
  </div>
</template>
<script setup lang="ts">
import { useSystemConfigStore } from '@/store/useSystemConfigStore';
import { useUserInfoStore } from '@/store/useUserInfoStore';
import Farm2Request from '@/service/remoteRequests/Farm2Request';
import { onMounted, reactive, ref } from 'vue';
import v from '@/hook/farmFormValids';
import { RequestTypeEnum } from '@/types/commons/RequestTypeEnum';
import type { DataResponse } from '@/types/commons/DataResponse';
import notice from '@/components/msg/FarmNotice';
import farmPasswordUtils from '@/hook/farmPasswordUtils';
import farmRoutUtils from '@/hook/farmRoutUtils';
import farmHtmlConfig from '@/hook/farmHtmlConfig';
// 初始化验证码URL
const captchaUrl = ref(Farm2Request.getFullPath(''));
const sysConfig = useSystemConfigStore();
const userInfo = useUserInfoStore();
const isLoading = ref(false);
const isSaveLogin = ref(false);
const isShowImgCode = ref(false);
//表单对象（可调用验证表单，清空表单方法）
const formRef = ref();
const isNeedLoginRef = ref(false);
//后台参数
const config = reactive({ title: "", logoPath: "" });
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
const form = reactive({
  loginname: '',
  password: '',
  imgcode: '',
});
const loginname = farmHtmlConfig.getStringVal('data-config-demo-loginname');
const password = farmHtmlConfig.getStringVal('data-config-demo-password');
if (loginname) {
  form.loginname = loginname;
}
if (password) {
  form.password = password;
}
// 刷新验证码的方法
function refreshCaptcha() {
  // 使用时间戳作为查询参数以避免缓存
  if (form.loginname && form.loginname != '') {
    captchaUrl.value = `${Farm2Request.getFullPath('api/login/captcha/' + form.loginname)}?t=${new Date().getTime()}`;
  }
}

const handleSubmit = () => {
  doLogin();
}
let codes = { webcode: { code: "", val: "" }, syscode: "", imgcode: false }

/**
 * 执行登录
 */
const doLogin = async () => {
  //验证表单
  await formRef.value.validate((valid: boolean) => {
    if (valid) {
      //获取远程盐
      Farm2Request.submit("api/login/info", RequestTypeEnum.post, isLoading, {
        loginname: form.loginname, password: 'none', code: 'none'
      }).then((dataResponse: DataResponse) => {
        codes = dataResponse.data as { webcode: { code: string, val: string }, syscode: string, imgcode: boolean };
        if (codes.imgcode) {
          //需要验证码就展示验证码框
          isShowImgCode.value = true;
          //加载验证码
          if (!form.imgcode) {
            refreshCaptcha();
          }
        } else {
          isShowImgCode.value = false;
        }
        formRef.value.validate((revalid: boolean) => {
          if (revalid) {
            {//加密密码并提交
              farmPasswordUtils.encodeWebPassword(codes.syscode, codes.webcode.val, form.password, (webPassword: string) => {
                //提交登录
                Farm2Request.submit("api/login/auth", RequestTypeEnum.post, isLoading,
                  { loginname: form.loginname, password: webPassword, code: codes.webcode.code, imgcode: form.imgcode, saveLogin: isSaveLogin.value }
                ).then((dataResponse: DataResponse) => {
                  //登录成功
                  userInfo.login(farmHtmlConfig.isJwtModel("local") ? (dataResponse.data as { token: string }).token : '', isSaveLogin.value);
                  //    const url = userConfig.getGotoUrl;
                  //  if (url && url.indexOf('/home') < 0) {
                  //返回来时页面
                  // farmRoutUtils.gotoPage(url);
                  //} else {
                  //访问首页
                  farmRoutUtils.goToHome();
                  //}
                }).catch((msg: Error) => {
                  notice.byError(msg.message);
                  form.imgcode = '';
                  form.password = '';
                  if (isShowImgCode.value) {
                    refreshCaptcha();
                  }
                });
              });
            }
          }
        });
      }).catch((msg: Error) => {
        notice.byError(msg.message);
      });
    }
  });

}
onMounted(() => {
  sysConfig.getParameter("farm2.config.sys.title.head", (value) => {
    config.title = value as string;
  });
  sysConfig.getParameter("logoPath", (value) => {
    config.logoPath = value as string;
  });
  sysConfig.getParameter("farm2.config.permission.login.need", (val) => {
    //是否必须登录
    isNeedLoginRef.value = !(val as boolean);
  })
});
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
</script>
<style>
.farm2LoginForm {
  padding-top: 5rem;
  max-width: 300px;
  margin: auto;
}

.farm2LoginForm img {
  border-radius: 10px;
  max-height: 32px;
}

.farm2LoginForm .title {
  font-size: 36px;
  padding-top: 50px;
  color: #25396f;
  font-weight: 700;
  margin-bottom: 20px;
}

.farm2LoginForm .el-form-item__error {
  color: #f3616d;
}
</style>
