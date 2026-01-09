//用户条件查询的封装
import type { AiMaterial } from "./AiMaterial";
import type { AiMsgConfig } from "./AiMsgConfig";
export interface SendMsg {
  id?: string; //消息id
  userMsg: string; //用户消息
  backMsg?: string; //模型返回消息
  materials: AiMaterial[]; //引用上下文：文本
  historyIds?: string[]; //历史消息记录key
  config?: AiMsgConfig; //引用知识【"all"全库检索 : "no"不引用知识库】
  commandMode:
    | "SEARCH_ANALYSIS" //智能检索
    | "QUESTION_ANSWERING" //知识问答
    | "FORM_COMPLETION" //表单完善
    | "FORM_JSON_BLANK" //空模板返回json数据
    | "KNOWTYPE_CHOOSE" //分类选中
    | "QUOTE_SEARCH_JSON"; //知识查询
}
