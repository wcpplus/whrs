<template>
  <el-drawer v-model="formCtrl.isDrawerOpen" class="farm2-data-form" :append-to-body="true" :with-header="false" size="500px">
    <div class="title">{{ currentFormType.type.title }}数据</div>
    <el-form :show-message="formCtrl.isShowValidMsg" v-loading="isLoading" :model="form" label-width="auto" ref="ruleFormRef" :disabled="!formCtrl.isAbledForm">
          <el-form-item label="主鍵:" prop="id" v-if="isShowform.id" :rules="v.getRules(true, 0, 16)">
        <el-input v-model="form.id" />
      </el-form-item>
      <el-form-item label="创建时间:" prop="ctime" v-if="isShowform.ctime" :rules="v.getRules(true, 0, 7)">
        <el-input v-model="form.ctime" />
      </el-form-item>
      <el-form-item label="USERKEY:" prop="userkey" v-if="isShowform.userkey" :rules="v.getRules(false, 0, 32)">
        <el-input v-model="form.userkey" />
      </el-form-item>
      <el-form-item label="考勤日期:" prop="attendancetime" v-if="isShowform.attendancetime" :rules="v.getRules(true, 0, 7)">
        <el-input v-model="form.attendancetime" />
      </el-form-item>
      <el-form-item label="工作时长：" prop="workhours" v-if="isShowform.workhours" :rules="v.getRules(true)">
        <el-select v-model="form.workhours" placeholder="请选择"> 
          <el-option label="~" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="迟到分钟:" prop="latem" v-if="isShowform.latem" :rules="v.getRules(false)">
        <el-input-number v-model="form.latem" />
      </el-form-item>
      <el-form-item label="早退分钟:" prop="earlym" v-if="isShowform.earlym" :rules="v.getRules(false)">
        <el-input-number v-model="form.earlym" />
      </el-form-item>
      <el-form-item label="加班时长:" prop="overtimem" v-if="isShowform.overtimem" :rules="v.getRules(false)">
        <el-input-number v-model="form.overtimem" />
      </el-form-item>
      <el-form-item label="是否缺勤：" prop="absentis" v-if="isShowform.absentis" :rules="v.getRules(false)">
        <el-select v-model="form.absentis" placeholder="请选择"> 
          <el-option label="~" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="原始状态:" prop="exceptiontype" v-if="isShowform.exceptiontype" :rules="v.getRules(false, 0, 16)">
        <el-input v-model="form.exceptiontype" />
      </el-form-item>
      <el-form-item label="最终状态:" prop="state" v-if="isShowform.state" :rules="v.getRules(false, 0, 16)">
        <el-input v-model="form.state" />
      </el-form-item>
      <el-form-item label="豁免原因：" prop="exemptnote" v-if="isShowform.exemptnote" :rules="v.getRules(false, 0, 128)">
        <el-input v-model="form.exemptnote" autosize type="textarea" />
      </el-form-item>
      <el-form-item label="是否工作日：" prop="working" v-if="isShowform.working" :rules="v.getRules(false)">
        <el-select v-model="form.working" placeholder="请选择"> 
          <el-option label="~" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="是否归档：" prop="backup" v-if="isShowform.backup" :rules="v.getRules(false)">
        <el-select v-model="form.backup" placeholder="请选择"> 
          <el-option label="~" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="上午上班时间:" prop="sstime" v-if="isShowform.sstime" :rules="v.getRules(false, 0, 7)">
        <el-input v-model="form.sstime" />
      </el-form-item>
      <el-form-item label="上午下班时间:" prop="sxtime" v-if="isShowform.sxtime" :rules="v.getRules(false, 0, 7)">
        <el-input v-model="form.sxtime" />
      </el-form-item>
      <el-form-item label="下午上班时间:" prop="xstime" v-if="isShowform.xstime" :rules="v.getRules(false, 0, 7)">
        <el-input v-model="form.xstime" />
      </el-form-item>
      <el-form-item label="下午下班时间:" prop="xxtime" v-if="isShowform.xxtime" :rules="v.getRules(false, 0, 7)">
        <el-input v-model="form.xxtime" />
      </el-form-item>
      <el-form-item>
        <el-row class="farm2-form-button">
          <el-col :span="14">
            <el-button v-if="formCtrl.isShowSubmit" type="primary" @click="onSubmitForm(addData)">提交{{ currentFormType.type.title }}</el-button>
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
  isShowValidMsg:true,//是否展示校验信息
});

//是否展示字段
const isShowform = reactive({
  id: true,//主鍵
  ctime: true,//创建时间
  userkey: true,//USERKEY
  attendancetime: true,//考勤日期
  workhours: true,//工作时长
  latem: true,//迟到分钟
  earlym: true,//早退分钟
  overtimem: true,//加班时长
  absentis: true,//是否缺勤
  exceptiontype: true,//原始状态
  state: true,//最终状态
  exemptnote: true,//豁免原因
  working: true,//是否工作日
  backup: true,//是否归档
  sstime: true,//上午上班时间
  sxtime: true,//上午下班时间
  xstime: true,//下午上班时间
  xxtime: true,//下午下班时间
});

// 表单默认值
const form = reactive({
  id: '',
  ctime: '',
  userkey: '',
  attendancetime: '',
  workhours: '',
  latem: 1,
  earlym: 1,
  overtimem: 1,
  absentis: '',
  exceptiontype: '',
  state: '',
  exemptnote: '',
  working: '',
  backup: '',
  sstime: '',
  sxtime: '',
  xstime: '',
  xxtime: '',
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
  formCtrl.isShowValidMsg=true;
  Object.assign(isShowform, {
    id: true,
    ctime: true,
    userkey: true,
    attendancetime: true,
    workhours: true,
    latem: true,
    earlym: true,
    overtimem: true,
    absentis: true,
    exceptiontype: true,
    state: true,
    exemptnote: true,
    working: true,
    backup: true,
    sstime: true,
    sxtime: true,
    xstime: true,
    xxtime: true,
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
    formCtrl.isShowValidMsg=true;
    viewData(id);
    Object.assign(isShowform, {
        id: true,
        ctime: true,
        userkey: true,
        attendancetime: true,
        workhours: true,
        latem: true,
        earlym: true,
        overtimem: true,
        absentis: true,
        exceptiontype: true,
        state: true,
        exemptnote: true,
        working: true,
        backup: true,
        sstime: true,
        sxtime: true,
        xstime: true,
        xxtime: true,
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
    formCtrl.isShowValidMsg=false;
    viewData(id);
    Object.assign(isShowform, {
        id: true,
        ctime: true,
        userkey: true,
        attendancetime: true,
        workhours: true,
        latem: true,
        earlym: true,
        overtimem: true,
        absentis: true,
        exceptiontype: true,
        state: true,
        exemptnote: true,
        working: true,
        backup: true,
        sstime: true,
        sxtime: true,
        xstime: true,
        xxtime: true,
      });
  }
}

/**
 * 展示信息（加载用户信息）
 */
const viewData = async (id: string) => {
  farm2Request.submit('api/attendancesummary/' + id, RequestTypeEnum.get, isLoading).then((response: DataResponse) => {
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
    farm2Request.submit('api/attendancesummary', RequestTypeEnum.post, isLoading, form).then(() => {
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
    farm2Request.submit('api/attendancesummary/' + form.id, RequestTypeEnum.put, isLoading, form).then(() => {
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
