/**
 * 将对象存储到 localStorage 中，并设置10分钟有效期
 * @param key - 存储的键名
 * @param obj - 要存储的对象
 * @param cacheMinute - 为空时默认5分钟
 */
const put = (key: string, obj: object, cacheMinute?: number): void => {
  try {
    const now = new Date();

    // 构造带过期时间的数据结构
    const item = {
      value: obj,
      expiry:
        now.getTime() +
        (cacheMinute && cacheMinute > 0 ? cacheMinute : 5) * 60 * 1000, // 10分钟后的时间戳（单位：毫秒）
    };

    // 将对象序列化为 JSON 字符串
    const serialized = JSON.stringify(item);
    // 存入 localStorage
    localStorage.setItem(key, serialized);
  } catch (error) {
    // 捕获可能的异常并输出错误信息
    console.error("localStorage存储错误:", error);
  }
};

/**
 * 从 localStorage 中读取并反序列化对象，若已过期则自动清除并返回 null
 * @param key - 要读取的键名
 * @returns 返回解析后的对象，如果不存在或已过期则返回 null
 */
const get = (key: string): object | null => {
  try {
    // 从 localStorage 获取字符串数据
    const serialized = localStorage.getItem(key);
    // 如果没有找到对应的键值，返回 null
    if (serialized === null) {
      return null;
    }

    const item = JSON.parse(serialized);
    const now = new Date();
    // 检查是否已过期
    if (now.getTime() > item.expiry) {
      clear(key); // 清除过期数据
      return null;
    }

    // 返回原始值
    return item.value;
  } catch (error) {
    // 捕获可能的异常并输出错误信息
    console.error("localStorage读取错误:", error);
    return null;
  }
};

/**
 * 清除 localStorage 中的某个键或全部数据
 * @param key - 要清除的键名（可选）
 */
const clear = (key?: string): void => {
  if (key) {
    // 删除指定的键
    localStorage.removeItem(key);
  } else {
    // 清空整个 localStorage
    localStorage.clear();
  }
};

// 导出封装好的方法
export default { put, get, clear };
