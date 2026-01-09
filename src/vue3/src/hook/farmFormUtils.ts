import type { DataQuery } from "@/types/commons/DataQuery";
import type { DataRule } from "@/types/commons/DataRule";
import {
  getCreatType,
  getUpdateType,
  getViewType,
  type FormType,
} from "@/types/commons/FormType";

/**
 * 清除查询条件中值
 * @param obj  Array<DataRule>
 * @returns  DataQuery
 */
const clearQueryForm = (obj: Array<DataRule>) => {
  for (const rule of obj) {
    rule.value = "";
  }
};

/**
 * 筛选有效得查询条件组成新的条件:入参为查询条件，返回DataQuery
 * @param obj  Array<DataRule>
 * @returns  DataQuery
 */
const getRules = (obj: Array<DataRule>): DataQuery => {
  const dataRules: Array<DataRule> = [];
  const query: DataQuery = { rules: [] };
  for (const rule of obj) {
    if (isRuleValid(rule)) {
      dataRules.push(rule);
    }
  }
  query.rules = dataRules;
  return query;
};

/**
 * 判断rule是否有效（key或vaule或compara为空就无效）
 * @param rule
 * @returns
 */
const isRuleValid = (rule: DataRule): boolean => {
  // 检查 key、value 和 compara 是否都不是空字符串、null 或 undefined
  return (
    rule.key !== "" &&
    rule.key != null &&
    rule.value !== "" &&
    rule.value != null &&
    rule.compara !== "" &&
    rule.compara != null
  );
};

const initFormCtrl = (
  type: FormType,
  ctrl: {
    isShowRetain: boolean;
    isShowSubmit: boolean;
    isDrawerOpen: boolean;
    isAbledForm: boolean;
    isShowReset: boolean;
  }
): void => {
  if (type.key == getCreatType().key) {
    ctrl.isShowSubmit = true;
    ctrl.isDrawerOpen = true;
    ctrl.isShowRetain = true;
    ctrl.isAbledForm = true;
    ctrl.isShowReset = true;
  }
  if (type.key == getUpdateType().key) {
    ctrl.isShowSubmit = true;
    ctrl.isDrawerOpen = true;
    ctrl.isShowRetain = false;
    ctrl.isAbledForm = true;
    ctrl.isShowReset = false;
  }
  if (type.key == getViewType().key) {
    ctrl.isShowSubmit = false;
    ctrl.isDrawerOpen = true;
    ctrl.isShowRetain = false;
    ctrl.isAbledForm = false;
    ctrl.isShowReset = false;
  }
};

export default { clearQueryForm, getRules, initFormCtrl };
