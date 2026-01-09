<template>
  <el-drawer class="farm2-query-form" v-model="drawerWin" title="I am the title2" :append-to-body="true"
    :with-header="false" size="300px">
    <div class="title">条件查询</div>
    <el-form :model="rules" label-width="auto" style="max-width: 600px">
      <el-form-item label="姓名：">
        <el-input v-model="rules[0].value" />
      </el-form-item>
      <el-form-item label="登录名：">
        <el-input v-model="rules[1].value" />
      </el-form-item>
      <el-form-item label="状态：">
        <el-select v-model="rules[2].value">
          <el-option label="~" value="" />
          <el-option label="可用" value="1" />
          <el-option label="禁用" value="0" />
        </el-select>
      </el-form-item>
      <el-form-item label="类型：">
        <el-select v-model="rules[3].value">
          <el-option label="~" value="" />
          <el-option label="系统用户" value="1" />
          <el-option label="管理员" value="3" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onQuery">查询</el-button>
        <el-button type="info" @click="onClear">清除</el-button>
      </el-form-item>
    </el-form>
  </el-drawer>
</template>
<script setup lang="ts">
import farmFormUtils from '@/hook/farmFormUtils';
import type { DataRule } from '@/types/commons/DataRule';
import type { Emitter, EventType } from 'mitt';
import { inject, onMounted, reactive, ref } from 'vue';
import { PageEvent } from '@/types/commons/PageEvent';
const drawerWin = ref(false);//窗口
const eventBus = inject('eventBus') as Emitter<Record<EventType, unknown>>;
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

/**
 * 将查询条件绑定到表单中
 */
const rules = reactive<DataRule[]>([
  { key: 'a.name', value: '', compara: 'like' },//0
  { key: 'a.loginname', value: '', compara: 'like' },//1
  { key: 'a.state', value: '', compara: '=' },//2
  { key: 'a.type', value: '', compara: '=' }//3
]);

//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
/**
 * 打开或关闭高级查询窗口
 * @param isOpen
 */
const drawerIs = (isOpen: boolean) => {
  drawerWin.value = isOpen;
};
/**
 * 调用执行查询-事件
 */
const onQuery = () => {
  eventBus.emit(PageEvent.data_do_query, farmFormUtils.getRules(rules));
}
/**
 * 清除高级查询条件
 */
const onClear = () => {
  farmFormUtils.clearQueryForm(rules)
  onQuery();
}

onMounted(() => {
  //注册方法
  eventBus.on(PageEvent.query_form_open, (data) => {
    drawerIs(data as boolean);//打开高级查询窗口方法
  });
  eventBus.on(PageEvent.query_form_clear, () => {
    onClear();//清空高级查询表单方法
  });
});
</script>
