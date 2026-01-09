<template>
  <el-drawer v-model="formCtrl.isDrawerOpen" class="farm2-data-form" :append-to-body="true" :with-header="false"
    size="500px">
    <div class="title">{{ currentFormType.type.title }}数据</div>
    <el-form :show-message="formCtrl.isShowValidMsg" v-loading="isLoading" :model="form" label-width="auto"
      ref="ruleFormRef" :disabled="!formCtrl.isAbledForm">
      <el-form-item label="主鍵:" prop="id" v-if="isShowform.id" :rules="v.getRules(true, 0, 16)">
        <el-input v-model="form.id" />
      </el-form-item>
      <el-form-item label="名称:" prop="name" v-if="isShowform.name" :rules="v.getRules(true, 0, 128)">
        <el-input v-model="form.name" autosize type="textarea" />
      </el-form-item>
      <el-form-item label="默认值:" prop="defaultval" v-if="isShowform.defaultval"
        :rules="v.getRegExpRules(v.ValidRxs.salary, true)">
        <el-input v-model="form.defaultval" />
      </el-form-item>
      <el-form-item label="关键字:" prop="keycode" v-if="isShowform.keycode" :rules="v.getRules(true, 0, 16)">
        <el-autocomplete v-model="form.keycode" :fetch-suggestions="querySearch" popper-class="my-autocomplete"  placeholder="可使用预置变量或自定义变量">
          <template #suffix>
            <el-icon class="el-input__icon">
              <edit />
            </el-icon>
          </template>
          <template #default="{ item }">
            <div class="value">{{ item.value }}  <span style="color: green;">   {{ item.note }}</span> </div>
          </template>
        </el-autocomplete>
      </el-form-item>
      <el-form-item label="类型:" prop="componenttype" v-if="isShowform.componenttype" :rules="v.getRules(false, 0, 16)">
        <el-select v-model="form.componenttype" placeholder="请选择">
          <el-option label="收入" value="收入" />
          <el-option label="扣款" value="扣款" />
          <el-option label="税费" value="税费" />
          <el-option label="补贴" value="补贴" />
          <el-option label="社保" value="社保" />
          <el-option label="其他" value="其他" />
        </el-select>
      </el-form-item>
      <el-form-item label="来源类型:" prop="sourcemodel" v-if="isShowform.sourcemodel" :rules="v.getRules(false, 0, 16)">
        <el-input v-model="form.sourcemodel" />
      </el-form-item>
      <el-form-item label="用户覆盖:" prop="userover" v-if="isShowform.userover" :rules="v.getRules(true)">
        <el-select v-model="form.userover" placeholder="请选择">
          <el-option label="手动更新  :  每个用户单独维护" value="1" />
          <el-option label="每月更新  :  每个用户每月更新" value="2" />
          <el-option label="系统配置  :  系统预置数据" value="0" />
          <el-option label="系统变量  :  中间运算数据" value="3" />
        </el-select>
      </el-form-item>
      <el-form-item label="排序:" prop="sortcode" v-if="isShowform.sortcode" :rules="v.getRules(true)">
        <el-input-number v-model="form.sortcode" />
      </el-form-item>
      <el-form-item label="TEMPLATEID:" prop="templateid" v-if="isShowform.templateid" :rules="v.getRules(true, 0, 16)">
        <el-input v-model="form.templateid" />
      </el-form-item>
      <el-form-item label="显示类型:" prop="showmodel" v-if="isShowform.showmodel" :rules="v.getRules(false)">
        <el-select v-model="form.showmodel" placeholder="请选择">
          <el-option label="~" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="备注:" prop="note" v-if="isShowform.note" :rules="v.getRules(false, 0, 256)">
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
  isShowReset: true,//是否展示清空按钮
  isShowValidMsg: true,//是否展示校验信息
});

//是否展示字段
const isShowform = reactive({
  id: false,//主鍵
  note: true,//备注
  name: true,//名称
  defaultval: true,//默认值
  keycode: true,//关键字
  componenttype: true,//类型
  sourcemodel: false,//来源类型
  userover: true,//用户覆盖
  sortcode: true,//排序
  templateid: true,//TEMPLATEID
  showmodel: true,//显示类型
});

// 表单默认值
const form = reactive({
  id: '',
  note: '',
  name: '',
  defaultval: '',
  keycode: '',
  componenttype: '',
  sourcemodel: '',
  userover: '0',
  sortcode: 1,
  templateid: '',
  showmodel: '',
})
interface LinkItem {
  value: string
  note: string
}
const links = ref<LinkItem[]>([])
const querySearch = (queryString: string, cb: (arg0: { value: string; note: string; }[]) => void) => {
  const results = queryString
    ? links.value.filter(createFilter(queryString))
    : links.value
  // call callback function to return suggestion objects
  cb(results)
}
const createFilter = (queryString: string) => {
  return (restaurant: LinkItem) => {
    return (
      restaurant.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0
    )
  }
}

const loadAll = () => {
  return [
    { value: 'GROSS_SALARY_SYS', note: '应发工资' },
    { value: 'INCOME_TAX_SYS', note: '个税总额' },
    { value: 'NET_SALARY_SYS', note: '实发工资' },
  ]
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
    note: true,
    name: true,
    defaultval: true,
    keycode: true,
    componenttype: true,
    sourcemodel: false,
    userover: true,
    sortcode: true,
    templateid: false,
    showmodel: false,
  });
  const templateId = formParam as unknown as { templateId: string };
  if (templateId.templateId) {
    //处理创建窗口时传入数据
    form.templateid = templateId.templateId;
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
      note: true,
      name: true,
      defaultval: true,
      keycode: true,
      componenttype: true,
      sourcemodel: false,
      userover: true,
      sortcode: true,
      templateid: false,
      showmodel: false,
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
      note: true,
      name: true,
      defaultval: true,
      keycode: true,
      componenttype: true,
      sourcemodel: false,
      userover: true,
      sortcode: true,
      templateid: false,
      showmodel: false,
    });
  }
}

/**
 * 展示信息（加载用户信息）
 */
const viewData = async (id: string) => {
  farm2Request.submit('api/salarytemplateitem/' + id, RequestTypeEnum.get, isLoading).then((response: DataResponse) => {
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
    farm2Request.submit('api/salarytemplateitem', RequestTypeEnum.post, isLoading, form).then(() => {
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
    farm2Request.submit('api/salarytemplateitem/' + form.id, RequestTypeEnum.put, isLoading, form).then(() => {
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
  links.value = loadAll();
  eventBus.on(PageEvent.data_form_open, (paras) => {//注册打开form表单方法
    drawerWinOpen((paras as { isOpen: boolean }).isOpen, (paras as { type: FormParam }).type);
  })
});
</script>
