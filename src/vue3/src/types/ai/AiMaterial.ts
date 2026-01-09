//用户条件查询的封装
export interface AiMaterial {
  text?: string; //引用文本
  title?: string; //引用内容名称
  id?: string; //引用内容的业务id
  url?: string; //引用内容的访问地址
  model:
    | "TEXT" // 引用文本
    | "KNOWID" //引用知识
    | "FILEID" //引用附件
    | "NOTEID"; //引用记事本
}
