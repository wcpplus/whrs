<template>
  <el-drawer v-model="drawer1" class="farm2-data-form" :append-to-body="true" :with-header="false" size="500px">
    <div class="title">{{ typeTitle }}用户</div>
    <el-form v-loading="isLoading" :model="form" label-width="auto" ref="ruleFormRef" :disabled="!isAbledForm">
      <el-form-item label="姓名：" prop="name" :rules="v.getRules(true, 2, 16)">
        <el-input v-model="form.name" />
      </el-form-item>
      <el-form-item label="登录名：" prop="loginname" v-show="isShowLoginname"
        :rules="v.getRegExpRules(v.ValidRxs.loginname, true, 2, 32)">
        <el-input v-model="form.loginname" />
      </el-form-item>
      <el-form-item label="组织机构：" prop="orgid" :rules="v.getRules(true)">
        <el-tree-select :key="treeSelectKey" :check-strictly='true' v-model="form.orgid" lazy :load="loadOrgidTreeNode"
          :props="orgidProps" @change="loadPost()" :default-expanded-keys="['NONE', ...localOrgPath]" />
      </el-form-item>
      <el-form-item label="用户岗位：" prop="post">
        <div
          style="display: flex;min-height: 30px; width: 100%;border:1px solid #d4d5da;padding: 1px 10px;border-radius: 4px;"
          :style="{ backgroundColor: isAbledForm ? '' : '#f5f7fa' }">
          <div style="flex:1"> <el-tag size="small" v-for="tag in posts" :key="tag.KEYID" :closable="isAbledForm"
              type="success" @close="() => {
                const index = posts.findIndex(itemTag => itemTag.KEYID === tag.KEYID);
                if (index !== -1) {
                  posts.splice(index, 1);
                }
              }">
              {{ tag.NAME }}
            </el-tag></div>
          <div style="margin-left: auto;" v-if="isAbledForm"> <el-button size="small" circle type="success"
              @click="postChooseRef.openWin(true)" icon="Search" /></div>
        </div>
      </el-form-item>
      <el-form-item label="用户类型：" prop="type" :rules="v.getRules(true)">
        <el-select v-model="form.type" placeholder="请选择">
          <el-option label="系统用户" value="1" />
          <el-option label="超级管理员" value="3" />
        </el-select>
      </el-form-item>
      <el-form-item label="职级:" prop="gradeid" :rules="v.getRules(true)">
        <el-select v-model="form.gradeid" placeholder="可检索职级" filterable>
          <el-option v-for="item in grades" :key="item.id" :label="item.keyid + ':' + item.name" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="待入职：" prop="state" :rules="v.getRules(true)">
        <el-switch v-model="form.state" active-value="0" inactive-value="1" />
      </el-form-item>
      <el-form-item label="备注：" prop="note" :rules="v.getRules(false, 0, 256)">
        <el-input v-model="form.note" autosize type="textarea" />
      </el-form-item>
      <el-form-item>
        <el-row class="farm2-form-button">
          <el-col :span="14">
            <el-button v-if="isShowSubmit" type="primary" @click="onSubmitForm(addData)">提交{{ typeTitle }}</el-button>
            <el-switch v-if="isShowRetain" v-model="isRetainForm" size="small" inactive-text="提交后保留表单" />
          </el-col>
          <el-col :span="10" class="farm2-right">
            <el-button v-if="isShowReset" type="warning" @click="onResetForm">清空</el-button>
            <el-button v-if="isShowSubmit" type="info" plain @click="drawerWinOpen(false)">取消</el-button>
          </el-col>
        </el-row>
      </el-form-item>
    </el-form>
    <post_choose ref="postChooseRef" :choosehandle="chooseHandle"></post_choose>
  </el-drawer>
</template>
<script lang="ts" setup>
//------------------------------------------------------------------------------------
import { computed, inject, onMounted, reactive, ref } from 'vue';
import v from '@/hook/farmFormValids';
import farm2Request from '@/service/remoteRequests/Farm2Request';
import { RequestTypeEnum } from '@/types/commons/RequestTypeEnum';
import notice from '@/components/msg/FarmNotice';
import { getCreatType, getUpdateType, getViewType, type FormParam } from '@/types/commons/FormType';
import type { DataResponse } from '@/types/commons/DataResponse';
import type { Emitter, EventType } from 'mitt';
import { PageEvent } from '@/types/commons/PageEvent';
import type { DataResult } from '@/types/commons/DataResult';
import type { LocalOrg } from '../../localorg/utils/localorg';
import farmClientUtils from '@/hook/farmClientUtils';
import Post_choose from '../../post/post_choose.vue';
import type { AuthGrade } from '../../authgrade/utils/authgrade';
const localOrgPath = ref<string[]>([]);
const loading = ref(false);
const treeSelectKey = ref(0);
const posts: Array<{ KEYID: string; NAME: string; ID: string }> = reactive([]);
const postChooseRef = ref();
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
const chooseHandle = (
  keys: string[],
  choosePosts: Array<{ KEYID: string; NAME: string; ID: string }>
) => {
  const existingIds = new Set(posts.map(p => p.ID));
  const newPosts = choosePosts.filter(post => !existingIds.has(post.ID));
  if (newPosts.length > 0) {
    posts.push(...newPosts);
    const newIds = newPosts.map(p => p.ID);
    form.post.push(...newIds); // ✅ 现在不会报错了
  }
};
/**
 * 角色选项表数据加载中
 */
