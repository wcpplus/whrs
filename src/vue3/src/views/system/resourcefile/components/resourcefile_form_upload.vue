<template>
  <el-drawer v-model="formCtrl.isDrawerOpen" class="farm2-data-form" :append-to-body="true" :with-header="false"
    size="500px">
    <div class="title">{{ currentFormType.type.title }}</div>
    <el-form v-loading="isLoading" label-width="auto" ref="ruleFormRef" :disabled="!formCtrl.isAbledForm">
      <File_upload_box model="FILE" :multiple="true" :error-handle="errorHandle" :success-handle="successHandle">
      </File_upload_box>
      <el-switch v-if="formCtrl.isShowRetain" v-model="isRetainForm" inactive-text="提交后保留表单" />
    </el-form>
  </el-drawer>
</template>
<script lang="ts" setup>
//------------------------------------------------------------------------------------
import { inject, onMounted, reactive, ref } from 'vue';
import File_upload_box from './file_upload_box.vue';
import { MittKeysEnum } from '../utils/mittKeys'
import { getCreatType, getViewType, type FormParam } from '@/types/commons/FormType';
import type { Emitter, EventType } from 'mitt';
import farmFormUtils from '@/hook/farmFormUtils';
import { PageEvent } from '@/types/commons/PageEvent';
import notice from '@/components/msg/FarmNotice';
const eventBus = inject('eventBus') as Emitter<Record<EventType, unknown>>;
const ruleFormRef = ref()//表单对象（可调用验证表单，清空表单方法）
const isLoading = ref(false);//加载状态：true|false
const currentFormType = reactive<FormParam>({ type: getViewType(), id: '', data: {} });//表单类型
const isRetainForm = ref(false);
//页面控制
const formCtrl = reactive({
  isShowRetain: true,//是否展示（提交后保留表单）选项
  isShowSubmit: true,//是否展示提交按钮
  isDrawerOpen: false,//当前窗口是否打开
  isAbledForm: false,//是否禁用表单
  isShowReset: true//是否展示清空按钮
});

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
  }
};
/**
 * 初始化创建窗口
 */
const initCreatForm = (formParam: FormParam) => {
  farmFormUtils.initFormCtrl(getCreatType(), formCtrl);
  if (formParam) {
    //处理创建窗口时传入数据
  }
}

const errorHandle = (msg: Error) => {
  notice.byError(msg.message);//报错
}
const successHandle = () => {
  notice.bySuccess("上传成功!");//提示成功
  eventBus.emit(PageEvent.data_do_query);//刷新列表
  if (isRetainForm.value) {
  } else {
    formCtrl.isDrawerOpen = false;
  }
}

//------------------------------------------------------------------------------------
onMounted(() => {
  eventBus.on(MittKeysEnum.openUploadForm, (paras) => {//注册打开form表单方法
    drawerWinOpen((paras as { isOpen: boolean }).isOpen, (paras as { type: FormParam }).type);
  })
});
</script>
