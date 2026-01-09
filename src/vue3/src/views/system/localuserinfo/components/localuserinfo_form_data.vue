<template>
  <el-drawer v-model="formCtrl.isDrawerOpen" class="farm2-data-form" :append-to-body="true" :with-header="false"
    size="500px">
    <div class="title">{{ currentFormType.type.title }}数据</div>
    <div style="height: calc(100vh - 130px); overflow: auto; background-color: var(--el-skc-theme-body-back-c);">
      <el-form :show-message="formCtrl.isShowValidMsg" v-loading="isLoading" :model="form" label-width="auto"
        ref="ruleFormRef" :disabled="!formCtrl.isAbledForm">
        <el-form-item label="主鍵:" prop="id" v-if="isShowform.id" :rules="v.getRules(true, 0, 16)">
          <el-input v-model="form.id" />
        </el-form-item>
        <el-form-item label="性别:" prop="sex" v-if="isShowform.sex" :rules="v.getRules(false, 0, 16)">
          <el-select v-model="form.sex">
            <el-option label="男" value="M" />
            <el-option label="女" value="W" />
            <el-option label="其他" value="O" />
          </el-select>
        </el-form-item>
        <el-form-item label="生日:" prop="birthdate" v-if="isShowform.birthdate" :rules="v.getRules(false, 0, 16)">
          <el-date-picker v-model="form.birthdate" type="date" value-format="YYYYMMDD" placeholder="选择日期"
            style="width: 100%;" />
        </el-form-item>
        <el-form-item label="移动电话:" prop="phonecode" v-if="isShowform.phonecode"
          :rules="v.getRegExpRules(v.ValidRxs.phone, false)">
          <el-input v-model="form.phonecode" />
        </el-form-item>
        <el-form-item label="入职时间:" prop="emptime" v-if="isShowform.emptime" :rules="v.getRules(false, 0, 16)">
          <el-date-picker v-model="form.emptime" type="date" value-format="YYYYMMDD" placeholder="选择日期"
            style="width: 100%;" />
        </el-form-item>
        <el-form-item label="电子邮箱:" prop="email" v-if="isShowform.email"
          :rules="v.getRegExpRules(v.ValidRxs.email, false)">
          <el-input v-model="form.email" />
        </el-form-item>
        <el-form-item label="身份证:" prop="idcode" v-if="isShowform.idcode"
          :rules="v.getRegExpRules(v.ValidRxs.idCard, false)">
          <el-input v-model="form.idcode" />
        </el-form-item>
        <el-form-item label="参保城市:" prop="citycode" v-if="isShowform.citycode" :rules="v.getRules(false, 0, 16)">
          <city_cascader :key="cascaderKeyRef" v-model="selectedCity" @change="onCityChange" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="银行卡号:" prop="pan" v-if="isShowform.pan"
          :rules="v.getRegExpRules(v.ValidRxs.bankCard, false)">
          <el-input v-model="form.pan" />
        </el-form-item>
        <el-form-item label="劳动合同ID:" prop="contractfid" v-if="isShowform.contractfid">
          <div v-if="userFiles.contractf.id" class="whrs-file-box">
            <div class="whrs-title" @click="viewWin.openWin(userFiles.contractf.id, true);"> {{
              userFiles.contractf.title }}</div>
            <el-button type="danger" size="small" icon="Delete" circle style="margin-left: 8px;" @click="() => {
              userFiles.contractf = {} as ResourceFile;
            }" />
          </div>
          <div v-if="!userFiles.contractf.id">
            <file_upload_box :max-size="1024 * 1024 * 10" model="FILE" :successHandle="(rfile: Object) => {
              const fileInof = rfile as { data: ResourceFile }
              Object.assign(userFiles.contractf, fileInof.data);
            }" :error-handle="(e: { message: string }) => {
              FarmNotice.byError(e.message);
            }" :multiple="false"></file_upload_box>
          </div>
        </el-form-item>
        <el-form-item label="学历证书ID:" prop="eqfid" v-if="isShowform.eqfid">
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
        <el-form-item label="学位证书ID:" prop="degfid" v-if="isShowform.degfid">
          <div v-if="userFiles.degf.id" class="whrs-file-box">
            <div class="whrs-title" @click="viewWin.openWin(userFiles.degf.id, true);">{{
              userFiles.degf.title }}</div><el-button type="danger" size="small" icon="Delete" circle @click="() => {
                userFiles.degf = {} as ResourceFile;
              }" style="margin-left: 8px;" />
          </div>
          <div v-if="!userFiles.degf.id">
            <file_upload_box :max-size="1024 * 1024 * 10" model="FILE" :successHandle="(rfile: Object) => {
              const fileInof = rfile as { data: ResourceFile }
              Object.assign(userFiles.degf, fileInof.data);
            }" :error-handle="(e: { message: string }) => {
              FarmNotice.byError(e.message);
            }" :multiple="false"></file_upload_box>
          </div>
        </el-form-item>
        <el-form-item label="体检报告ID:" prop="merfid" v-if="isShowform.merfid">
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
        <el-form-item label="身份证复印件ID:" prop="icardfid" v-if="isShowform.icardfid">
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
        <el-form-item label="完善度:" prop="process" v-if="false" :rules="v.getRules(true)">
          <el-input-number v-model="form.process" />
        </el-form-item>
        <el-form-item label="USERKEY:" prop="userkey" v-if="false" :rules="v.getRules(false, 0, 16)">
          <el-input v-model="form.userkey" />
        </el-form-item>

      </el-form>
    </div>
    <div style="padding: 20px;">
      <el-row class="farm2-form-button">
        <el-col :span="14">
          <el-button v-if="formCtrl.isShowSubmit" type="primary" @click="onSubmitForm(addData)">提交{{
            currentFormType.type.title }}</el-button>
          <el-switch v-if="formCtrl.isShowRetain" v-model="isRetainForm" size="small" inactive-text="提交后保留表单" />
        </el-col>
        <el-col :span="10" class="farm2-right">
          <el-button v-if="formCtrl.isShowReset" type="warning" @click="onResetForm">清空</el-button>
          <el-button v-if="formCtrl.isShowSubmit" type="info" plain @click="drawerWinOpen(false)">取消</el-button>
        </el-col>
      </el-row>
      <file_view_win ref="viewWin"></file_view_win>
    </div>
  </el-drawer>
