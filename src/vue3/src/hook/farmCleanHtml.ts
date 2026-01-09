/**
 * 清理 HTML 内容，使其适配 TipTap / ProseMirror 等富文本编辑器的 schema
 * 防止插入时出现：Invalid content for node 'listItem' 等错误
 */
interface CleanHtmlOptions {
  /** 是否移除无内容的空 span、div 等元素（默认 true） */
  removeEmptyNodes?: boolean;
  /** 是否修复空的 <li> 为 <li>&nbsp;</li>（默认 true） */
  fixEmptyListItems?: boolean;
  /** 是否尝试修复孤立的 <li>（默认 true） */
  wrapOrphanedListItems?: boolean;
  /** 自定义需要移除的标签名集合（如 ['script', 'style']） */
  removeTags?: string[];
  /** 自定义保留的属性（默认保留 src、href、class、style） */
  allowedAttributes?: string[];
}

function cleanHtmlForEditor(
  html: string,
  options: CleanHtmlOptions = {}
): string {
  const {
    removeEmptyNodes = true,
    fixEmptyListItems = true,
    wrapOrphanedListItems = true,
    removeTags = [],
    allowedAttributes = [
      "src",
      "href",
      "class",
      "style",
      "alt",
      "title",
      "target",
    ],
  } = options;

  // 创建 DOMParser
  const parser = new DOMParser();
  const doc = parser.parseFromString(html, "text/html");

  // 移除指定标签
  removeTags.forEach((tag) => {
    doc.body.querySelectorAll(tag).forEach((el) => el.remove());
  });

  // 1. 修复孤立的 <li>：未被 ul/ol 包裹的 li
  if (wrapOrphanedListItems) {
    const allLi = Array.from(doc.body.querySelectorAll("li"));
    allLi.forEach((li) => {
      const parent = li.parentElement;
      if (!parent || !["UL", "OL"].includes(parent.tagName)) {
        const ul = doc.createElement("ul");
        li.before(ul);
        ul.appendChild(li);
      }
    });
  }

  // 2. 修复空的 <li>：内容为空或只有空 span
  if (fixEmptyListItems) {
    doc.body.querySelectorAll("li").forEach((li) => {
      if (!li.textContent?.trim()) {
        li.innerHTML = "&nbsp;"; // TipTap 要求有内容
      }
    });
  }

  // 3. 移除完全空的内联元素（如 <span></span>、<em></em>）
  if (removeEmptyNodes) {
    const inlineTags = [
      "span",
      "b",
      "i",
      "em",
      "strong",
      "u",
      "sub",
      "sup",
      "small",
    ];
    inlineTags.forEach((tag) => {
      doc.body.querySelectorAll(tag).forEach((el) => {
        if (!el.textContent?.trim() && el.children.length === 0) {
          el.remove();
        }
      });
    });

    // 可选：清理空的 div/p（谨慎）
    // doc.body.querySelectorAll('div, p').forEach(el => {
    //   if (!el.textContent?.trim() && el.children.length === 0) {
    //     el.remove();
    //   }
    // });
  }

  // 4. 可选：清理属性（仅保留允许的属性）
  // 注意：这需要更复杂的逻辑，此处简化为保留白名单
  const allElements = doc.body.getElementsByTagName("*");
  for (let i = 0; i < allElements.length; i++) {
    const el = allElements[i];
    const attrs = Array.from(el.attributes);
    attrs.forEach((attr) => {
      if (!allowedAttributes.includes(attr.name)) {
        el.removeAttribute(attr.name);
      }
    });
  }

  // 返回 body 内部 HTML
  return doc.body.innerHTML;
}
export default { cleanHtmlForEditor };
