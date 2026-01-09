<template>
  <el-drawer v-model="formCtrl.isDrawerOpen" class="farm2-data-form" :append-to-body="true" :with-header="false"
    size="500px">
    <div class="title">{{ currentFormType.type.title }}角色</div>
    <el-form v-loading="isLoading" :model="form" label-width="auto" ref="ruleFormRef" :disabled="!formCtrl.isAbledForm">

      <el-form-item label="类别：" v-if="props.category?.id">
        {{ props.category.name }}
      </el-form-item>
      <el-form-item label="创建时间:" prop="ctime" v-if="isShowform.ctime" :rules="v.getRules(true, 0, 7)">
        <el-input v-model="form.ctime" />
      </el-form-item>
      <el-form-item label="修改时间:" prop="etime" v-if="isShowform.etime" :rules="v.getRules(true, 0, 7)">
        <el-input v-model="form.etime" />
      </el-form-item>
      <el-form-item label="修改用户:" prop="euserkey" v-if="isShowform.euserkey" :rules="v.getRules(true, 0, 16)">
        <el-input v-model="form.euserkey" />
      </el-form-item>
      <el-form-item label="创建用户:" prop="cuserkey" v-if="isShowform.cuserkey" :rules="v.getRules(true, 0, 16)">
        <el-input v-model="form.cuserkey" />
      </el-form-item>
      <el-form-item label="名称：" prop="name" v-if="isShowform.name" :rules="v.getRules(true, 0, 128)">
        <el-input v-model="form.name" autosize type="textarea" />
      </el-form-item>
      <el-form-item label="编码：" prop="keyid" v-if="isShowform.keyid"
        :rules="v.getRegExpRules(v.ValidRxs.loginname, true, 2, 32)">
        <el-input v-model="form.keyid" autosize type="textarea" />
      </el-form-item>
      <el-form-item label="职级序列:" prop="tracktype" :rules="v.getRules(true)">
        <el-select v-model="form.tracktype" placeholder="">
          <el-option v-for="item in tracktypes" :key="item.keyid" :label="item.name" :value="item.keyid" />
        </el-select>
      </el-form-item>
      <el-form-item label="默认职级:" prop="gradeid" :rules="v.getRules(true)">
        <el-select v-model="form.gradeid" placeholder="">
          <el-option v-for="item in grades.filter(node => !form.tracktype || node.tracktype === form.tracktype)"
            :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="岗位族:" prop="familyid" :rules="v.getRules(false)">
        <el-select v-model="form.familyid" placeholder="">
          <el-option v-for="item in familys" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
        <template #label><el-tooltip class="box-item" effect="dark" content="岗位族在岗位管理界面左侧创建" placement="top-start">
            岗位族:
          </el-tooltip></template>
      </el-form-item>
      <el-form-item label="编制人数：" prop="maxunum">
        <el-input-number v-model="form.maxunum" :min="1" :max="200" />
      </el-form-item>
      <el-form-item label="归属机构：" prop="orgid">
        <el-tree-select :key="treeSelectKey" :check-strictly='true' v-model="form.orgid" lazy :load="loadOrgidTreeNode"
          :props="orgidProps" :default-expanded-keys="['NONE', ...localOrgPath]" style="width: 240px" />
      </el-form-item>
      <el-form-item label="状态：" prop="state" v-if="isShowform.state" :rules="v.getRules(true)">
        <el-select v-model="form.state" placeholder="请选择">
          <el-option label="可用" value="1" />
          <el-option label="禁用" value="0" />
        </el-select>
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
import { inject, onMounted, reactive, ref, type PropType } from 'vue';
import v from '@/hook/farmFormValids';
import farm2Request from '@/service/remoteRequests/Farm2Request';
import { RequestTypeEnum } from '@/types/commons/RequestTypeEnum';
import notice from '@/components/msg/FarmNotice';
import { getCreatType, getUpdateType, getViewType, type FormParam } from '@/types/commons/FormType';
import type { DataResponse } from '@/types/commons/DataResponse';
import type { Emitter, EventType } from 'mitt';
import { PageEvent } from '@/types/commons/PageEvent';
import farmFormUtils from '@/hook/farmFormUtils';
import type { AuthFamily } from '../../authfamily/utils/authfamily';
import farmClientUtils from '@/hook/farmClientUtils';
import farmDicEntityUtils from '@/hook/farmDicEntityUtils';
import type { DicType } from '../../dictype/utils/dictype';
import type { AuthGrade } from '../../authgrade/utils/authgrade';
import type { DataResult } from '@/types/commons/DataResult';
import type { LocalOrg } from '../../localorg/utils/localorg';
const localOrgPath = ref<string[]>([]);
const orgidProps = {
  children: 'children',
  label: 'NAME',
  isLeaf: 'isLeaf'
}
const loading = ref(false);
const tracktypes: Array<DicType> = reactive([]);
const grades: Array<AuthGrade> = reactive([]);
const eventBus = inject('eventBus') as Emitter<Record<EventType, unknown>>;
const ruleFormRef = ref()//表单对象（可调用验证表单，清空表单方法）
const isLoading = ref(false);//加载状态：true|false
const isRetainForm = ref(false);//提交后是否保留表单
const currentFormType = reactive<FormParam>({ type: getViewType(), id: '', data: {} });//表单类型
const familys: Array<AuthFamily> = reactive([]);
const treeSelectKey = ref(0);
const props = defineProps({
  category: {
    type: Object as PropType<{ id: string, name: string }>,
    required: false
  }
});
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
  ctime: true,//创建时间
  etime: true,//修改时间
  euserkey: true,//修改用户
  cuserkey: true,//创建用户
  state: true,//状态
  note: true,//备注
  name: true,//名称
  keyid: true,//编码
});