</template>
<script lang="ts" setup>
//------------------------------------------------------------------------------------
import { inject, onMounted, reactive, ref } from 'vue';
import v from '@/hook/farmFormValids';
import farm2Request from '@/service/remoteRequests/Farm2Request';
import { RequestTypeEnum } from '@/types/commons/RequestTypeEnum';
import notice from '@/components/msg/FarmNotice';
import { getCreatType, getUpdateType, getViewType, type FormParam } from '@/types/commons/FormType';
import type { DataResponse } from '@/types/commons/DataResponse';
import type { Emitter, EventType } from 'mitt';
import { PageEvent } from '@/types/commons/PageEvent';
import farmFormUtils from '@/hook/farmFormUtils';
import City_cascader from '@/components/utils/city_cascader.vue';
import File_upload_box from '../../resourcefile/components/file_upload_box.vue';
import FarmNotice from '@/components/msg/FarmNotice';
import type { ResourceFile } from '../../resourcefile/utils/resourcefile';
import File_view_win from '@/views/back_console/wdap/file_view_win.vue';
import type { LocalUserInfo } from '../utils/localuserinfo';
const eventBus = inject('eventBus') as Emitter<Record<EventType, unknown>>;
const ruleFormRef = ref()//表单对象（可调用验证表单，清空表单方法）
const isLoading = ref(false);//加载状态：true|false
const viewWin = ref();//预览窗口
const isRetainForm = ref(false);//提交后是否保留表单
const currentFormType = reactive<FormParam>({ type: getViewType(), id: '', data: {} });//表单类型
const cascaderKeyRef = ref(1);
//页面控制
const formCtrl = reactive({
  isShowRetain: true,//是否展示（提交后保留表单）选项
  isShowSubmit: true,//是否展示提交按钮
  isDrawerOpen: false,//当前窗口是否打开
  isAbledForm: false,//是否禁用表单
  isShowReset: true,//是否展示清空按钮
  isShowValidMsg: true,//是否展示校验信息
});
const userFiles = reactive({
  contractf: {} as ResourceFile,//劳动合同ID
  eqf: {} as ResourceFile,//学历证书ID
  degf: {} as ResourceFile,//学位证书ID
  merf: {} as ResourceFile,//体检报告ID
  icardf: {} as ResourceFile,//身份证复印件ID
});

