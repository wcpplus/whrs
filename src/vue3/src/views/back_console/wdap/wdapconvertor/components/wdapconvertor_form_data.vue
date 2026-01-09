<template>
  <el-drawer v-model="formCtrl.isDrawerOpen" class="farm2-data-form" :append-to-body="true" :with-header="false"
    size="500px">
    <div class="title">{{ currentFormType.type.title }}数据</div>
    <el-form v-loading="isLoading" :model="form" label-width="auto" ref="ruleFormRef" :disabled="!formCtrl.isAbledForm">
      <el-form-item label="主鍵:" prop="id" v-if="isShowform.id" :rules="v.getRules(true, 0, 32)">
        <el-input v-model="form.id" />
      </el-form-item>
      <el-form-item label="实现类：" prop="classkey" v-if="isShowform.classkey" :rules="v.getRules(true, 0, 512)">
        <el-select v-model="form.classkey" placeholder="请选择">
          <el-option v-for="node in impls" :key="node.classKey" :label="node.classKey + ':' + node.title"
            :value="node.classKey" />
        </el-select>
      </el-form-item>
      <el-form-item label="来源model:" prop="sfilemodel" v-if="isShowform.sfilemodel" :rules="v.getRules(true, 0, 32)">
        <el-select v-model="form.sfilemodel" placeholder="请选择">
          <el-option v-for="node in filemodels" :key="node.key" :label="node.key + ':' + node.title"
            :value="node.key" />
        </el-select>
      </el-form-item>
      <el-form-item label="目标model:" prop="tfilemodel" v-if="isShowform.tfilemodel" :rules="v.getRules(true, 0, 32)">
        <el-input v-model="form.tfilemodel" />
      </el-form-item>
      <el-form-item label="标题:" prop="title" v-if="isShowform.title" :rules="v.getRules(true, 0, 32)">
        <el-input v-model="form.title" />
      </el-form-item>
      <div v-if="isShowform.params">
        <el-form-item v-for="node in form.params" :key="node.field" :label="node.title">
          <el-input v-model="node.value" autosize type="textarea" />
          <div style="font-size: 12px;color:green;  white-space: pre-wrap; word-break: break-word; ">{{ node.note }}
          </div>
        </el-form-item>
      </div>
      <el-form-item label="状态：" prop="state" v-if="isShowform.state" :rules="v.getRules(true)">
        <el-switch v-model="form.state" active-value="1" inactive-value="0" />
      </el-form-item>
      <el-form-item label="备注：" prop="note" v-if="isShowform.note" :rules="v.getRules(false, 0, 256)">
        <el-input v-model="form.note" autosize type="textarea" />
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
import { inject, onMounted, reactive, ref, watch } from 'vue';
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
const impls = reactive([{ classKey: '', title: '', sfileModels: [{ key: '', title: '' }] }]);//备选转换器实现类
const filemodels = reactive([{ key: '', title: '' }]);//原文件模型

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
  id: true,//主鍵
  classkey: true,//实现类
  sfilemodel: true,//来源model
  tfilemodel: true,//目标model
  title: true,//标题
  state: true,//状态
  note: true,//备注
  params: true,//参数
});

// 表单默认值
const form = reactive({
  id: '',
  classkey: '',
  sfilemodel: '',
  tfilemodel: '',
  title: '',
  state: '1',
  note: '',
  params: [{ field: '', title: '', note: '', value: '' }],
});

// 监听单个 ref
watch(form, (newVal) => {
  for (const node of impls) {
    if (node.classKey == newVal.classkey) {
      filemodels.length = 0;
      Object.assign(filemodels, node.sfileModels);
    }
  }
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
    id: false,
    classkey: true,
    sfilemodel: true,
    tfilemodel: false,
    title: false,
    state: true,
    note: false,
    params: false,
  });
  if (formParam) {
    //处理创建窗口时传入数据
    loadImpls();
  }
}


/**
 * 加载转换器可用实现类
 */
const loadImpls = () => {
  farm2Request.submit('api/wdapconvertor/impls',
    RequestTypeEnum.post, isLoading).then((response: DataResponse) => {
      // console.log(response.data);
      Object.assign(impls, response.data)
    }).catch((msg: string) => {
      notice.byError(msg);//报错
    });
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
      id: false,
      classkey: false,
      sfilemodel: false,
      tfilemodel: false,
      title: true,
      state: true,
      note: true,
      params: true,
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
    Object.assign(isShowform, {
      id: true,
      classkey: true,
      sfilemodel: true,
      tfilemodel: true,
      title: true,
      state: true,
      note: true,
      params: true,
    });
  }
}

/**
 * 展示信息（加载用户信息）
 */
const viewData = async (id: string) => {
  farm2Request.submit('api/wdapconvertor/' + id, RequestTypeEnum.get, isLoading).then((response: DataResponse) => {
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
    farm2Request.submit('api/wdapconvertor', RequestTypeEnum.post, isLoading, form).then(() => {
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
    farm2Request.submit('api/wdapconvertor/' + form.id, RequestTypeEnum.put, isLoading, form).then(() => {
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
