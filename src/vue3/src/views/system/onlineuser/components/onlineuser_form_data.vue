<template>
  <el-drawer v-model="formCtrl.isDrawerOpen" class="farm2-data-form" :append-to-body="true" :with-header="false"
    size="500px">
    <div class="title">{{ currentFormType.type.title }}数据</div>
    <el-form v-loading="isLoading" :model="form" label-width="auto" ref="ruleFormRef" :disabled="!formCtrl.isAbledForm">
      <el-form-item label="ip:" prop="ip" v-if="isShowform.ip" :rules="v.getRules(true, 0, 16)">
        <el-input v-model="form.ip" />
      </el-form-item>
      <el-form-item label="name:" prop="name" v-if="isShowform.name" :rules="v.getRules(true, 0, 7)">
        <el-input v-model="form.name" />
      </el-form-item>
      <el-form-item label="loginName：" prop="loginname" v-if="isShowform.loginname" :rules="v.getRules(true, 0, 64)">
        <el-input v-model="form.loginname" autosize type="textarea" />
      </el-form-item>
      <el-form-item label="loginId：" prop="loginid" v-if="isShowform.loginid" :rules="v.getRules(true, 0, 64)">
        <el-input v-model="form.loginid" autosize type="textarea" />
      </el-form-item>
      <el-form-item label="startTime:" prop="starttime" v-if="isShowform.starttime" :rules="v.getRules(false)">
        <el-input-number v-model="form.starttime" />
      </el-form-item>
      <el-form-item label="endTime:" prop="endtime" v-if="isShowform.endtime" :rules="v.getRules(false)">
        <el-input-number v-model="form.endtime" />
      </el-form-item>
      <el-form-item>
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
      </el-form-item>
    </el-form>
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
const eventBus = inject('eventBus') as Emitter<Record<EventType, unknown>>;
const ruleFormRef = ref()//表单对象（可调用验证表单，清空表单方法）
const isLoading = ref(false);//加载状态：true|false
const isRetainForm = ref(false);//提交后是否保留表单
const currentFormType = reactive<FormParam>({ type: getViewType(), id: '', data: {} });//表单类型

//页面控制
const formCtrl = reactive({
  isShowRetain: true,//是否展示（提交后保留表单）选项
  isShowSubmit: true,//是否展示提交按钮
  isDrawerOpen: false,//当前窗口是否打开
  isAbledForm: false,//是否禁用表单
  isShowReset: true//是否展示清空按钮
});

//是否展示字段
const isShowform = reactive({
  ip: true,//ip
  name: true,//name
  loginname: true,//loginName
  loginid: true,//loginId
  starttime: true,//startTime
  endtime: true,//endTime
});

// 表单默认值
const form = reactive({
  id: '',
  ip: '',
  name: '',
  loginname: '',
  loginid: '',
  starttime: 1,
  endtime: 1,
})


/**
 * 打开或关闭表单窗口
 * @param isOpen
 * @param formType
 */
const drawerWinOpen = (isOpen: boolean, formParam?: FormParam) => {
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
  Object.assign(isShowform, {
    ip: true,
    name: true,
    loginname: true,
    loginid: true,
    starttime: true,
    endtime: true,
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
    viewData(id);
    Object.assign(isShowform, {
      ip: true,
      name: true,
      loginname: true,
      loginid: true,
      starttime: true,
      endtime: true,
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
    viewData(id);
  }
}

/**
 * 展示信息（加载用户信息）
 */
const viewData = async (id: string) => {
  farm2Request.submit('api/onlineuser/' + id, RequestTypeEnum.get, isLoading).then((response: DataResponse) => {
    Object.assign(form, response.data)
  }).catch((msg: string) => {
    notice.byError(msg);//报错
  });
}

/**
 * 执行提交按钮（创建/更新）
 */
const addData = async () => {
  //创建提交
  if (currentFormType.type.key == getCreatType().key) {
    farm2Request.submit('api/onlineuser', RequestTypeEnum.post, isLoading, form).then(() => {
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
    farm2Request.submit('api/onlineuser/' + form.id, RequestTypeEnum.put, isLoading, form).then(() => {
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
  eventBus.on(PageEvent.data_form_open, (paras) => {//注册打开form表单方法
    drawerWinOpen((paras as { isOpen: boolean }).isOpen, (paras as { type: FormParam }).type);
  })
});
</script>