const isPostLoading = ref(false);
/**
 * 角色选项表的选项值
 */
const postOptions = ref<{ ID: string, NAME: string }[]>([]);
/**
 *加载角色选项表
 */
const loadPost = () => {
  farm2Request.submit("api/post/ables", RequestTypeEnum.post, isPostLoading, {})
    .then((response: DataResponse) => {
      Object.assign(postOptions.value, response.data);
    })
    .catch((msg: Error) => {
      notice.byError(msg.message);
    });
};
const loadOrgPath = () => {
  if (form.orgid && form.orgid != 'NONE') {
    farmClientUtils.postList('api/localorg/loadOrgPath', { id: form.orgid }, (list: []) => {
      const types = list as Array<LocalOrg>;
      localOrgPath.value = types.map(node => node.id!);
    }, loading);
  }
}

const orgidProps = {
  children: 'children',
  label: 'NAME',
  isLeaf: 'isLeaf'
}
const eventBus = inject('eventBus') as Emitter<Record<EventType, unknown>>;
const drawer1 = ref(false);
//是否禁用表单
const isAbledForm = ref(false);
//表单对象（可调用验证表单，清空表单方法）
const ruleFormRef = ref()
//加载状态：true|false
const isLoading = ref(false);
//提交后是否保留表单
const isRetainForm = ref(false);
//表单类型
const currentFormType = reactive<FormParam>({ type: getViewType(), id: '', data: {} });
//表单类型名：创建/更新/浏览
const typeTitle = computed(() => {
  return currentFormType.type.title;
});;
//是否展示清空按钮
const isShowReset = computed(() => {
  return currentFormType.type.key == getCreatType().key;
});
//是否展示清空按钮
const isShowRetain = ref(true);

//是否展示登录名称
const isShowLoginname = ref(true);

//是否展示提交按钮
const isShowSubmit = ref(true);



// 表单默认值
const form = reactive({
  id: '',
  post: [] as Array<string>,
  loginname: '',
  name: '',
  type: '1',
  state: '0',
  note: '',
  orgid: '',
  gradeid: '',
})
const grades: Array<AuthGrade> = reactive([]);

/**
 * 打开或关闭表单窗口
 * @param isOpen
 * @param formType
 */
const drawerWinOpen = (isOpen: boolean, formType?: FormParam) => {
  if (!isOpen) {
    drawer1.value = isOpen;
  } else {
    treeSelectKey.value++;
    loadPost();
    Object.assign(currentFormType, formType);
    if (formType?.type.key == getCreatType().key) {
      initCreatForm();//创建
    }
    if (formType?.type.key == getUpdateType().key) {
      initUpdateForm(formType.id); //更新
    }
    if (formType?.type.key == getViewType().key) {
      initViewForm(formType.id);//浏览
    }
    loadGrads();
  }
};
const loadGrads = () => {
  farmClientUtils.postList('api/authgrade/lists', {}, (list: []) => {
    Object.assign(grades, list);
  })
}
/**
 * 初始化创建窗口
 */
const initCreatForm = () => {
  isShowSubmit.value = true;
  drawer1.value = true;
  isShowLoginname.value = true;
  form.id = '';
  isShowRetain.value = true;
  isAbledForm.value = true;
}

/**
 * 初始化更新窗口
 * @param id
 */
const initUpdateForm = (id: string | undefined) => {
  isShowSubmit.value = true;
  drawer1.value = true;
  isShowRetain.value = false;
  isShowLoginname.value = false;
  isAbledForm.value = true;
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
  isShowSubmit.value = false;
  drawer1.value = true;
  isShowRetain.value = false;
  isAbledForm.value = false;
  isShowLoginname.value = true;
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
  farm2Request.submit('api/localuser/' + id, RequestTypeEnum.get, isLoading).then((response: DataResponse) => {
    Object.assign(form, response.data);
    const dataPlus = response.data as { posts: Array<{ id: string, keyid: string, name: string }> };
    posts.length = 0;
    Object.assign(posts, dataPlus.posts.map(node => ({ KEYID: node.keyid, NAME: node.name, ID: node.id })))
    loadOrgPath();
  }).catch((msg: Error) => {
    notice.byError(msg.message);//报错
  });
}

/**
 * 执行提交按钮（创建/更新）
 */
const addData = async () => {
  {
    form.post.length = 0;
    Object.assign(form.post, posts.map(node => node.ID));
  }
  //创建提交
  if (currentFormType.type.key == getCreatType().key) {
    farm2Request.submit('api/localuser', RequestTypeEnum.post, isLoading, form).then(() => {
      notice.bySuccess("创建成功!");//提示成功
      reQuery();//刷新列表
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
    farm2Request.submit('api/localuser/' + form.id, RequestTypeEnum.put, isLoading, form).then(() => {
      notice.bySuccess("更新成功!");//提示成功
      reQuery();//刷新列表
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
 * 执行查询（提交表单后刷新数据）
 */
const reQuery = () => {
  eventBus.emit(PageEvent.data_do_query)
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
