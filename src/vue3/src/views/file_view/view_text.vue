<template>
  <div style="height: 100%;background-color: #eeeeee;padding: 4px;" v-loading="isLoading">
    <div ref="editor" style="height: calc(100% - 20px); width: 100%;border-radius: 12px 12px 4px 4px;"></div>
    <div style="font-size: 12px;color:#999999;padding-left: 20px;padding-top: 4px;">{{ exname }}</div>
  </div>
</template>
<script setup lang="ts">
import { onMounted, ref, watch } from 'vue';
import ace from 'ace-builds';
import 'ace-builds/src-noconflict/ext-language_tools'
import 'ace-builds/src-noconflict/theme-monokai' // 默认设置的主题
import 'ace-builds/src-noconflict/snippets/javascript'
import 'ace-builds/src-noconflict/snippets/sql'
import 'ace-builds/src-noconflict/snippets/json'
import 'ace-builds/src-noconflict/snippets/java'
import 'ace-builds/src-noconflict/snippets/text'
import modelist from 'ace-builds/src-noconflict/ext-modelist';
import Farm2Request from '@/service/remoteRequests/Farm2Request';
import { RequestTypeEnum } from '@/types/commons/RequestTypeEnum';
import notice from '@/components/msg/FarmNotice';
import type { DataResponse } from '@/types/commons/DataResponse';
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
const props = defineProps({
  extendFileId: {
    type: String,
    required: true
  }
});
const exname = ref('txt');
const isLoading = ref(false);//加载状态：true|false
let aceEditor: ace.Editor | null = null;
watch(() => {
  return props.extendFileId;
}, () => {
  loadText();
});

const loadText = () => {
  Farm2Request.submit('api/exfiles/download/text/' + props.extendFileId, RequestTypeEnum.post, isLoading)
    .then((response: DataResponse) => {
      aceEditor!.setValue((response as { data: { text: string } }).data.text,-1);
      let rexname = (response as { data: { exname: string } }).data.exname;
      if (!isModeSupported(rexname)) {
        rexname = 'html';
      }
      aceEditor!.session.setMode('ace/mode/' + rexname);
      exname.value = ('ace/mode/' + rexname);
    }).catch((msg: string) => {
      notice.byError(msg);//报错
    });
}



function isModeSupported(mode: string) {
  // 获取所有的模式
  const modes = modelist.modes;

  // 遍历所有模式，检查目标模式是否在其中
  for (let i = 0; i < modes.length; i++) {
    if (modes[i].name === mode || modes[i].caption.toLowerCase() === mode.toLowerCase()) {
      return true;
    }
  }
  return false;
}

//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
const editor = ref(null);

ace.config.set('basePath', '/node_modules/ace-builds/src-noconflict/');
onMounted(() => {
  aceEditor = ace.edit(editor.value);
  aceEditor.setOptions({
    enableSnippets: false,//启用代码片段（snippets）支持。
    enableLiveAutocompletion: false,//启用实时自动补全（live autocompletion）。
    enableBasicAutocompletion: true,//启用基本自动补全（basic autocompletion）。
    mode: 'ace/mode/java', // 设置语言模式
    theme: 'ace/theme/github', // 设置主题
    selectionStyle: 'text',//选中风格
    readOnly: true,
    wrap: true, // 启用自动换行
  })
  loadText();
});
</script>
<style scoped>
/* 样式可以根据需要添加 */
</style>
