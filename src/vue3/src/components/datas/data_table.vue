<template>
  <div class="farm2-data-table">
    <el-table :data="props.remoteData.data" style="width: 100%" :stripe="true" :highlight-current-row="true" class="table">
      <el-table-column type="selection" width="55" />
      <el-table-column sortable prop="NAME" label="姓名" width="150" />
      <el-table-column sortable prop="LOGINNAME" label="登录名" width="120" />
      <el-table-column prop="STATE" :formatter="formatSTATE" label="状态" width="120" />
      <el-table-column prop="TYPE" :formatter="formatTYPE" label="类型" width="120" />
      <el-table-column prop="NOTE" label="备注" width="600" />
      <el-table-column fixed="right" label="操作" min-width="140">
        <template #default>
          <el-button link type="primary" icon="Edit" size="small" @click="handleClick">
            修改
          </el-button>
          <el-button link type="success" icon="Delete" size="small">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>
<script setup lang="ts">
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>定义>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
import type { DataResult } from '@/types/commons/DataResult';
const props = defineProps<{ remoteData: DataResult }>();
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>转义>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
const formatSTATE = (_row: never, _column: never, cellValue: string) => {
  const dic: { [key: string]: string } = {
    "0": "禁用",
    "1": "可用"
  };
  return dic[cellValue] || "未知";
};

const formatTYPE = (_row: never, _column: never, cellValue: string) => {
  const dic: { [key: string]: string } = {
    "3": "管理员",
    "1": "系统用户"
  };
  return dic[cellValue] || "未知";
};
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
const handleClick = () => {
  console.log('click')
}
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
</script>
<style scoped>
/* 样式可以根据需要添加 */
</style>