//是否展示字段
const isShowform = reactive({
  id: true,//主鍵
  sex: true,//SEX
  birthdate: true,//生日
  phonecode: true,//移动电话
  emptime: true,//入职时间
  email: true,//电子邮箱
  idcode: true,//身份证
  citycode: true,//参保城市
  pan: true,//银行卡号
  contractfid: true,//劳动合同ID
  eqfid: true,//学历证书ID
  degfid: true,//学位证书ID
  merfid: true,//体检报告ID
  icardfid: true,//身份证复印件ID
  process: true,//完善度
  userkey: true,//USERKEY
});

// 表单默认值
const form = reactive({
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

const onCityChange = (names: string[], codes: string[]) => {
  console.log('城市名称:', names); // e.g. ['广东省', '深圳市']
  console.log('城市编码:', codes); // e.g. ['440000', '440300']
};

/**
 * 打开或关闭表单窗口
 * @param isOpen
 * @param formType
 */
const drawerWinOpen = (isOpen: boolean, formParam?: FormParam) => {
  userFiles.contractf = {} as ResourceFile;
  userFiles.eqf = {} as ResourceFile;
  userFiles.degf = {} as ResourceFile;
  userFiles.merf = {} as ResourceFile;
  userFiles.icardf = {} as ResourceFile;
  if (!isOpen) {
    formCtrl.isDrawerOpen = isOpen;
  } else {
    if (formParam?.type.key !== currentFormType?.type.key) {
      ruleFormRef.value?.resetFields();
    }
    Object.assign(currentFormType, formParam);
    if (formParam?.type.key == getCreatType().key) {
      initCreatForm(formParam);//创建
    }
    if (formParam?.type.key == getUpdateType().key) {
      initUpdateForm(formParam.id); //更新
    }
    if (formParam?.type.key == getViewType().key) {
      initViewForm(formParam.id);//浏览
    }
  }
};

/**
 * 初始化创建窗口
 */
const initCreatForm = (formParam: FormParam) => {
  farmFormUtils.initFormCtrl(getCreatType(), formCtrl);
  formCtrl.isShowValidMsg = true;
  Object.assign(isShowform, {
    id: false,
    sex: true,
    birthdate: true,
    phonecode: true,
    emptime: true,
    email: true,
    idcode: true,
    citycode: true,
    pan: true,
    contractfid: true,
    eqfid: true,
    degfid: true,
    merfid: true,
    icardfid: true,
    process: true,
    userkey: true,
  });
  if (formParam) {
    //处理创建窗口时传入数据
  }
}

/**
 * 初始化更新窗口
 * @param id
 */
const initUpdateForm = (id: string | undefined) => {
  farmFormUtils.initFormCtrl(getUpdateType(), formCtrl);
  if (!id) {
    notice.byError("id不能为空");
  } else {
    formCtrl.isShowValidMsg = true;
    viewData(id);
    Object.assign(isShowform, {
      id: false,
      sex: true,
      birthdate: true,
      phonecode: true,
      emptime: true,
      email: true,
      idcode: true,
      citycode: true,
      pan: true,
      contractfid: true,
      eqfid: true,
      degfid: true,
      merfid: true,
      icardfid: true,
      process: true,
      userkey: true,
    });
  }
}

/**
 * 初始化浏览窗口
 * @param id
 */
const initViewForm = (id: string | undefined) => {
  farmFormUtils.initFormCtrl(getViewType(), formCtrl);
  if (!id) {
    notice.byError("id不能为空");
  } else {
    formCtrl.isShowValidMsg = false;
    viewData(id);
    Object.assign(isShowform, {
      id: false,
      sex: true,
      birthdate: true,
      phonecode: true,
      emptime: true,
      email: true,
      idcode: true,
      citycode: true,
      pan: true,
      contractfid: true,
      eqfid: true,
      degfid: true,
      merfid: true,
      icardfid: true,
      process: true,
      userkey: true,
    });
  }
}

/**
 * 展示信息（加载用户信息）
 */
const viewData = async (id: string) => {
  farm2Request.submit('api/localuserinfo/' + id, RequestTypeEnum.get, isLoading).then((response: DataResponse) => {
    Object.assign(form, response.data);
    const userRemoteFiles = response.data as LocalUserInfo;
    if (userRemoteFiles.contractfFile) {
      userFiles.contractf = userRemoteFiles.contractfFile!;
    }
    if (userRemoteFiles.degfFile) {
      userFiles.degf = userRemoteFiles.degfFile!;
    }
    if (userRemoteFiles.eqfFile) {
      userFiles.eqf = userRemoteFiles.eqfFile!;
    }

    if (userRemoteFiles.icardfFile) {
      userFiles.icardf = userRemoteFiles.icardfFile!;
    }
    if (userRemoteFiles.merfFile) {
      userFiles.merf = userRemoteFiles.merfFile!;
    }
    if (form.citycode) {
      selectedCity.value.length = 0;
      selectedCity.value = form.citycode.split(",");
    } else {
      selectedCity.value = [];
    }
    cascaderKeyRef.value = cascaderKeyRef.value + 1;
  }).catch((msg: string) => {
    notice.byError(msg);//报错
  });
}

/**
 * 执行提交按钮（创建/更新）
 */
const addData = async () => {
  if (selectedCity.value && selectedCity.value.length > 0) {
    form.citycode = selectedCity.value.join(",");
  } else {
    form.citycode = '';
  }

  if (userFiles.contractf.id) {
    form.contractfid = userFiles.contractf.id
  } else {
    form.contractfid = '';
  }

  if (userFiles.degf.id) {
    form.degfid = userFiles.degf.id
  } else {
    form.degfid = '';
  }

  if (userFiles.eqf.id) {
    form.eqfid = userFiles.eqf.id
  } else {
    form.eqfid = '';
  }

  if (userFiles.icardf.id) {
    form.icardfid = userFiles.icardf.id
  } else {
    form.icardfid = '';
  }

  if (userFiles.merf.id) {
    form.merfid = userFiles.merf.id
  } else {
    form.merfid = '';
  }


  //创建提交
  if (currentFormType.type.key == getCreatType().key) {
    farm2Request.submit('api/localuserinfo', RequestTypeEnum.post, isLoading, form).then(() => {
      notice.bySuccess("创建成功!");//提示成功
      eventBus.emit(PageEvent.data_do_query);//刷新列表
      if (!isRetainForm.value) {//保留窗口数据
        onResetForm();//清空
        drawerWinOpen(false);//关闭
      }
    }).catch((msg: string) => {
      notice.byError(msg);//报错
    });
  }
  //更新提交
  if (currentFormType.type.key == getUpdateType().key) {
    farm2Request.submit('api/localuserinfo/' + form.id, RequestTypeEnum.put, isLoading, form).then(() => {
      notice.bySuccess("更新成功!");//提示成功
      eventBus.emit(PageEvent.data_do_query);//刷新列表
      onResetForm();//清空
      drawerWinOpen(false);//关闭
    }).catch((msg: string) => {
      notice.byError(msg);//报错
    });
  }
}


/**清空表单 */
const onResetForm = () => {
  ruleFormRef.value.resetFields();
}

/**
 * 校验表单，准备提交
 * @param func
 */
const onSubmitForm = async (func: () => void) => {
  await ruleFormRef.value.validate((valid: boolean) => { if (valid) { func() } });
}
//------------------------------------------------------------------------------------
onMounted(() => {
  eventBus.on(PageEvent.data_form_open + "_USERINFO", (paras) => {//注册打开form表单方法
    drawerWinOpen((paras as { isOpen: boolean }).isOpen, (paras as { type: FormParam }).type);
  })
});
</script>
<style scoped>
.whrs-file-box {
  display: flex;
  background-color: #ffffff;
  padding: 4px 20px;
  border-radius: 4px;
  color: #999999;
  width: 100%;

  .whrs-title {
    flex: 1;
    cursor: pointer;
    font-size: 14px;
    width: 200px;
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
