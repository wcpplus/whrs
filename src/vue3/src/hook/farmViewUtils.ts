/**
 * 14位日期格式化
 * @param dateTimeString
 * @returns
 */
const formatDateTime = (dateTimeString: string): string => {
  // 检查输入是否符合预期格式（14位数字）
  if (typeof dateTimeString !== "string" || !/^\d{14}$/.test(dateTimeString)) {
    return dateTimeString;
  }

  // 分割字符串并构建新的格式
  const year = dateTimeString.slice(0, 4);
  const month = dateTimeString.slice(4, 6);
  const day = dateTimeString.slice(6, 8);
  const hour = dateTimeString.slice(8, 10);
  const minute = dateTimeString.slice(10, 12);

  // 返回格式化后的时间字符串
  return `${year}-${month}-${day} ${hour}:${minute}`;
};
/**
 * 显示最近多久创建
 * @param dateTimeString
 * @returns
 */
const formatDateTimeToNew = (dateTimeString: string): string => {
  // 检查输入是否符合预期格式（14位数字）
  if (typeof dateTimeString !== "string" || !/^\d{14}$/.test(dateTimeString)) {
    return dateTimeString;
  }

  // 将14位日期字符串转换为Date对象
  const year = parseInt(dateTimeString.slice(0, 4), 10);
  const month = parseInt(dateTimeString.slice(4, 6), 10) - 1; // 注意：月份从0开始
  const day = parseInt(dateTimeString.slice(6, 8), 10);
  const hour = parseInt(dateTimeString.slice(8, 10), 10);
  const minute = parseInt(dateTimeString.slice(10, 12), 10);

  const inputDate = new Date(year, month, day, hour, minute);
  const currentDate = new Date();

  // 计算时间差（以毫秒为单位）
  const timeDifference = currentDate.getTime() - inputDate.getTime();
  const minutesDifference = Math.floor(timeDifference / (1000 * 60));
  const hoursDifference = Math.floor(minutesDifference / 60);
  const daysDifference = Math.floor(hoursDifference / 24);

  // 根据时间差返回不同的格式
  if (daysDifference > 1) {
    // 如果超过2天，显示具体日期
    return `${year}-${String(month + 1).padStart(2, "0")}-${String(
      day
    ).padStart(2, "0")}`;
  } else if (hoursDifference > 0) {
    // 如果在2天内但超过1小时，显示几小时前
    return `${hoursDifference}小时前`;
  } else {
    // 如果在1小时内，显示几分钟前
    return `${minutesDifference}分钟前`;
  }
};

const formatSNO = (sno?: number) => {
  if (!sno) {
    return sno;
  }
  if (sno < 0 || sno > 999999) {
    throw new Error("Number must be between 0 and 999999.");
  }
  return sno.toString().padStart(6, "0");
};

/**
 * 绑定普通链接事件
 */
const bindALinkClickEvents = (
  htmoDom: Ref<HTMLElement | null>,
  doLink: (urlV: string, linktargetV: string) => void
) => {
  if (!htmoDom.value) {
    return;
  }
  const links = htmoDom.value.querySelectorAll<HTMLElement>("a");
  links.forEach((link) => {
    const href = link.getAttribute("href");
    link.removeAttribute("href");
    link.classList.add("skc-a-link");
    link.style.cursor = "pointer";
    const linktarget = link.getAttribute("target");
    link.addEventListener("click", () => {
      doLink(href!, linktarget!);
    });
  });
};
import katex from "katex";
const bindMath = (htmoDom: Ref<HTMLElement | null>) => {
  if (!htmoDom.value) {
    return;
  }

  // 2. ✅ 新增：渲染所有 .math-node 公式
  const mathNodes = htmoDom.value.querySelectorAll<HTMLElement>(".math-node");
  mathNodes.forEach((node) => {
    const tex = node.getAttribute("data-latex") || "";
    if (tex) {
      try {
        const renderedHTML = katex.renderToString(tex, {
          throwOnError: false,
          displayMode: false,
          // 可选：根据需要调整 strict 模式
          // strict: 'ignore',
        });
        node.innerHTML = renderedHTML;
        node.classList.add("math-rendered"); // 标记已渲染
      } catch (error) {
        console.warn("KaTeX render failed for:", tex, error);
        node.innerHTML = `<span style="color: red; font-size: 0.9em;">❌ ${tex}</span>`;
      }
    } else {
      // 清空空节点（避免显示零宽字符）
      node.innerHTML = "";
    }
  });
};

/**
 * 绑定卡片链接事件
 */
