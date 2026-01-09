function formatNumber(num: number, fixed: number): string {
  // 先四舍五入到 fixed 位小数（数值计算）
  const rounded = Math.round(num * Math.pow(10, fixed)) / Math.pow(10, fixed);

  // 判断 rounded 是否为整数（且 fixed >= 1 时才考虑去掉 .0）
  if (fixed > 0 && Number.isInteger(rounded)) {
    return rounded.toString(); // 如 1 → "1"
  } else {
    // 否则保留 fixed 位小数
    return num.toFixed(fixed); // 如 1.23 → "1.23"（当 fixed=2）
  }
}

export default { formatNumber };
