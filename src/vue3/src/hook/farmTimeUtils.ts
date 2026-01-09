/**
 * 将日期转为14位字符串
 * @param date
 * @returns
 */
function dateTo14DigitString(date: Date) {
  function pad(n: number) {
    return n < 10 ? "0" + n : n;
  }

  const year = date.getFullYear(); // YYYY
  const month = pad(date.getMonth() + 1); // MM (注意月份从0开始)
  const day = pad(date.getDate()); // DD
  const hours = pad(date.getHours()); // HH
  const minutes = pad(date.getMinutes()); // MM
  const seconds = pad(date.getSeconds()); // SS

  return `${year}${month}${day}${hours}${minutes}${seconds}`;
}
function dateTo8DigitString(date: Date) {
  function pad(n: number) {
    return n < 10 ? "0" + n : n;
  }

  const year = date.getFullYear(); // YYYY
  const month = pad(date.getMonth() + 1); // MM (注意月份从0开始)
  const day = pad(date.getDate()); // DD

  return `${year}${month}${day}`;
}

/**
 * 将日期14位字符串转为Date
 * @param date
 * @returns
 */
function digitString14ToDate(str: string): Date | null {
  if (!/^\d{14}$/.test(str)) {
    console.error("Invalid 14-digit string format");
    return null;
  }
  const year = parseInt(str.substring(0, 4), 10);
  const month = parseInt(str.substring(4, 6), 10) - 1; // 月份从0开始
  const day = parseInt(str.substring(6, 8), 10);
  const hours = parseInt(str.substring(8, 10), 10);
  const minutes = parseInt(str.substring(10, 12), 10);
  const seconds = parseInt(str.substring(12, 14), 10);
  const date = new Date(year, month, day, hours, minutes, seconds);
  if (isNaN(date.getTime())) {
    console.error("Invalid date value after parsing");
    return null;
  }
  return date;
}
/**
 * 计算参数日期距离当前时间的分钟数
 * @param dateTimeString 14位日期时间，如20251212082312
 * @returns 距离当前时间的分钟数（过去为正数，未来为负数）
 */
const minutesFromNow = (dateTimeString: string): number => {
  if (!dateTimeString || dateTimeString.length !== 14) {
    throw new Error(
      "dateTimeString must be a 14-digit string in format YYYYMMDDHHmmss"
    );
  }

  const year = parseInt(dateTimeString.slice(0, 4), 10);
  const month = parseInt(dateTimeString.slice(4, 6), 10) - 1; // 月份从0开始
  const day = parseInt(dateTimeString.slice(6, 8), 10);
  const hour = parseInt(dateTimeString.slice(8, 10), 10);
  const minute = parseInt(dateTimeString.slice(10, 12), 10);
  const second = parseInt(dateTimeString.slice(12, 14), 10);

  const inputDate = new Date(year, month, day, hour, minute, second);

  // 验证日期是否合法
  if (isNaN(inputDate.getTime())) {
    throw new Error("Invalid date string");
  }

  const now = new Date();
  const diffInMs = now.getTime() - inputDate.getTime(); // 当前时间 - 输入时间
  const minutes = Math.floor(diffInMs / (1000 * 60));

  return minutes;
};
/**
 * 14位日期格式化
 * @param dateTimeString
 * @returns
 */
