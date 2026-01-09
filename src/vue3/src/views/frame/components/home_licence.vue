<template>
  <el-col :lg="6" :sm="12" :xs="12">
    <div class="farm2-block-box">
      <el-row>
        <el-col :span="9" style="text-align: center;">
          <el-avatar :class="'farm2_home_row_icon ' + props.colorType" shape="square" :icon="props.icon" />
        </el-col>
        <el-col :span="15" style="text-align: left;padding-left: 20px;padding-top: 4px;">
          <div style="color: #7c8db5;">{{ hardKeyRef }}</div>
          <div style="font-weight: 700;padding-top: 10px;font-size: 12px;">
            <el-button @click="openForm" style="margin-left: 4px;" type="primary" size="small" round
              plain>注册授权</el-button>
          </div>
        </el-col>
      </el-row>
    </div>
  </el-col>
  <el-dialog v-model="dialogVisibleRef" style="background-color: #ffffff;" title="授权注册" width="350">
    机器码：<span title="" style="color:brown;">{{ hardKeyRef }}</span>，通过机器码兑换授权码。<br />
    授权码：<span title="" style="color:brown;">{{ lcKeyRef }}</span><br />
    <el-input v-model="licenceKeyRef" style="width: 300px;margin-top: 20px;" maxlength="25" placeholder="请录入授权码"
      show-word-limit type="text" />
    <template #footer>
      <div class="dialog-footer" style="text-align: center;">
        <!--<el-button @click="dialogVisibleRef = false">Cancel</el-button>-->
        <el-button v-loading="loadingFlagRef" @click="submitKey" type="primary">提交授权码</el-button>
      </div>
    </template>
  </el-dialog>
</template>
<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { useSystemConfigStore } from '@/store/useSystemConfigStore';
const systemConfig = useSystemConfigStore();
const hardKeyRef = ref('');
const lcKeyRef = ref('');
const dialogVisibleRef = ref(false);
const licenceKeyRef = ref('');
import farm2Request from '@/service/remoteRequests/Farm2Request';
import notice from '@/components/msg/FarmNotice';
import { RequestTypeEnum } from '@/types/commons/RequestTypeEnum';
import farmRoutUtils from '@/hook/farmRoutUtils';
const loadingFlagRef = ref(false);
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
  func: {
    type: Function,
    required: false
  },
  functitle: {
    type: String,
    required: false
  },
})

const openForm = () => {
  dialogVisibleRef.value = true;
}
const submitKey = () => {
  farm2Request.submit('api/current/llc', RequestTypeEnum.post, loadingFlagRef, { id: licenceKeyRef.value }).then(() => {
    farmRoutUtils.reLoad();
  }).catch((msg: string) => {
    notice.byError(msg);//报错
  });
}

onMounted(() => {
  systemConfig.getParameter("farm2.compute.hardkey", (value1) => {
    hardKeyRef.value = value1 as string;
    systemConfig.getParameter("farm2.compute.lckey", (value2) => {
      lcKeyRef.value = value2 as string;
    });
  });
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
