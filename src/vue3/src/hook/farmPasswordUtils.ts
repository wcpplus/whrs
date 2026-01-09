import farmEncodeUtils from "./farmEncodeUtils";

// 定义所有可能的字符
const CHARACTERS =
  "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

//获得随机盐
function getSalt(len: number) {
  let result = "";
  for (let i = 0; i < len; i++) {
    // 生成一个从 0 到 CHARACTERS.length - 1 的随机整数
    const index = Math.floor(Math.random() * CHARACTERS.length);
    // 获取对应的字符并添加到结果字符串中
    result += CHARACTERS.charAt(index);
  }
  return result;
}

//密码加密=>webpassword
function encodeWebPassword(
  syscode: string,
  webcode: string,
  rowPassword: string,
  fun: (arg0: string) => void
) {
  farmEncodeUtils.sha256(syscode + rowPassword).then((groupHash) => {
    farmEncodeUtils
      .sha256(webcode + syscode + groupHash)
      .then((webPassword) => {
        fun(webPassword);
      });
  });
}

//密码加密=>syspassword
function encodeSysPassword(rowPassword: string, fun: (arg0: string) => void) {
  const salt = getSalt(10);
  farmEncodeUtils.sha256(salt + rowPassword).then((groupHash) => {
    fun(salt + groupHash);
  });
}

export default { encodeWebPassword, encodeSysPassword };
