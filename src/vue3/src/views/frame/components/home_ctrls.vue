<template>
  <el-col :lg="6" :sm="12" :xs="12">
    <div class="farm2-block-box">
      <el-row>
        <el-col :span="9" style="text-align: center;">
          <el-avatar :class="'farm2_home_row_icon ' + props.colorType" shape="square" :icon="props.icon" />
        </el-col>
        <el-col :span="15" style="text-align: left;padding-left: 10px;">
          <div style="font-weight: 700;font-size: 12px; text-align: left;">
            <div style="font-weight: 200;padding-bottom: 4px;margin-top: -4px;">
              <el-switch v-model="value1" active-action-icon="Refresh"
                inactive-action-icon="Refresh" /><span>&nbsp;连续刷新</span>
            </div>
            <el-button :loading="value1" style="width: 94px;" type="primary" @click="doClick" size="small" round
              plain>刷新本页</el-button>
          </div>
        </el-col>
      </el-row>
    </div>
  </el-col>
</template>
<script setup lang="ts">
import { ref } from 'vue';

//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
const value1 = ref(false)
const props = defineProps({
  colorType: {
    type: String,
    required: true
  },
  icon: {
    type: String,
    required: true
  },
  func: {
    type: Function,
    required: true
  },
  num: {
    type: Number,
    required: true
  },
});

props.func();

const doClick = () => {
  props.func();
}

setInterval(() => {
  if (value1.value) {
    props.func();
  }
}, 3000); // 每隔3000毫秒(即3秒)调用一次updateTime方法

//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
</script>
<style scoped>
.farm2-block-box {
  background-color: #ffffff;
  padding: 20px;
  text-align: center;
  border-radius: 1em;
  margin-left: 20px;
  margin-bottom: 20px;
}

.farm2_home_row_icon {
  height: 64px;
  width: 64px;
  margin-right: 10px;
  font-size: 28px;
}

.farm2_home_row_icon.purple {
  background-color: #9694ff;
}

.farm2_home_row_icon.blue {
  background-color: #57caeb;
}

.farm2_home_row_icon.green {
  background-color: #5ddab4;
}

.farm2_home_row_icon.red {
  background-color: #ff7976;
}
</style>
