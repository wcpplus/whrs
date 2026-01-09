/**
 * 转义think间的xml内容
 * @param msg
 * @returns
 */
function processThinkTags(msg: string) {
  const start = msg.indexOf("<think>");
  const end = msg.indexOf("</think>");
  if (start < 0) {
    return msg;
  } else {
    if (end < 0) {
      msg = msg + "</think>";
      return transformThinkTags(msg).replaceAll("</think>", "");
    } else {
      return transformThinkTags(msg);
    }
  }
}
function escapeXml(unsafe: string): string {
  return unsafe.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
}

function transformThinkTags(input: string): string {
  // 正则表达式匹配<think>...</think>中的内容
  const regex = /(<think>)([\s\S]*?)(<\/think>)/g;
  // 替换所有匹配项
  return input.replace(regex, function (match, p1, p2, p3) {
    // 转义匹配到的内容
    const escapedContent = escapeXml(p2);
    // 返回新的<think>标签包含转义后的内容
    return `${p1}${escapedContent}${p3}`;
  });
}

export default {
  processThinkTags,
};
