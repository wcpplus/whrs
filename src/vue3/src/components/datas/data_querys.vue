<template>
  <div class="farm2-data-querys-operators">
    <!-- 👇 默认插槽：允许父组件插入自定义内容 -->
    <slot></slot>
    <!--默认查询框，从外部传入条件提示-->
    <el-input v-if="props.queryModel == 'default'" :clearable="hasDefaultSlot" :placeholder="props.placeholder"
      @keyup.enter="onQueryClick" v-model="defaultRule" class="input-with-select">
      <template #append>
        <el-button @click="onQueryClick" icon="Search" />
      </template>
      <template #prepend v-if="!hasDefaultSlot">
        <el-button @click="onClearClick" icon="Delete" />
      </template>
    </el-input>
    <el-popover v-if="props.queryModel == 'user'" class="box-item" placement="bottom" width="230">
      <div v-for="value in users" :key="value.loginname" class="skc-query-rule-list"
        @click="chooseUserRule(value.loginname)">
        {{ value.name }}:{{ value.loginname }}
      </div>
      <div style="text-align: center;color:#aaaaaa" v-if="users.length <= 0">
        请录入用户名称或登录名
      </div>
      <template #reference>
        <el-input placeholder="用户姓名" @keyup.enter="onUserQueryClick" v-model="defaultRule" class="input-with-select">
          <template #append>
            <el-button @click="onUserQueryClick" icon="Search" />
          </template>
          <template #prepend v-if="!hasDefaultSlot">
            <el-button @click="onClearClick" icon="Delete" />
          </template>
        </el-input></template>
    </el-popover>
    <el-button v-if="props.isShowQueryPlus" type="primary" size="small" title="组合查询" @click="openQueryFormClick" circle
      :plain="true">
      <el-icon>
        <Filter />
      </el-icon>
    </el-button>
    <br />
  </div>
</template>
<script setup lang="ts">
import farmClientUtils from '@/hook/farmClientUtils';
import type { DataQuery } from '@/types/commons/DataQuery';
import { PageEvent } from '@/types/commons/PageEvent';
import type { Emitter, EventType } from 'mitt';
import { inject, reactive, ref, type PropType } from 'vue';
import { useSlots } from 'vue'

const slots = useSlots()
const hasDefaultSlot = !!slots.default
// 定义枚举类型
type AppModel = 'default' | 'user'
const props = defineProps({
  placeholder: {
    type: String,
    required: true,
  },
  isShowQueryPlus: {
    type: Boolean,
    required: true,
  },
  queryModel: {
    type: String as PropType<AppModel>,
    required: false,
    default: 'default'
  },
});
const eventBus = inject('eventBus') as Emitter<Record<EventType, unknown>>;
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

const defaultRule = ref('');
const users = reactive([] as Array<{ name: string, loginname: string }>);

//执行查询
const onQueryClick = () => {
  const query: DataQuery = { rules: [] };
  query.rules?.push({ key: 'default', value: defaultRule.value, compara: 'like' });
  eventBus.emit(PageEvent.data_do_query, query)
}
const onUserQueryClick = () => {
  if (defaultRule.value) {
    farmClientUtils.postList("api/search/user", { word: defaultRule.value }, (list: []) => {
      users.length = 0;
      Object.assign(users, list);
    });
  } else {
    onQueryClick();
  }
}

//清理条件
const onClearClick = () => {
  defaultRule.value = '';
  onQueryClick();
  eventBus.emit(PageEvent.query_form_clear);
}

//打开高级查询窗口
const openQueryFormClick = () => {
  eventBus.emit(PageEvent.query_form_open, true);
}
const chooseUserRule = (userkey: string) => {
  defaultRule.value = userkey;
  const query: DataQuery = { rules: [] };
  query.rules?.push({ key: 'default', value: defaultRule.value, compara: 'like' });
  eventBus.emit(PageEvent.data_do_query, query)
}

//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
</script>
<style scoped>
/* 样式可以根据需要添加 */
.skc-query-rule-list {
  cursor: pointer;
  padding: 10px;
  border-radius: 10px;

}

.skc-query-rule-list:hover {
  background-color: var(--el-farms-win-bg-color);
}
</style>
