<template><el-dialog v-model="dialogFormVisible" title="请设置权限" width="380">
    <div class="farm-tree-choose"
      style="padding: 20px;background-color: var(--el-dialog-bg-color) ;border-radius: 1em;">
      <!-- 复选框权限树 -->
      <el-tree v-loading="loadingFlag" ref="treeRef" style="max-width: 600px" :data="treeData" show-checkbox
        :default-expanded-keys="['FRAME', 'APP']" node-key="id" highlight-current :check-on-click-node="true"
        :expand-on-click-node="false" :props="defaultTreeProps"><template v-slot="{ node, data }">
          <!--此处可自定义图标-->
          <el-icon v-if="data.icon" :size="16">
            <span v-html="data.icon"></span>
          </el-icon>
          {{ node.label }}
        </template></el-tree>
    </div>
    <div style="text-align: center;">
      <el-button-group style="margin-top: 20px;">
        <el-button type="primary" @click="getCheckedKeys">保存</el-button>
        <el-button type="danger" @click="resetChecked" plain>清空</el-button>
        <el-button type="primary" @click="getRemoteAllTree" plain>刷新</el-button>
      </el-button-group>
    </div>
  </el-dialog>
</template>
<script setup lang="ts">
import farm2Request from '@/service/remoteRequests/Farm2Request';
import notice from '@/components/msg/FarmNotice';
import { MittKesEnum } from '../utils/mittKeys'
import { inject, reactive, ref } from 'vue';
import { RequestTypeEnum } from '@/types/commons/RequestTypeEnum';
import type { Emitter, EventType } from 'mitt';
import { PageEvent } from '@/types/commons/PageEvent';
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
const eventBus = inject('eventBus') as Emitter<Record<EventType, unknown>>;
const dialogFormVisible = ref(false)//显示树组件
const defaultTreeProps = {
  children: 'children',
  label: 'label',
  disabled: 'disabled'
}
let currentPostid: string;
const treeRef = ref()
const loadingFlag = ref(false);//加载中遮罩
/**
 * 保存
 */
const getCheckedKeys = () => {
  farm2Request.submit("api/post/setactions", RequestTypeEnum.post, loadingFlag, { id: currentPostid, ids: treeRef.value!.getCheckedKeys(false) }).then(() => {
    notice.bySuccess("更新成功!");//提示成功
    eventBus.emit(PageEvent.data_do_query);//刷新列表
    dialogFormVisible.value = false;
  }).catch((msg: Error) => {
    notice.byError(msg.message);
  });
}
/**
 * 清空
 */
const resetChecked = () => {
  treeRef.value!.setCheckedKeys([], false)
}
/**
 * 加载远程树
 */
const getRemoteAllTree = () => {
  farm2Request.search("api/actions/alltree", {})
    .then((remoteResult) => {
      Object.assign(treeData, remoteResult);
      farm2Request.submit("api/post/getactions", RequestTypeEnum.post, loadingFlag, { id: currentPostid })
        .then((result) => {
          treeRef.value!.setCheckedKeys(result.data, false)
        }).catch((msg: Error) => {
          notice.byError(msg.message);
        });
    })
    .catch((msg: Error) => {
      notice.byError(msg.message);
    });
}
eventBus.on(MittKesEnum.openActionsSettingForm, (postid) => {//注册打开form表单方法
  dialogFormVisible.value = true;
  currentPostid = postid as string;
  getRemoteAllTree();
})
const treeData = reactive([]);
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
</script>
