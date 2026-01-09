//简化validat参数封装，通过简单参数返回复杂的配置内容

// [
//   { required: true, message: '必填项', trigger: 'blur' },
//   { min: 3, max: 5, message: '长度要求： 2 - 16', trigger: 'blur' },
// ]
type Rule = {
  // eslint-disable-next-line @typescript-eslint/no-unsafe-function-type
  validator?: Function;
  required?: boolean;
  message?: string;
  trigger: string;
  min?: number;
  max?: number;
};

/**
 * 普通校验
 * @param required 是否必填
 * @param minNum 最新字符数量
 * @param maxNum 最大字符数
 * @returns
 */
const getRules = (required: boolean, minNum?: number, maxNum?: number) => {
  const dataRules: Rule[] = [];
  if (required) {
    //如果没有录入信息，则不提示文字“请填写”
    dataRules.push({ required: true, message: "", trigger: "blur" });
  }
  if (minNum || maxNum) {
    if (!minNum) {
      minNum = 0;
    }
    if (!maxNum) {
      maxNum = 64;
    }

    dataRules.push({
      min: minNum,
      max: maxNum,
      message:
        minNum == 0
          ? "长度： 不超过 " + maxNum
          : "长度： " + minNum + " 到 " + maxNum,
      trigger: "blur",
    });
  }

  return dataRules;
};

/**
 * 自定义校验（自定义函数）
 * @param validateFunc 自定义校验函数
 * 如：const validatePass = (rule, value, callback) => {callback(new Error(value));};
 * 描述：rule为校验规则 ，value为当前表单值，callback可通过callback(new Error(value));返回错误信息,正确时返回callback();
 * @param required 是否必填
 * @param minNum 最新字符数量
 * @param maxNum 最大字符数
 * @returns
 */
const getCustomRules = (
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  validateFunc: any,
  required: boolean,
  minNum?: number,
  maxNum?: number
) => {
  const dataRules: Rule[] = getRules(required, minNum, maxNum);
  dataRules.push({ validator: validateFunc, trigger: "blur" });
  return dataRules;
};

/**
 *自定义校验（正则表达式）
 * @param regEx 正则表达式
 * @param msg 不符合时报错信息
 * @param required
 * @param minNum
 * @param maxNum
 * @returns
 */
const getRegExpRules = (
  validRx: ValidRx,
  required: boolean,
  minNum?: number,
  maxNum?: number
) => {
  const validateFunc = (
    _rule: unknown,
    value: string,
    callback: (arg0?: Error | undefined) => void
  ) => {
    // 🟢 关键修复：如果非必填且值为空，直接通过
    if (!required && (value === "" || value == null)) {
      return callback(); // 不校验
    }
    // 字符校验
    const usernamePattern = validRx.regEx;
    if (!usernamePattern.test(value)) {
      return callback(new Error(validRx.msg));
    }

    return callback();
  };
  return getCustomRules(validateFunc, required, minNum, maxNum);
};

interface ValidRx {
  regEx: RegExp;
  msg: string;
}
/**
 * 预置正则验证表达式
 */
const ValidRxs = {
  loginname: {
    regEx: /^[a-zA-Z][a-zA-Z0-9_-]*$/,
    msg: "必须字母开头，只允许字母、数字或下划线",
  } as ValidRx,
  password: {
    regEx:
      /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{6,}$/,
    msg: "必须至少6个字符包含大写字母、小写字母、数字和特殊字符",
  } as ValidRx,
  indexKey: {
    regEx: /^[a-zA-Z0-9_]+$/,
    msg: "只允许字母、数字或下划线",
  } as ValidRx,
  phone: {
    regEx: /^(\+?86[-\s]?)?1[3-9]\d{9}$|^\+[1-9]\d{1,14}$|^[1-9]\d{10,14}$/,
    msg: "请输入有效的手机号（如：13812345678 或 +8613812345678）",
  } as ValidRx,
  email: {
    regEx: /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/,
    msg: "请输入有效的电子邮箱地址",
  } as ValidRx,
  idCard: {
    regEx: /^(\d{17}[\dXx]|\d{15})$/,
    msg: "身份证格式不正确，请输入18位身份证号（末位可为X）",
  } as ValidRx,
  bankCard: {
    regEx: /^\d{16,19}$/,
    msg: "银行卡号格式不正确，请输入16至19位数字",
  } as ValidRx,
  salary: {
    regEx: /^(?:[1-9]\d{0,6}|0)(?:\.\d{1,2})?$/,
    msg: "工资格式不正确，请输入大于0的数字，最多保留两位小数（如：5000 或 8500.50）",
  } as ValidRx,
};

export default { getRules, getCustomRules, getRegExpRules, ValidRxs };
