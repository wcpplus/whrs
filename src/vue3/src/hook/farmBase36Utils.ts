// Base36Converter.ts
class Base36Converter {
  // 定义36进制的字符集：0-9 + a-z
  private static readonly CHARACTERS: string =
    "abcdefghijklmnopqrstuvwxyz0123456789";

  /**
   * 将10进制数字转换为36进制字符串
   *
   * @param decimalNumber 要转换的10进制数字
   * @returns 转换后的36进制字符串
   */
  public static decimalToBase36(decimalNumber: number): string {
    if (decimalNumber === 0) {
      return "0"; // 特殊情况处理，0的36进制还是0
    }

    let result: string = "";
    const isNegative: boolean = decimalNumber < 0;
    decimalNumber = Math.abs(decimalNumber);

    while (decimalNumber > 0) {
      const remainder: number = decimalNumber % 36;
      result = Base36Converter.CHARACTERS[remainder] + result; // 从字符集中获取对应的字符
      decimalNumber = Math.floor(decimalNumber / 36);
    }

    return isNegative ? "-" + result : result;
  }

  /**
   * 将36进制字符串转换为10进制数字
   *
   * @param base36String 要转换的36进制字符串
   * @returns 转换后的10进制数字
   */
  public static base36ToDecimal(base36String: string): number {
    if (!base36String || base36String.trim() === "") {
      throw new Error("输入的36进制字符串不能为空");
    }

    base36String = base36String.toLowerCase(); // 统一转换为小写
    const isNegative: boolean = base36String.startsWith("-");
    if (isNegative) {
      base36String = base36String.substring(1); // 去掉负号
    }

    let result: number = 0;
    for (const char of base36String) {
      const digitValue: number = Base36Converter.CHARACTERS.indexOf(char);
      if (digitValue === -1) {
        throw new Error(`无效的36进制字符: ${char}`);
      }
      result = result * 36 + digitValue;
    }

    return isNegative ? -result : result;
  }
}

/**
 * 导出方法
 */
function convertDecimalToBase36(decimalNumber: number): string {
  const base36String = Base36Converter.decimalToBase36(decimalNumber);
  return base36String;
}

function convertBase36ToDecimal(base36String: string): number {
  const decimalNumber = Base36Converter.base36ToDecimal(base36String);
  return decimalNumber;
}

export default { convertDecimalToBase36, convertBase36ToDecimal };
