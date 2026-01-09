<template>
  <el-col :lg="6" :sm="12" :xs="12">
    <div class="farm2-block-box">
      <el-row>
        <el-col :span="9" style="text-align: center;"> <el-avatar :class="'farm2_home_row_icon ' + props.colorType" shape="square"
            :icon="props.icon" />

        </el-col>
        <el-col :span="15" style="text-align: left;padding-left: 10px;">
          <div style="color: #7c8db5;">{{ props.title }}</div>
          <div style="font-weight: 700;padding-top: 10px;font-size: 12px;">
            {{ props.num }}
            <el-button v-if="props.func" @click="props.func" type="primary" size="small" round plain>{{ props.functitle
            }}</el-button>
          </div>
        </el-col>
      </el-row>
    </div>
  </el-col>
</template>
<script setup lang="ts">
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

const props = defineProps({
  colorType: {
    type: String,
    required: true
  },
  icon: {
    type: String,
    required: true
  },
  title: {
    type: String,
    required: true
  },
  num: {
    type: Number,
    required: false
  },
  func: {
    type: Function,
    required: false
  },
  functitle: {
    type: String,
    required: false
  },
})


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
