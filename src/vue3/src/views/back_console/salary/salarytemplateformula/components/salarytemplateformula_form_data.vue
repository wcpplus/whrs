<template>
  <el-drawer v-model="formCtrl.isDrawerOpen" class="farm2-data-form" :append-to-body="true" :with-header="false"
    size="800px">
    <div class="title">{{ currentFormType.type.title }}数据</div>
    <el-form :show-message="formCtrl.isShowValidMsg" v-loading="isLoading" :model="form" label-width="auto"
      ref="ruleFormRef" :disabled="!formCtrl.isAbledForm">
      <el-form-item label="主鍵:" prop="id" v-if="isShowform.id" :rules="v.getRules(true, 0, 16)">
        <el-input v-model="form.id" />
      </el-form-item>
      <el-form-item label="步骤:" prop="stepcode" v-if="isShowform.stepcode" :rules="v.getRules(true)">
        <el-input-number v-model="form.stepcode" />
      </el-form-item>
      <el-form-item label="名称:" prop="name" v-if="isShowform.name" :rules="v.getRules(true, 0, 128)">
        <el-input v-model="form.name" autosize type="textarea" />
      </el-form-item>
      <div style="padding: 20px;padding-left: 54px;padding-top: 10px;padding-right: 4px;">
        <el-button type="success" style="width: 100%;"
          @click="itemWinRef.openWin(true, form.templateid)">编辑变量</el-button>
        <el-table :data="items" style="width: 100%;height: 250px;">
          <el-table-column prop="keycode" label="变量" width="180" />
          <el-table-column prop="name" label="名称" width="180" />
          <el-table-column prop="defaultval" label="默认值" />
          <el-table-column prop="userover" label="用户覆盖" :formatter="dic.formatSTATE" />
        </el-table>
      </div>
      <salarytemplateitem_win :refresh-handle="loadTemItems" ref="itemWinRef"></salarytemplateitem_win>
      <el-form-item label="公式:" prop="ruleval" v-if="isShowform.ruleval" :rules="v.getRules(true, 0, 512)">
        <el-input v-model="form.ruleval" :rows="3" type="textarea" placeholder="（AviatorScript）" />
        <div style="color: #999999;"> 本系统使用（AviatorScript）计算引擎</div>
      </el-form-item>
      <el-form-item label="结果名称:" prop="valname" v-if="isShowform.valname" :rules="v.getRules(true, 0, 16)">
        <el-input v-model="form.valname" />
      </el-form-item>
      <el-form-item label="结果存入:" prop="valcode" v-if="isShowform.valcode" :rules="v.getRules(true, 0, 16)">
        <el-select v-model="form.valcode" filterable placeholder="选择数据项" style="width: 240px">
          <el-option v-for="item in items" :key="item.keycode" :label="item.keycode+item.name!" :value="item.keycode" />
        </el-select>
      </el-form-item>
      <el-form-item label="TEMPLATEID:" prop="templateid" v-if="isShowform.templateid" :rules="v.getRules(true, 0, 16)">
        <el-input v-model="form.templateid" />
      </el-form-item>
      <el-form-item label="是否显示：" prop="showmodel" v-if="isShowform.showmodel" :rules="v.getRules(true)">
        <el-select v-model="form.showmodel" placeholder="请选择">
          <el-option label="~" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="排序:" prop="sortcode" v-if="isShowform.sortcode" :rules="v.getRules(true)">
        <el-input-number v-model="form.sortcode" />
      </el-form-item>
      <el-form-item label="备注:" prop="note" v-if="isShowform.note" :rules="v.getRules(true, 0, 256)">
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
            <el-button type="success" @click="runFormula()">运行公式</el-button>
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
import farmClientUtils from '@/hook/farmClientUtils';
import type { SalaryTemplateItem } from '../../salarytemplateitem/utils/salarytemplateitem';
import FarmNotice from '@/components/msg/FarmNotice';
import Salarytemplateitem_win from '../../salarytemplateitem/salarytemplateitem_win.vue';
import dic from '../../salarytemplateitem/utils/dictinarys'
const itemWinRef = ref();
const eventBus = inject('eventBus') as Emitter<Record<EventType, unknown>>;
const ruleFormRef = ref()//表单对象（可调用验证表单，清空表单方法）
const isLoading = ref(false);//加载状态：true|false
const isRetainForm = ref(false);//提交后是否保留表单
const currentFormType = reactive<FormParam>({ type: getViewType(), id: '', data: {} });//表单类型
const items: Array<SalaryTemplateItem> = reactive([]);
//页面控制
const formCtrl = reactive({
  isShowRetain: true,//是否展示（提交后保留表单）选项
  isShowSubmit: true,//是否展示提交按钮
  isDrawerOpen: false,//当前窗口是否打开
  isAbledForm: false,//是否禁用表单
  isShowReset: true,//是否展示清空按钮
  isShowValidMsg: true,//是否展示校验信息
});

//是否展示字段
const isShowform = reactive({
  id: true,//主鍵
  name: true,//名称
  ruleval: true,//公式
  valname: true,//结果名称
  valcode: true,//结果关键字
  templateid: true,//TEMPLATEID
  showmodel: true,//是否显示
  sortcode: true,//排序
  stepcode: true,//步骤排序
  note: true,//备注
});

// 表单默认值
const form = reactive({
  id: '',
  name: '',
  ruleval: '',
  valname: '',
  valcode: '',
  templateid: '',
  showmodel: '',
  sortcode: 1,
  stepcode: 1,
  note: '',
})

const runFormula = () => {
  farmClientUtils.postObject('api/salarytemplateformula/run', { key: form.templateid, val: form.ruleval }, (obj: object) => {
    FarmNotice.bySuccess(obj as unknown as string);
  })
}



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
    loadTemItems();
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
    name: false,
    ruleval: true,
    valname: false,
    valcode: true,
    templateid: false,
    showmodel: false,
    sortcode: false,
    stepcode: true,
    note: true,
  });
  const id2 = formParam as unknown as { id2: string };
  form.templateid = id2.id2;
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
      name: false,
      ruleval: true,
      valname: false,
      valcode: true,
      templateid: false,
      showmodel: false,
      sortcode: false,
      stepcode: true,
      note: true,
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
      name: false,
      ruleval: true,
      valname: false,
      valcode: true,
      templateid: false,
      showmodel: false,
      sortcode: false,
      stepcode: true,
      note: true,
    });
  }
}

/**
 * 展示信息（加载用户信息）
 */
const viewData = async (id: string) => {
  farm2Request.submit('api/salarytemplateformula/' + id, RequestTypeEnum.get, isLoading).then((response: DataResponse) => {
    Object.assign(form, response.data);
    loadTemItems();
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
    farm2Request.submit('api/salarytemplateformula', RequestTypeEnum.post, isLoading, form).then(() => {
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
    farm2Request.submit('api/salarytemplateformula/' + form.id, RequestTypeEnum.put, isLoading, form).then(() => {
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

const loadTemItems = () => {
  farmClientUtils.postList('api/salarytemplateitem/templateItems', { id: form.templateid }, (list: []) => {
    items.length = 0;
    Object.assign(items, list);
  })
}

//------------------------------------------------------------------------------------
onMounted(() => {

  eventBus.on(PageEvent.data_form_open, (paras) => {//注册打开form表单方法
    drawerWinOpen((paras as { isOpen: boolean }).isOpen, (paras as { type: FormParam }).type);
  })
});
</script>
