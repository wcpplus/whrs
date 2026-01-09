import { ElNotification } from "element-plus";

let curentNotis = { close: () => {} };

const byError = (msg: string): void => {
  if (msg) {
    msg = msg.toString().replace("Error:", "");
  }
  curentNotis.close();
  curentNotis = ElNotification({
    title: "错误",
    message: msg,
    duration: 3000,
    type: "error",
  });
};

const byInfo = (msg: string): void => {
  curentNotis.close();
  curentNotis = ElNotification({
    title: "提示",
    message: msg,
    duration: 3000,
    type: "info",
  });
};

const bySuccess = (msg: string): void => {
  curentNotis.close();
  curentNotis = ElNotification({
    title: "成功",
    message: msg,
    duration: 3000,
    type: "success",
  });
};

export default { byError, bySuccess, byInfo };
