/**
 * 默认页面事件定义
 */
export enum PageEvent {
  /**
   * 打开或关闭查询条件窗口：参数boolean
   */
  query_form_open = "query_form_open",
  /**
   * 打开或关闭数据表单窗口：参数{ isOpen: true, type: <FormParam>{ type: 如getViewType()} }
   */
  data_form_open = "data_form_open",
  /**
   * 清理查询表单:参数void
   */
  query_form_clear = "query_form_clear",

  /**
   * 执行查询:参数DataQuery{}
   */
  data_do_query = "data_do_query",

  /**
   * 刷新一个table表格的字节的数据
   */
  data_do_reload_tree_node = "data_do_reload_tree_node",

  /**
   * 用户选中树节点事件
   */
  data_choose_tree = "data_choose_tree",
}