const bindCardLinkClickEvents = (
  htmoDom: Ref<HTMLElement | null>,
  doLink: (urlV: string, linktargetV: string) => void
) => {
  if (!htmoDom.value) {
    return;
  }
  const divs = htmoDom.value.querySelectorAll<HTMLElement>(".skc-link-card");
  divs.forEach((div) => {
    div.addEventListener("click", (event: MouseEvent) => {
      const target = event.target as HTMLElement;
      // 找到最近的 .skc-link-card 祖先元素
      const card = target.closest<HTMLElement>(".skc-link-card");
      if (!card) return;
      const urlElement = card.querySelector(".skc-link");
      const url = urlElement?.textContent?.trim();
      const linktarget = card.getAttribute("target");
      doLink(url!, linktarget!);
    });
  });
};

import { type Ref } from "vue";
import type { ViewImg } from "@/types/file/viewImg";
// 用于缓存每个 div 对应的 app 实例

/**
 * 绑定图片点击事件
 */
const bindImgClickEvents = (
  images: Array<ViewImg>,
  imgViewerRef: Ref,
  htmoDom: Ref<HTMLElement | null>
) => {
  if (htmoDom.value == null) {
    return;
  }
  const imgs = htmoDom.value.querySelectorAll<HTMLElement>("img");
  images.length = 0;
  let index = 0;
  imgs.forEach((img) => {
    if (
      !img.closest(".skc-file") &&
      !img.closest(".skc-link-card") &&
      img.getAttribute("src")
    ) {
      const maxSrc = img.getAttribute("data-src")
        ? img.getAttribute("data-src")
        : img.getAttribute("src");
      const minSrc = img.getAttribute("data-min-src")
        ? img.getAttribute("data-min-src")
        : maxSrc;
      images.push({ minSrc: minSrc!, maxSrc: maxSrc! });
      img.setAttribute("data-index", "" + index++);
      img.style.cursor = "pointer";
      img.addEventListener("click", ((event: MouseEvent) => {
        const imgElement = event.target as HTMLImageElement;
        // 处理图片点击逻辑，例如打开一个查看大图的组件
        let src = imgElement.getAttribute("data-src");
        if (!src) {
          src = imgElement.src;
        }
        if (imgViewerRef.value) {
          imgViewerRef.value.showImages(
            images,
            imgElement.getAttribute("data-index")
          );
        }
      }) as EventListenerOrEventListenerObject);
    }
  });
};

/**
 * 绑定附件点击事件
 */
const bindFileClickEvents = (
  fileViewerRef: Ref,
  htmoDom: Ref<HTMLElement | null>
) => {
  if (htmoDom.value == null) {
    return;
  }
  const divs = htmoDom.value.querySelectorAll<HTMLElement>(".skc-file");
  divs.forEach((div) => {
    div.addEventListener("click", (event: MouseEvent) => {
      // 获取点击的目标元素
      const targetElement = event.target as HTMLElement;
      // 使用 closest 方法找到最近的 .skc-file 祖先元素
      const skcFileElement = targetElement.closest(".skc-file") as HTMLElement;
      // 检查是否找到了 .skc-file 元素并且该元素有 fileid 属性
      if (skcFileElement && skcFileElement.hasAttribute("fileid")) {
        const fileid = skcFileElement.getAttribute("fileid");
        if (fileid) {
          fileViewerRef.value.openWin(fileid, null, true);
        }
      }
    });
  });
};

/**
 * 处理图片懒加载
 */
const imgsLazyEvents = (htmoDom: Ref<HTMLElement | null>) => {
  //const lazyImages = document.getElementsByClassName('farm-view-content')![0].querySelectorAll('img[data-lazy]');
  if (htmoDom.value == null) {
    return;
  }
  const lazyImages: HTMLImageElement[] = Array.from(
    htmoDom.value.querySelectorAll(".farm-view-content img[data-lazy]")
  );

  const config = {
    rootMargin: "50px 0px",
    threshold: 0.01,
  };
  const observer = new IntersectionObserver((entries, self) => {
    entries.forEach((entry) => {
      if (entry.isIntersecting) {
        const img = entry.target as HTMLImageElement;
        img.src = img.getAttribute("data-lazy")!;
        img.onload = () => {
          img.removeAttribute("data-lazy");
        };
        self.unobserve(img);
      }
    });
  }, config);
  lazyImages.forEach((image) => {
    if (image.getAttribute("data-src")) {
      observer.observe(image);
    }
  });
};

export default {
  formatDateTime,
  formatDateTimeToNew,
  formatSNO,
  bindALinkClickEvents,
  bindCardLinkClickEvents,
  bindMath,
  bindImgClickEvents,
  bindFileClickEvents,
  imgsLazyEvents,
};
