import farmSessionStorageUtils from "@/hook/farmSessionCacheUtils";
import Farm2Request from "@/service/remoteRequests/Farm2Request";
import { RequestTypeEnum } from "@/types/commons/RequestTypeEnum";
import { defineStore } from "pinia";

// 定义配置键的常量数组
const CONFIG_KEYS = [
  "farm2.config.file.exnames",
  "farm2.config.file.length.max",
  "farm2.config.img.exnames",
  "farm2.config.img.length.max",
  "farm2.config.media.exnames",
  "farm2.config.media.length.max",
  "farm2.config.sys.title.head",
  "farm2.config.sys.title.foot",
  "farm2.config.sys.home.url",
  "farm2.conf.server.version",
  "farm2.config.ai.embedding.able",
  "farm2.compute.ai.able",
  "farm2.config.permission.login.need",
  "farm2.compute.hardkey",
  "farm2.compute.lckey",
  "farm2.config.sys.help.url",
  "farm2.config.ai.max.def.bylib.able",
  "farm2.config.sys.tip.login.html",
  "farm2.config.permission.know.type.num.max",
  "farm2.config.column.head.show.course",
  "farm2.config.column.head.show.knowtypes",
  "farm2.config.column.head.show.searchview",
  "farm2.config.column.head.show.task",
  "farm2.config.column.head.show.wts",
] as const;

// 定义配置值的类型（根据实际情况补充）
type ConfigValue = string | number | boolean | null | undefined;
type ConfigMap = Map<string, ConfigValue>;

export const useSystemConfigStore = defineStore("systemConfigStore", {
  state: (): {
    dic: ConfigMap;
    configKey: string;
  } => ({
    dic: new Map([["logoPath", "/images/logo.png"]]),
    configKey: "LOCAL_CONFIG",
  }),

  actions: {
    /**
     * 加载远程配置参数
     * @param callback - 加载完成后执行的回调函数
     */
    async loadRemotePara(callback?: () => void): Promise<void> {
      try {
        const cachedData = farmSessionStorageUtils.get(
          this.configKey
        ) as Record<string, ConfigValue> | null;

        if (cachedData) {
          this._updateDicFromObject(cachedData);
          callback?.();
          return;
        }
        interface ConfigRequestPayload {
          keys: Readonly<typeof CONFIG_KEYS>;
        }
        // 请求远程数据
        const response = await Farm2Request.submit(
          "api/current/config",
          RequestTypeEnum.post,
          null,
          {
            keys: CONFIG_KEYS,
          } as ConfigRequestPayload
        );

        const data = response.data as Record<string, ConfigValue>;
        this._updateDicFromObject(data);
        farmSessionStorageUtils.put(this.configKey, data);

        callback?.();
      } catch (error) {
        console.error("加载远程配置失败:", error);
        alert(error);
      }
    },

    /**
     * 更新字典中的值
     * @param data - 键值对对象
     */
    _updateDicFromObject(data: Record<string, ConfigValue>): void {
      for (const key in data) {
        if (data.hasOwnProperty(key)) {
          this.dic.set(key, data[key]);
        }
      }
    },

    /**
     * 清空本地缓存和内存中的配置
     */
    clearCache(): void {
      this.dic.clear();
    },

    /**
     * 获取指定配置项
     * @param key - 配置项键名
     * @param handle - 回调函数，用于返回结果
     */
    getParameter(key: string, handle: (value: ConfigValue) => void): void {
      if (this.dic.has(key)) {
        handle(this.dic.get(key));
        return;
      }
      this.loadRemotePara(() => {
        handle(this.dic.get(key));
      });
    },

    /**
     * 快捷方法：判断是否启用 AI 功能
     * @param handle - 返回布尔值的回调
     */
    isAiAble(handle: (value: boolean) => void): void {
      this.getParameter("farm2.compute.ai.able", (value) => {
        handle(!!value); // 强制转换为布尔值
      });
    },
  },
});
