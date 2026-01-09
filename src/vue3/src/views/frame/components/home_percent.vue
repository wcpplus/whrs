<template>
  <el-col :lg="6" :sm="12" :xs="12">
    <div class="farm2-block-box">
      <el-row>
        <el-col :span="9" style="text-align: center;">
          <el-progress type="dashboard" :width="64" style="height: 64px;" :percentage="props.persent"
            :color="[{ color: colorval, percentage: 20 }]">
            <template #default="{ }">
              <div v-if="props.showPersent" class="percentage-value">{{ props.persent }}%</div>
              <div v-if="!props.showPersent" class="percentage-value">{{ props.title1 }}</div>
            </template>
          </el-progress>
        </el-col>
        <el-col :span="15" style="text-align: left;padding-left: 10px;">
          <div style="color: #7c8db5;">{{ props.title1 }}</div>
          <div class="percentage-label" style="font-size: 12px;overflow: hidden;">{{ props.title2 }}</div>
          <div v-if="props.title2" class="percentage-label" style="font-size: 12px;overflow: hidden;">{{
            props.title3 }}</div>
        </el-col>
      </el-row>
    </div>
  </el-col>
</template>
<script setup lang="ts">
import { computed } from 'vue';

//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

const props = defineProps({
  icon: {
    type: String,
    required: false
  },
  persent: {
    type: Number,
    required: true
  },
  title1: {
    type: String,
    required: true
  },
  title2: {
    type: String,
    required: false
  },
  title3: {
    type: String,
    required: false
  },
  showPersent: {
    type: Boolean,
    required: false,
    default: true
  },
})

const colorval = computed(() => {
  let color = "#5ddab4";
  if (!props.showPersent) {
    return color;
  }
  if (props.persent >= 60) {
    //"yello"
    color = "#eaca4a";
  }
  if (props.persent >= 80) {
    // "red"
    color = "#ff7976";
  }
  return color;
});


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

.percentage-value {
  font-size: 12px;
}

.percentage-label {
  padding-top: 4px;
}
</style>