// 表单默认值
const form = reactive({
  categoryId: '',
  id: '',
  ctime: '',
  etime: '',
  euserkey: '',
  cuserkey: '',
  state: '',
  note: '',
  name: '',
  keyid: '',
  familyid: '',
  gradeid: '',
  tracktype: '',
  maxunum: 0,
  orgid: '',
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
    treeSelectKey.value++;
    loadFamilys();
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
    farmDicEntityUtils.getOptions('TRACK_TYPES', (types: Array<DicType>) => {
      Object.assign(tracktypes, types);
    })
    loadGrads();
  }
};

const loadOrgPath = () => {
  if (form.orgid && form.orgid != 'NONE') {
    farmClientUtils.postList('api/localorg/loadOrgPath', { id: form.orgid }, (list: []) => {
      const types = list as Array<LocalOrg>;
      localOrgPath.value = types.map(node => node.id!);
    }, loading);
  }
}

const loadFamilys = () => {
  farmClientUtils.postList('api/authfamily/lists', {}, (list: []) => {
    Object.assign(familys, list);
  })
}
const loadGrads = () => {
  farmClientUtils.postList('api/authgrade/lists', {}, (list: []) => {
    Object.assign(grades, list);
  })
}
const loadOrgidTreeNode = (node: { data: { ID: string } }, resolve: (data: unknown) => void) => {
  const theRules = [];
  if (node.data.ID) {
    theRules.push({ key: 'parentid', value: node.data.ID, compara: '=' });
  }
  farm2Request.search("api/localorg/tree", { rules: theRules })
    .then((remoteResult: DataResult) => {
      // 对返回的数据进行处理，给 id 为 'NONE' 的节点加上 disabled
      const processedData = (remoteResult.data || []).map((item) => {
        const treeNode = item as { ID: string };
        if (treeNode.ID === 'NONE') {
          return { ...treeNode, disabled: true };
        }
        return item;
      });
      resolve(processedData);
    })
    .catch((msg: Error) => {
      notice.byError(msg.message);
    });
};
/**
 * 初始化创建窗口
 */
const initCreatForm = (formParam: FormParam) => {
  farmFormUtils.initFormCtrl(getCreatType(), formCtrl);
  Object.assign(isShowform, {
    ctime: false,
    etime: false,
    euserkey: false,
    cuserkey: false,
    state: true,
    note: true,
    name: true,
    keyid: true,
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
  Object.assign(isShowform, {
    ctime: false,
    etime: false,
    euserkey: false,
    cuserkey: false,
    state: true,
    note: true,
    name: true,
    keyid: true,
  });
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
  Object.assign(isShowform, {
    ctime: true,
    etime: true,
    euserkey: true,
    cuserkey: true,
    state: true,
    note: true,
    name: true,
    keyid: true,
  });
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
  farm2Request.submit('api/post/' + id, RequestTypeEnum.get, isLoading).then((response: DataResponse) => {
    Object.assign(form, response.data);
    loadOrgPath();
  }).catch((msg: Error) => {
    notice.byError(msg.message);//报错
  });
}

/**
 * 执行提交按钮（创建/更新）
 */
const addData = async () => {
  //创建提交
  if (currentFormType.type.key == getCreatType().key) {
    if (props.category?.id) {
      form.categoryId = props.category?.id;
    } else {
      form.categoryId = '';
    }
    farm2Request.submit('api/post', RequestTypeEnum.post, isLoading, form).then(() => {
      notice.bySuccess("创建成功!");//提示成功
      eventBus.emit(PageEvent.data_do_query);//刷新列表
      if (!isRetainForm.value) {//保留窗口数据
        onResetForm();//清空
        drawerWinOpen(false);//关闭
      }
    }).catch((msg: Error) => {
      notice.byError(msg.message);//报错
    });
  }
  //更新提交
  if (currentFormType.type.key == getUpdateType().key) {
    farm2Request.submit('api/post/' + form.id, RequestTypeEnum.put, isLoading, form).then(() => {
      notice.bySuccess("更新成功!");//提示成功
      eventBus.emit(PageEvent.data_do_query);//刷新列表
      onResetForm();//清空
      drawerWinOpen(false);//关闭
    }).catch((msg: Error) => {
      notice.byError(msg.message);//报错
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
