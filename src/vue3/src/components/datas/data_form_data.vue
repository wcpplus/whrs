<template>
  <el-drawer v-model="drawer1" class="farm2-data-form" :append-to-body="true" :with-header="false" size="500px">
    <div class="title">Demo模板.用户</div>
    <el-form :model="form" label-width="auto" ref="ruleFormRef">
      <el-form-item label="登录名：" prop="loginname" :rules="[
        { required: true, message: '必填项', trigger: 'blur' },
        { min: 3, max: 5, message: '长度要求： 3 - 5', trigger: 'blur' },
      ]">
        <el-input v-model="form.loginname" />
      </el-form-item>
      <el-form-item label="姓名：">
        <el-input v-model="form.name" />
      </el-form-item>
      <el-form-item label="用户类型：">
        <el-select v-model="form.type" placeholder="请选择">
          <el-option label="系统用户" value="1" />
          <el-option label="超级管理员" value="3" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态：">
        <el-switch v-model="form.state" />
      </el-form-item>
      <el-form-item label="备注：">
        <el-input v-model="form.note" type="textarea" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit">提交</el-button>
        <el-button type="info" plain @click="drawerIs(false)">取消</el-button>
      </el-form-item>
    </el-form>
  </el-drawer>
</template>
<script lang="ts" setup>
//------------------------------------------------------------------------------------
import { reactive, ref } from 'vue'
const drawer1 = ref(false);
//抽屉关闭事件
const emit = defineEmits(['close']);
const ruleFormRef = ref()


const drawerIs = (isOpen: boolean) => {
  drawer1.value = isOpen;
};
defineExpose({
  drawerIs,
});






// 定义关闭抽屉的方法


const form = reactive({
  loginname: '',
  name: '',
  type: '',
  state: false,
  note: '',
})
const onSubmit = async () => {
  if (!ruleFormRef.value) return
  await ruleFormRef.value.validate((valid: boolean) => {
    if (valid) {
      console.log('submit!')
    } else {
      console.log('error submit!')
    }
  })
}
// eslint-disable-next-line @typescript-eslint/no-unused-vars
const onClose = () => {
  emit('close');
}
//------------------------------------------------------------------------------------
</script>
