const HISTORY_KEY = 'searchHistory';
const MAX_HISTORY_LENGTH = 5;

/**
 * 添加一个检索词到历史记录中，历史中一共存放最近的5条记录，数据存储在localStorage中
 * @param {string} word - 要添加的检索词
 */
const push = (word: string): void => {
  if (!word.trim()) {
    return; // 忽略空白关键字
  }

  const history: string[] = JSON.parse(localStorage.getItem(HISTORY_KEY) || '[]') as string[];

  // 移除已存在的相同词项
  const index = history.indexOf(word);
  if (index !== -1) {
    history.splice(index, 1);
  }

  // 将新词项添加到数组头部
  history.unshift(word);

  // 截断数组长度至最大值
  if (history.length > MAX_HISTORY_LENGTH) {
    history.pop();
  }

  // 存储更新后的历史记录
  localStorage.setItem(HISTORY_KEY, JSON.stringify(history));
};

/**
 * 从localStorage中取出历史检索记录，返回一个历史记录数组元素是检索词
 * @returns {string[]} 历史记录数组
 */
const get = (): string[] => {
  return JSON.parse(localStorage.getItem(HISTORY_KEY) || '[]') as string[];
};

// 导出封装好的方法
export default { push, get };
