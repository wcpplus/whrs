<template>
  <div>
    <button @click="insertFormula">插入公式</button>
    <editor-content :editor="editor" />
  </div>
</template>

<script setup lang="ts">
import { Editor, EditorContent } from '@tiptap/vue-3'
import StarterKit from '@tiptap/starter-kit'
import { ref, onMounted, onBeforeUnmount } from 'vue'

const editor = ref()

onMounted(() => {
  editor.value = new Editor({
    extensions: [StarterKit.configure({ heading: false })],
    content: '<p>试试插入一个公式：</p>',
  })
})

onBeforeUnmount(() => {
  editor.value?.destroy()
})

const insertFormula = () => {
  const formula = prompt('请输入 LaTeX 公式（如：\\frac{a}{b}）')
  if (formula !== null) {
    editor.value.commands.insertMath(formula)
  }
}
</script>

<style>
/* 隐藏原始 math-node，只显示 widget */
.math-node {
  display: inline-block;
  width: 0 !important;
  height: 0 !important;
  padding: 0 !important;
  margin: 0 !important;
  border: none !important;
  font-size: 0 !important;
  line-height: 0 !important;
  opacity: 0 !important;
  pointer-events: none;
  user-select: none;
  /* 防止 \u200B 占位 */
  font-family: monospace !important;
}

.math-render {
  display: inline-block;
  vertical-align: middle;
  min-width: 1em;
  text-align: center;
}
</style>