const formatDate = (dateTimeString: string): string => {
  // 检查输入是否符合预期格式（14位数字）
  if (typeof dateTimeString !== "string" || !/^\d{14}$/.test(dateTimeString)) {
    return dateTimeString;
  }
  // 分割字符串并构建新的格式
  const year = dateTimeString.slice(0, 4);
  const month = dateTimeString.slice(4, 6);
  const day = dateTimeString.slice(6, 8);
  // 返回格式化后的时间字符串
  return `${year}-${month}-${day}`;
};
const format14ToHms = (dateTimeString: string): string => {
  // 检查输入是否符合预期格式（14位数字）
  if (typeof dateTimeString !== "string" || !/^\d{14}$/.test(dateTimeString)) {
    return ""; // 或返回原值、'--:--:--' 等，根据需求
  }

  const hour = dateTimeString.slice(8, 10);
  const minute = dateTimeString.slice(10, 12);
  const second = dateTimeString.slice(12, 14);

  return `${hour}:${minute}:${second}`;
};
const formatDate14 = (dateTimeString: string, hideSecond?: boolean): string => {
  // 检查输入是否符合预期格式（14位数字）
  if (typeof dateTimeString !== "string" || !/^\d{14}$/.test(dateTimeString)) {
    return dateTimeString; // 不符合格式原样返回
  }

  // 分割字符串
  const year = dateTimeString.slice(0, 4);
  const month = dateTimeString.slice(4, 6);
  const day = dateTimeString.slice(6, 8);
  const hour = dateTimeString.slice(8, 10);
  const minute = dateTimeString.slice(10, 12);
  const second = dateTimeString.slice(12, 14);

  // 返回格式化后的时间字符串：YYYY-MM-DD HH:mm:ss
  if (hideSecond) {
    return `${year}-${month}-${day} ${hour}:${minute}`;
  } else {
    return `${year}-${month}-${day} ${hour}:${minute}:${second}`;
  }
};

/**
 * 8位日期格式化（支持自动截取前8位）
 * @param dateTimeString - 输入的日期字符串（如 "20250912"）
 * @returns 格式化后的 "YYYY-MM-DD"，若输入无效则返回当前日期（格式同上）
 */
const formatDate8 = (dateTimeString?: string): string => {
  // 默认使用当前日期
  const now = new Date();

  // 如果输入为空或非字符串，使用当前日期
  if (!dateTimeString || typeof dateTimeString !== "string") {
    const year = now.getFullYear();
    const month = String(now.getMonth() + 1).padStart(2, "0");
    const day = String(now.getDate()).padStart(2, "0");
    return `${year}-${month}-${day}`;
  }

  // 截取前8位
  const datePart = dateTimeString.slice(0, 8);

  // 验证是否为8位数字
  if (!/^\d{8}$/.test(datePart)) {
    // 格式不对，也返回当前日期（或可选：return dateTimeString）
    const year = now.getFullYear();
    const month = String(now.getMonth() + 1).padStart(2, "0");
    const day = String(now.getDate()).padStart(2, "0");
    return `${year}-${month}-${day}`;
  }

  // 分割并格式化
  const year = datePart.slice(0, 4);
  const month = datePart.slice(4, 6);
  const day = datePart.slice(6, 8);

  // 可选：验证日期是否合法（如 20251345）
  const date = new Date(`${year}-${month}-${day}`);
  if (date.toString() === "Invalid Date") {
    return `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(
      2,
      "0"
    )}-${String(now.getDate()).padStart(2, "0")}`;
  }

  return `${year}-${month}-${day}`;
};

/**
 * 8位日期格式化（支持自动截取前8位）
 * @param dateTimeString - 输入的日期字符串（如 "20250912"）
 * @returns 格式化后的 "YYYY-MM-DD"，若输入无效则返回当前日期（格式同上）
 */
