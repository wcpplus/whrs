<template>
  <el-drawer v-model="formCtrl.isDrawerOpen" class="farm2-data-form" :append-to-body="true" :with-header="false"
    size="500px">
    <div class="title">{{ currentFormType.type.title }}数据</div>
    <el-form v-loading="isLoading" :model="form" label-width="auto" ref="ruleFormRef" :disabled="!formCtrl.isAbledForm">
      <el-form-item label="ID:" prop="id" v-if="isShowform.id">
        <el-input v-model="form.id" />
      </el-form-item>
      <el-form-item label="创建时间:" prop="ctime" v-if="isShowform.ctime" :rules="v.getRules(true, 0, 7)">
        <el-input v-model="form.ctime" />
      </el-form-item>
      <el-form-item label="创建用户:" prop="cuserkey" v-if="isShowform.cuserkey" :rules="v.getRules(true, 0, 16)">
        <el-input v-model="form.cuserkey" />
      </el-form-item>
      <el-form-item label="状态：" prop="state" v-if="isShowform.state" :rules="v.getRules(true)">
        <el-select v-model="form.state" placeholder="请选择">
          <el-option label="~" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="备注：" prop="note" v-if="isShowform.note" :rules="v.getRules(false, 0, 256)">
        <el-input v-model="form.note" autosize type="textarea" />
      </el-form-item>
      <el-form-item label="文件扩展名:" prop="exname" v-if="isShowform.exname" :rules="v.getRules(true, 0, 16)">
        <el-input v-model="form.exname" />
      </el-form-item>
      <el-form-item label="相对路径：" prop="relativepath" v-if="isShowform.relativepath" :rules="v.getRules(true, 0, 512)">
        <el-input v-model="form.relativepath" autosize type="textarea" />
      </el-form-item>
      <el-form-item label="原文件名：" prop="filename" v-if="isShowform.filename" :rules="v.getRules(true, 0, 64)">
        <el-input v-model="form.filename" autosize type="textarea" />
      </el-form-item>
      <el-form-item label="文件别名：" prop="title" v-if="isShowform.title" :rules="v.getRules(true, 0, 128)">
        <el-input v-model="form.title" autosize type="textarea" />
      </el-form-item>
      <el-form-item label="文件大小:" prop="filesize" v-if="isShowform.filesize" :rules="v.getRules(true)">
        <el-input-number v-model="form.filesize" />
      </el-form-item>
      <el-form-item label="资源地址KEY：" prop="resourcekey" v-if="isShowform.resourcekey" :rules="v.getRules(true, 0, 16)">
        <el-input v-model="form.resourcekey" autosize type="textarea" />
      </el-form-item>
      <el-form-item label="外部ID：" prop="appid" v-if="isShowform.appid" :rules="v.getRules(false, 0, 64)">
        <el-input v-model="form.appid" autosize type="textarea" />
      </el-form-item>
      <el-form-item label="存储路径：" prop="appid">
        <el-input v-model="form.fullpath" autosize type="textarea" />
      </el-form-item>
      <div style="text-align: center;"
        v-if="['jpg', 'png', 'gif'].some(element => element.toLowerCase() === form.exname)">
        <el-image style="max-width: 100%; " :src="Farm2Request.getFullPath('api/files/download/' + form.id)"
          fit="fill" />
      </div>
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
import Farm2Request from '@/service/remoteRequests/Farm2Request';
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
  id: false,//主鍵
  ctime: false,//创建时间
  cuserkey: false,//创建用户
  state: false,//状态
  note: true,//备注
  exname: true,//文件扩展名
  relativepath: true,//相对路径
  filename: true,//原文件名
  title: true,//文件别名
  filesize: false,//文件大小
  resourcekey: true,//资源地址KEY
  appid: false,//外部ID
});

// 表单默认值
const form = reactive({
  id: '',
  ctime: '',
  cuserkey: '',
  state: '',
  note: '',
  exname: '',
  relativepath: '',
  filename: '',
  title: '',
  filesize: 1,
  resourcekey: '',
  appid: '',
  fullpath: '',
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
    ctime: false,
    cuserkey: false,
    state: false,
    note: false,
    exname: true,
    relativepath: true,
    filename: true,
    title: true,
    filesize: true,
    resourcekey: true,
    appid: false,
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
    Object.assign(isShowform, {
      id: true,
      ctime: true,
      cuserkey: true,
      state: true,
      note: true,
      exname: true,
      relativepath: true,
      filename: true,
      title: true,
      filesize: true,
      resourcekey: true,
      appid: true,
    });
    viewData(id);
  }
}

/**
 * 展示信息（加载用户信息）
 */
const viewData = async (id: string) => {
  farm2Request.submit('api/resourcefile/' + id, RequestTypeEnum.get, isLoading).then((response: DataResponse) => {
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
    farm2Request.submit('api/resourcefile', RequestTypeEnum.post, isLoading, form).then(() => {
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
    farm2Request.submit('api/resourcefile/' + form.id, RequestTypeEnum.put, isLoading, form).then(() => {
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
