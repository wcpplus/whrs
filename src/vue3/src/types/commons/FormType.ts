/**
 * 定义表单类型，如增加/修改/展示信息
 */
export interface FormParam {
  type: FormType;
  id?: string;
  data?: unknown;
}

export interface FormType {
  key: string;
  title: string;
}

export const getViewType = () => {
  return { key: "view", title: "浏览" };
};
export const getCreatType = () => {
  return { key: "creat", title: "创建" };
};
export const getUpdateType = () => {
  return { key: "update", title: "更新" };
};