const formatDate8Stype2 = (dateTimeString?: string): string => {
  // 默认使用当前日期
  const now = new Date();

  // 如果输入为空或非字符串，使用当前日期
  if (!dateTimeString || typeof dateTimeString !== "string") {
    const year = now.getFullYear();
    const month = String(now.getMonth() + 1).padStart(2, "0");
    const day = String(now.getDate()).padStart(2, "0");
    return `${year}年${month}月${day}日`;
  }

  // 截取前8位
  const datePart = dateTimeString.slice(0, 8);

  // 验证是否为8位数字
  if (!/^\d{8}$/.test(datePart)) {
    // 格式不对，也返回当前日期（或可选：return dateTimeString）
    const year = now.getFullYear();
    const month = String(now.getMonth() + 1).padStart(2, "0");
    const day = String(now.getDate()).padStart(2, "0");
    return `${year}年${month}月${day}日`;
  }

  // 分割并格式化
  const year = datePart.slice(0, 4);
  const month = datePart.slice(4, 6);
  const day = datePart.slice(6, 8);

  // 可选：验证日期是否合法（如 20251345）
  const date = new Date(`${year}-${month}-${day}`);
  if (date.toString() === "Invalid Date") {
    return `${now.getFullYear()}年${String(now.getMonth() + 1).padStart(
      2,
      "0"
    )}月${String(now.getDate()).padStart(2, "0")}日`;
  }

  return `${year}年${month}月${day}日`;
};

/**
 * 8位日期格式化（支持自动截取前8位）
 * @param dateTimeString - 输入的日期字符串（如 "20250912"）
 * @returns 格式化后的 "YYYY-MM-DD"，若输入无效则返回当前日期（格式同上）
 */
const formatDate6 = (dateTimeString?: string): string => {
  // 默认使用当前日期
  const now = new Date();

  // 如果输入为空或非字符串，使用当前日期
  if (!dateTimeString || typeof dateTimeString !== "string") {
    const year = now.getFullYear();
    const month = String(now.getMonth() + 1).padStart(2, "0");
    return `${year}-${month}`;
  }

  // 截取前8位
  const datePart = dateTimeString.slice(0, 8);

  // 分割并格式化
  const year = datePart.slice(0, 4);
  const month = datePart.slice(4, 6);

  // 可选：验证日期是否合法（如 20251345）
  const date = new Date(`${year}-${month}`);
  if (date.toString() === "Invalid Date") {
    return `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(
      2,
      "0"
    )}-${String(now.getDate()).padStart(2, "0")}`;
  }
  return `${year}-${month}`;
};


/**
 * 返回多少小时之前的日期
 * @param hour 小时
 * @returns
 */
const lastDateByHour = (hour: number): Date => {
  const now = new Date();
  const millisecondsInHour = 3600000; // 1 hour = 3600000 ms
  return new Date(now.getTime() - hour * millisecondsInHour);
};
/**
 * 返回多少天之前的日期
 * @param day 天
 * @returns
 */
const lastDateByDay = (day: number): Date => {
  const now = new Date();
  const millisecondsInHour = 3600000 * 24; // 1 hour = 3600000 ms
  return new Date(now.getTime() - day * millisecondsInHour);
};

/**
 * 返回多少月之前的日期
 * @param month 月
 * @returns
 */
const lastDateByMonth = (month: number): Date => {
  const date = new Date();
  date.setMonth(date.getMonth() - month);
  return date;
};
/**
 * 当前距离这个日期还有几天，dataStr8超出8位就截取8位，如20250911
 * @param dataStr8 - 日期字符串，格式如 "20250911"，超长自动截取前8位
 * @returns 距离目标日期的天数（整数，正数=未来，负数=已过，0=今天）
 */
const getDaysUntil = (dataStr8: string): number => {
  // 截取前8位
  const datePart = dataStr8.slice(0, 8);

  // 校验是否为8位数字
  if (!/^\d{8}$/.test(datePart)) {
    throw new Error(
      `Invalid date string: ${dataStr8}. Expected format: YYYYMMDD`
    );
  }

  // 解析年月日
  const year = parseInt(datePart.slice(0, 4), 10);
  const month = parseInt(datePart.slice(4, 6), 10) - 1; // JS 月份是 0-11
  const day = parseInt(datePart.slice(6, 8), 10);

  // 构造目标日期（本地时区，忽略时间）
  const targetDate = new Date(year, month, day);
  targetDate.setHours(0, 0, 0, 0);

  // 获取当前日期（忽略时间）
  const now = new Date();
  now.setHours(0, 0, 0, 0);

  // 计算差值（毫秒 → 天）
  const diffTime = targetDate.getTime() - now.getTime();
  const diffDays = Math.round(diffTime / (1000 * 60 * 60 * 24));

  return diffDays;
};

