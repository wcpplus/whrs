import TurndownService from "turndown";
import { marked } from "marked"; // 如果需要解析 Markdown
/**
 * 删除html标签返回纯文本
 * @param html
 * @returns
 */
const removeHtmltags = (html: string) => {
  return html.replace(/<[^>]+>/g, "");
};

/**
 * html转markdown
 * @param html
 * @returns
 */
const getMarkDown = (html: string) => {
  const turndownService = new TurndownService();
  return turndownService.turndown(html);
};

/**
 * 转换为html
 * @param text markDown文本
 * @returns
 */
const getHtmlByMarkDown = (text: string) => {
  return marked.parse(text) as string;
};
/**
 * 页面选中内容时的回调函数
 * @param document
 * @param handle
 */
const pageHtmSelectedHandle = (
  document: Document,
  handle: (html: string) => void
) => {
  // 监听 selectionchange 事件
  document.addEventListener("selectionchange", () => {
    // 获取用户选中的内容
    const selection = document.getSelection();
    if (!selection || selection.isCollapsed) {
      // 检查是否有选中的文本
      // console.log("没有选中的内容");
      return;
    }

    // 获取选中的 HTML 内容
    const range = selection.getRangeAt(0); // 获取选区范围
    const tempDiv = document.createElement("div");
    tempDiv.appendChild(range.cloneContents()); // 将选区内容克隆到临时 div 中
    const selectedHtml = tempDiv.innerHTML; // 获取 HTML 格式的内容

    // 如果成功获取到 HTML 内容，则调用 handle 函数
    if (selectedHtml) {
      handle(selectedHtml);
    } else {
      //console.error("无法获取选中的 HTML 内容");
    }
  });
};

/**
 * 获得适合html的title标签中存放的字符串
 * @param html 任意字符
 * @returns 删除html和引号
 */
const getTitle = (text: string): string => {
  if (!text) {
    return "";
  }
  //console.log(text);
  // 1. 替换图片标签为 [图片]
  let processed = text.replace(/<img\b[^>]*>/gi, "[图片]");

  // 2. 替换视频标签为 [视频]
  processed = processed.replace(/<video\b[^>]*>[\s\S]*?<\/video>/gi, "[视频]");

  // 3. 替换音频标签为 [音频]
  processed = processed.replace(/<audio\b[^>]*>[\s\S]*?<\/audio>/gi, "[音频]");

  // 3.替换公式为[公式]
  // 替换所有 <span> 标签，只处理 math-node
  processed = processed.replace(
    /<span\b([^>]*)>([^<]*)<\/span>/gi,
    (match, attrs) => {
      // 判断是否包含 class="...math-node..."
      const hasMathClass = /class\s*=\s*["'][^"']*math-node[^"']*["']/i.test(
        attrs
      );
      // 提取 data-latex
      const latexMatch = attrs.match(/data-latex\s*=\s*["']([^"']*)["']/i);

      if (hasMathClass && latexMatch) {
        return `[公式：${latexMatch[1]}]`;
      }
      return match; // 不是公式，保留原标签
    }
  );
  //console.log(processed);

  // 4. 移除所有其他 HTML 标签
  const withoutTags = removeHtmltags(processed);

  // 5. 清理引号
  return withoutTags.replaceAll('"', "").replaceAll("'", "");
};
/**
 * 删除html两个标签之间的空白
 * @param html
 * @returns
 */
function removeWhitespaceBetweenTags(html: string): string {
  // 删除标签之间的空白
  let cleanedHtml = html.replace(/>\s+</g, "><");
  // 删除开始标签后的空白和结束标签前的空白
  cleanedHtml = cleanedHtml.replace(/(?<=>)\s+/g, "");
  cleanedHtml = cleanedHtml.replace(/\s+(?=<)/g, "");
  return cleanedHtml;
}
export default {
  removeHtmltags,
  getTitle,
  getMarkDown,
  getHtmlByMarkDown,
  pageHtmSelectedHandle,
  removeWhitespaceBetweenTags,
};
