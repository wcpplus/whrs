//知识封装
export interface TreeNode {
  label: string;
  level: number;
  num: number;
  id: string;
  parentId: string;
  model: string;
  children?: Array<TreeNode>;
}
