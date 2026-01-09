/**
 * 删除重复的子字符串（按空格分隔）
 * @param input
 * @returns
 */
const removeDuplicateSubStr = (input: string): string => {
  const tags = input.split(" ");
  const uniqueTags = [...new Set(tags)];
  return uniqueTags.join(" ");
};
/**按换行符分隔字符串，并去除空行（包括只含空白字符的行）。
 *
 * @param text
 * @returns
 */
const splitByEnter = (text: string): Array<string> => {
  return text.split(/\r?\n/).filter((line) => line.trim() !== "");
};
/**截取字符串，当前str超过len时截取，并且添加省略号
 *
 * @param str
 * @param len
 */
const subString = (str: string, len: number, isAddDot?: boolean): string => {
  if (!str) return "";
  if (str.length <= len) return str;
  return str.substring(0, len) + (isAddDot ? "..." : "");
};

export default { removeDuplicateSubStr, splitByEnter, subString };
