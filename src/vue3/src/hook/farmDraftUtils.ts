import farmHtmlUtils from "./farmHtmlUtils";

/**
 * 保存草稿
 * @param key 唯一标识符前缀
 * @param id 唯一ID
 * @param input 输入字符串
 */
function saveDraft(key: string, id: string, input: string) {
  const storageKey = "editorContent_" + key + id;
  const timestampKey = storageKey + "_timestamp";
  localStorage.setItem(storageKey, input);
  localStorage.setItem(timestampKey, Date.now().toString()); // 记录当前时间戳
  return getDraft(key, id);
}

/**
 * 获取草稿
 * @param key 唯一标识符前缀
 * @param id 唯一ID
 * @returns 转义后的字符串 | null
 */
function getDraft(key: string, id: string): string {
  const storageKey = "editorContent_" + key + id;
  const html = localStorage.getItem(storageKey);
  if (html && farmHtmlUtils.getMarkDown(html).trim()) {
    return html;
  } else {
    return "";
  }
}

/**
 * 清除草稿
 * 同时清除指定的 key+id 内容 和 所有超过24小时的 editorContent_ 开头条目
 */
function clearDraft(key: string, id: string) {
  const storageKey = "editorContent_" + key + id;
  const timestampKey = storageKey + "_timestamp";

  // 删除指定的草稿内容和对应的时间戳
  localStorage.removeItem(storageKey);
  localStorage.removeItem(timestampKey);

  // 清理所有以 editorContent_ 开头、且超过24小时的缓存
  const now = Date.now();
  const prefix = "editorContent_";
  const oneDay = 24 * 60 * 60 * 1000;

  for (let i = localStorage.length - 1; i >= 0; i--) {
    const keyName = localStorage.key(i);
    if (!keyName) continue;

    if (keyName.startsWith(prefix) && !keyName.endsWith("_timestamp")) {
      const timestampKeyName = keyName + "_timestamp";
      const timestampStr = localStorage.getItem(timestampKeyName);
      const timestamp = timestampStr ? Number(timestampStr) : NaN;
      if (!isNaN(timestamp) && now - timestamp > oneDay) {
        localStorage.removeItem(keyName);
        localStorage.removeItem(timestampKeyName);
      }
    }
  }
}

export default { saveDraft, getDraft, clearDraft };
