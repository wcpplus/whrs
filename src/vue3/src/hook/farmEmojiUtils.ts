import emojiRegex from "emoji-regex";
/**
 * 将字符串中的 Emoji 转义为 Unicode 转义序列，其他字符保持不变
 * @param input 输入字符串
 * @returns 转义后的字符串
 */
function escapeEmoji(input: string): string {
  return input.replace(emojiRegex(), (emoji) => {
    const codePoint = emoji.codePointAt(0)!;
    if (codePoint > 0xffff) {
      const high = 0xd800 + ((codePoint - 0x10000) >> 10);
      const low = 0xdc00 + ((codePoint - 0x10000) & 0x3ff);
      return `\\u${high.toString(16).padStart(4, "0")}\\u${low
        .toString(16)
        .padStart(4, "0")}`;
    } else {
      return `\\u${codePoint.toString(16).padStart(4, "0")}`;
    }
  });
}

/**
 * 将转义后的 Emoji 还原为原始字符
 * @param escaped 转义后的字符串
 * @returns 还原后的字符串
 */
function unescapeEmoji(escaped: string): string {
  if (typeof escaped !== "string") {
    throw new TypeError("Input must be a string");
  }
  try {
    return escaped.replace(/\\u([0-9a-fA-F]{4})/g, (_, hex) => {
      const codePoint = parseInt(hex, 16);
      return String.fromCodePoint(codePoint);
    });
  } catch {
    throw new Error("Invalid escaped string");
  }
}

export default { escapeEmoji, unescapeEmoji };