const getDate8 = () => {
  const now = new Date();
  const year = now.getFullYear(); // 获取四位数年份，如 2025
  const month = now.getMonth() + 1; // getMonth() 返回 0-11，需加1
  const day = now.getDate(); // 获取日期

  // 格式化为 "YYYY年MM月DD日"
  const formattedDateCN = `${year}${month.toString().padStart(2, "0")}${day
    .toString()
    .padStart(2, "0")}`;
  return formattedDateCN; // 例如：2025年09月12日
};
/**
 * 将多少秒，转义为多少分多少秒
 * - 如果秒为0，则只显示“X分”
 * - 如果分为0，则只显示“X秒”
 * - 如果都是0，显示“0秒”或按需处理
 */
const formatMinute = (second: number): string => {
  if (second <= 0) {
    return "0秒"; // 或 '0分'，但通常 0 秒更合理
  }

  const minutes = Math.floor(second / 60);
  const seconds = second % 60;

  if (minutes === 0) {
    return `${seconds}秒`;
  } else if (seconds === 0) {
    return `${minutes}分`;
  } else {
    return `${minutes}分${seconds}秒`;
  }
};
/**
 * 格式化为 1h50m 格式
 * @param minute - 总分钟数（建议为非负整数）
 * @returns 格式化后的字符串，如 "1h50m"；若为0分钟，返回 "0m"
 */
const formatHm = (minute: number): string => {
  if (minute <= 0) {
    return "0m";
  }

  const hours = Math.floor(minute / 60);
  const minutes = minute % 60;

  let result = "";
  if (hours > 0) {
    result += `${hours}h`;
  }
  if (minutes > 0 || hours === 0) {
    result += `${minutes}m`;
  }

  return result;
};
/**
 * 计算两个 14 位时间字符串之间相差的分钟数（精确到秒）
 * @param start14 14位时间字符串，格式：YYYYMMDDHHmmss，如 "20150811123402"
 * @param end14   14位时间字符串，格式：YYYYMMDDHHmmss，如 "20150811133532"
 * @returns 相差的分钟数（end - start），可为负数，精确到秒（例如 61.5 表示 61分30秒）
 */
const minutesFrom14Date = (start14: string, end14: string): number => {
  if (!/^\d{14}$/.test(start14) || !/^\d{14}$/.test(end14)) {
    throw new Error("时间字符串必须为14位纯数字，格式：YYYYMMDDHHmmss");
  }

  const parse14ToDate = (s: string): Date => {
    const year = parseInt(s.substring(0, 4), 10);
    const month = parseInt(s.substring(4, 6), 10) - 1; // 月份从0开始
    const day = parseInt(s.substring(6, 8), 10);
    const hour = parseInt(s.substring(8, 10), 10);
    const minute = parseInt(s.substring(10, 12), 10);
    const second = parseInt(s.substring(12, 14), 10);

    return new Date(year, month, day, hour, minute, second);
  };

  const startDate = parse14ToDate(start14);
  const endDate = parse14ToDate(end14);

  // 计算时间差（毫秒）
  const diffMs = endDate.getTime() - startDate.getTime();

  // 转换为分钟，保留秒的小数部分（1 分钟 = 60 * 1000 毫秒）
  return diffMs / (60 * 1000); // 返回浮点数，例如 61.5
};

export default {
  dateTo14DigitString,
  dateTo8DigitString,
  digitString14ToDate,
  formatDate,
  formatDate8,
  formatDate8Stype2,
  lastDateByHour,
  lastDateByDay,
  lastDateByMonth,
  minutesFromNow,
  getDaysUntil,
  getDate8,
  formatDate6,
  formatDate14,
  formatMinute,
  formatHm,
  format14ToHms,
  minutesFrom14Date,
};
