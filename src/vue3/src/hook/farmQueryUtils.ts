import type { DataQuery } from "@/types/commons/DataQuery";
import type { DataResult } from "@/types/commons/DataResult";
import type { DataRule } from "@/types/commons/DataRule";

//获得默认远程结果集合
const getDefaultResult = (): DataResult => {
  return {
    totalSize: 0,
    totalPage: 0,
    page: 1,
    pageSize: 10,
  };
};

/**
 * 初始化或更新查询条件
 * @param query - 查询对象
 * @param key - 规则的键
 * @param value - 规则的值
 * @param compara - 比较操作符
 */
const initRule = (
  query: DataQuery | undefined,
  key: string,
  value: string | number,
  compara: string
): void => {
  if (!value) {
    return;
  }
  if (!query) {
    // 如果query未定义，初始化一个空的DataQuery对象
    query = { rules: [] };
  }

  if (!query.rules) {
    // 如果rules数组不存在，初始化为空数组
    query.rules = [];
  }

  // 查找是否存在key为'A.ENTITYID'的规则
  const existingRuleIndex = query.rules.findIndex((rule) => rule.key === key);

  if (existingRuleIndex !== -1) {
    // 如果存在，更新其value和compara属性
    query.rules[existingRuleIndex] = {
      ...query.rules[existingRuleIndex],
      value,
      compara,
    };
  } else {
    // 如果不存在，添加新的规则
    query.rules.push({ key, value, compara });
  }
};

/**
 * 删除重复的查询word
 * @param word
 * @returns
 */
function removeDuplicatesSearchWord(word: string): string {
  // 分割字符串为数组，使用空格作为分隔符
  const items = word.split(" ");

  // 使用Set数据结构来去除重复项，因为Set会自动去重
  const uniqueItems = new Set(items);

  // 将Set转换回数组
  const resultArray = Array.from(uniqueItems);

  // 将数组重新组合成字符串，并以空格作为连接符
  return resultArray.join(" ");
}

//通过key删除rules中的条件
function removeRuleByKey(rules: Array<DataRule>, key: string) {
  const index = rules.findIndex((rule) => rule.key === key);
  if (index !== -1) {
    rules.splice(index, 1); // 删除 1 项
  }
}

export default {
  getDefaultResult,
  initRule,
  removeDuplicatesSearchWord,
  removeRuleByKey,
};
