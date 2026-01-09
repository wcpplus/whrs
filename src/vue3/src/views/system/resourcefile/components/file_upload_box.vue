<template>
  <div v-loading="isLoading">
    <el-upload :before-upload="handleBeforeUpload" :show-file-list="false" class="upload-demo" drag
      :http-request="handleUpload" :multiple="props.multiple">
      <el-icon v-if="props.model == 'IMG'" class="el-icon--upload">
        <PictureFilled />
      </el-icon>
      <el-icon v-if="props.model == 'VEDIO'" class="el-icon--upload">
        <VideoPlay />
      </el-icon>
      <el-icon v-if="props.model == 'AUDIO'" class="el-icon--upload">
        <Headset />
      </el-icon>
      <el-icon v-if="props.model == 'FILE'" class="el-icon--upload"><upload-filled /></el-icon>
      <div class="el-upload__text">
        拖动文件到此或 <em>点击上传</em>
      </div>
      <div class="el-upload__tip" style="font-size: 12px;color: #aaaaaa;line-height: 1.7em; word-break: break-all;">
        <div v-if="props.tip">{{ props.tip }}</div>
        支持附件类型{{ fileLimit.exNames ? fileLimit.exNames.join(',') : config.exnames }}，
        附件大小{{ fileLimit.maxSize ? (fileLimit.maxSize / 1024 / 1024).toFixed(2) : (config.filesize / 1024 /
          1024).toFixed(2) }}mb {{ props.multiple ? '多选' : '单选' }}
      </div>
      <div v-for="state in uploadfiles" :key="state.index" style="max-width: 100%;overflow:auto;">
        <el-progress :percentage="state.pnum">
          <el-button text :title="state.name">{{ state.name.length > 5 ? state.name.substring(0, 5) + '...' : state.name
          }}上传中...</el-button>
        </el-progress>
      </div>
    </el-upload>
  </div>

</template>
<script setup lang="ts">
import Farm2Request from '@/service/remoteRequests/Farm2Request';
import { useSystemConfigStore } from '@/store/useSystemConfigStore';
import { onMounted, reactive, ref, type PropType } from 'vue';
import type { DataResponse } from "@/types/commons/DataResponse";
import type { FileUploadState } from '../utils/fileUploadState';
import farmConfig from '@/hook/farmConfig';
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
const config = reactive({ exnames: "", filesize: 1000 });
const sysConfig = useSystemConfigStore();
const isLoading = ref(false);//加载状态：true|false
const props = defineProps({
  successHandle: {
    type: Function,
    required: true
  },
  updatingHandle: {
    type: Function,
    required: false
  },
  errorHandle: {
    type: Function,
    required: true
  },
  exNames: {
    type: Array<string>,
    required: false
  },
  maxSize: {
    type: Number,
    required: false
  },
  model: {
    type: String as PropType<'IMG' | 'VEDIO' | 'AUDIO' | 'FILE'>,
    required: true
  },
  multiple: {
    type: Boolean,
    required: true
  },
  tip: {
    type: String,
    required: false
  },
});

const fileLimit = reactive({
  exNames: [] as string[],
  maxSize: 0,
});



//缓存所有上传文件
const uploadfiles = reactive<FileUploadState[]>([]);

/**
 * 加载远程参数
 */
const loadParas = () => {
  let key = 'file';
  if (props.model == 'IMG') {
    key = 'img';
  }
  if (props.model == 'VEDIO' || props.model == 'AUDIO') {
    key = 'media';
  }
  if (props.model == 'FILE') {
    key = 'file';
  }
  if (props.exNames) {
    fileLimit.exNames = props.exNames;
  } else {
    farmConfig.loadPara('farm2.config.' + key + '.exnames', (val) => {
      const exnames = (val as string).split(',').map(s => s.trim()).filter(node => node);
      fileLimit.exNames = exnames;
    });
  } if (props.maxSize) {
    fileLimit.maxSize = props.maxSize;
  } else {
    farmConfig.loadPara('farm2.config.' + key + '.length.max', (val) => {
      const maxsize = (val as number);
      fileLimit.maxSize = maxsize;
    });
  }
}



//校验上传文件
function handleBeforeUpload(file: File) {
  // 校验扩展名
  if (fileLimit.exNames && fileLimit.exNames.length > 0) {
    const fileExtension = file.name.split('.').pop()?.toLowerCase();
    if (!fileExtension) {
      props.errorHandle({ message: '无法识别文件类型: ' + file.name });
      return false;
    }
    // 将配置中的扩展名也转为小写再比较（确保忽略大小写）
    const allowedExtensions = fileLimit.exNames.map(ext => ext.toLowerCase());
    const isValidFile = allowedExtensions.includes(fileExtension);
    if (!isValidFile) {
      props.errorHandle({ message: '文件类型不允许上传: ' + file.name });
      return false;
    }
  }
  if (fileLimit.maxSize) {
    if (file.size > fileLimit.maxSize) {
      const maxMB = (fileLimit.maxSize / 1024 / 1024).toFixed(1);
      const curMB = (file.size / 1024 / 1024).toFixed(2);
      props.errorHandle({ message: `文件超大（最大 ${maxMB}MB）: ${curMB}MB - ${file.name}` });
      return false;
    }
    if (file.size <= 0) {
      props.errorHandle({ message: '文件内容不可为空' });
      return false;
    }
  }
  return true;
}

//通过index查找元素
function findByIndex(targetIndex: number, files: FileUploadState[]): FileUploadState | undefined {
  return files.find(item => item.index === targetIndex);
}
// 通过 index 删除元素
function removeByIndex(targetIndex: number, files: FileUploadState[]) {
  // 使用 findIndex 找到目标元素在数组中的索引
  const indexInArray = files.findIndex(item => item.index === targetIndex);
  if (indexInArray !== -1) {
    // 使用 splice 方法从数组中删除该元素
    files.splice(indexInArray, 1);
  }
}


const handleUpload = (option: { file: File }) => {
  const index = uploadfiles.length + 1;
  uploadfiles.push({
    name: option.file.name,
    index: index,
    pnum: 0,
  });
  if (props.updatingHandle) {
    //开始上传
    props.updatingHandle();
  }
  Farm2Request.uploadFile("api/files/upload", isLoading, option.file, (pnum: number) => {
    if (pnum > 0) {
      if (findByIndex(index, uploadfiles)) {
        findByIndex(index, uploadfiles)!.pnum = pnum;
      }
    }
  }).then((response: DataResponse) => {
    //上传完成
    removeByIndex(index, uploadfiles);
    // if (uploadfiles.length <= 0) {
    props.successHandle(response);
    // }
  }).catch((msg: Error) => {
    if (findByIndex(index, uploadfiles)) {
      const state = findByIndex(index, uploadfiles)
      props.errorHandle({ message: state?.name + msg.message });
    }
    removeByIndex(index, uploadfiles);
  });
}
onMounted(() => {
  sysConfig.getParameter("farm2.config.file.length.max", (value) => {
    config.filesize = value as number;
  });
  sysConfig.getParameter("farm2.config.file.exnames", (value) => {
    config.exnames = value as string;
  });
});
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
onMounted(() => {
  loadParas();
});
</script>
<style scoped>
/* 样式可以根据需要添加 */
:deep(.el-upload .el-upload-dragger) {
  padding-top: 10px;
}
</style>
