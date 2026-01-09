import Farm2Request from "@/service/remoteRequests/Farm2Request";
import type { Router } from "vue-router";
import farmHtmlUtils from "./farmHtmlUtils";
import { useDeviceStore } from "@/store/useDeviceStore";
import farmConfig from "./farmConfig";
/**
 * 获取html中由H1H2H3组成的标题
 * @param html - 大段的HTML字符串
 * @returns 包含标题名称、标签、层级和顺序号的数组
 */
const openPageByPathId = (path: string, id: string, type: string) => {
  window.open(path + "/" + id, type == "_blank" ? "_blank" : "_self");
};

/**
 * 文件下载
 * @param fileid
 */
const downloadFile = (id: string) => {
  const url = Farm2Request.getFullPath("api/files/download/" + id);
  window.open(url, "_blank");
};

const getImgUrl = (id: string) => {
  const url = Farm2Request.getFullPath("api/files/download/" + id);
  return url;
};

/**
 * 返回首页
 */
const goToHome = (isGotoSearchHome?: boolean) => {
  console.log(isGotoSearchHome);
  window.open("/system", "_self");
  // const deviceStore = useDeviceStore();
  // if (deviceStore.$state.isMobile) {
  //   window.open("/home", "_self");
  // } else {
  //   if (isGotoSearchHome !== undefined) {
  //     // 如果 isLogin 有明确的 true/false 值，直接使用它
  //     if (isGotoSearchHome) {
  //       farmConfig.loadPara("farm2.config.sys.home.url", (url) => {
  //         window.open(url as string, "_self");
  //       });
  //     } else {
  //       window.open("/home", "_self");
  //     }
  //   } else {
  //     // 否则使用 store 中的 isLogin 状态
  //     farmConfig.loadPara("farm2.config.sys.home.url", (url) => {
  //       window.open(url as string, "_self");
  //     });
  //   }
  // }
};
/**
 * 返回首页地址
 */
const getHomeUrl = (urlHandle: (url: string) => void) => {
  const deviceStore = useDeviceStore();
  if (deviceStore.$state.isMobile) {
    urlHandle("/home");
  } else {
    farmConfig.loadPara("farm2.config.sys.home.url", (url) => {
      urlHandle(url as string);
    });
  }
};

const gotoPage = (url: string, isNewPage?: boolean) => {
  if (isNewPage) {
    window.open(url, "_blank");
  } else {
    window.open(url, "_self");
  }
};

/**
 * 返回登录页
 */
const goToLogin = () => {
  window.open("/login", "_self");
};
/**
 * 跳转到分类知识页面
 */
const gotoTypeKnows = (typeId: string) => {
  window.open("/typeknows?typeid=" + typeId, "_self");
};
/**
 * 刷新当前页面
 */
const reLoad = () => {
  window.location.reload();
};

/**
 * 跳转到查询页面执行查询
 *
 * import { useRouter } from 'vue-router';
 * const router = useRouter();
 */
const gotoSearchPage = (
  searchword: { word: string; page: number },
  router?: Router
) => {
  if (router) {
    if (searchword.word.trim()) {
      router.push({ path: "/search", query: { word: searchword.word } });
    }
  } else {
    if (searchword.word.trim()) {
      window.open("/search?word=" + searchword.word, "_self");
    }
  }
};
/**
 * 获得标签得检索词
 * @param tagName
 * @param tagtype 0：系统功能，其他为自定义或者系统标签
 */
const getTagSearchWord = (tagName: string, tagtype: string) => {
  tagName = (tagName + "").trim();
  if (tagtype == "0") {
    return "SYS-" + tagName;
  } else {
    return "TAG-" + farmHtmlUtils.removeHtmltags(tagName);
  }
};
/**
 * 从超链接中提取知识id
 * @param url
 * @returns
 */
const getKnowIdByUrl = (url: string) => {
  if (url.indexOf("/knowview/") != 0) {
    return;
  }
  const parts = url.split("/");
  const id = parts.length > 0 ? parts[parts.length - 1] : "";
  if (id?.length == 32) {
    return id;
  } else {
    return;
  }
};
const getFqaIdByUrl = (url: string) => {
  if (url.indexOf("/fqa/view/") != 0) {
    return;
  }
  const parts = url.split("/");
  const id = parts.length > 0 ? parts[parts.length - 1] : "";
  if (id?.length == 32) {
    return id;
  } else {
    return;
  }
};

/**vue3路由页面
 * const router = useRouter()
 *
 * @param router
 * @param url
 */
const routToPage = (router: Router, url: string) => {
  router.push(url);
};

export default {
  routToPage,
  openPageByPathId,
  downloadFile,
  reLoad,
  goToHome,
  gotoSearchPage,
  getTagSearchWord,
  goToLogin,
  gotoTypeKnows,
  gotoPage,
  getKnowIdByUrl,
  getFqaIdByUrl,
  getHomeUrl,
  getImgUrl,
};
