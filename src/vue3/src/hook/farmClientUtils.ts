import farm2Request from "@/service/remoteRequests/Farm2Request";
import type { DataResponse } from "@/types/commons/DataResponse";
import { RequestTypeEnum } from "@/types/commons/RequestTypeEnum";
import notice from "@/components/msg/FarmNotice";
import type { Ref } from "vue";
/**
 * 获得一个远程数字
 * @param url 资源地址
 * @param handle  处理获取的数字
 */
function getNum(url: string, handle: (num: number) => void, data?: object) {
  farm2Request
    .submit(url, RequestTypeEnum.post, null, data)
    .then((response: DataResponse) => {
      handle(response.data as number);
    })
    .catch((msg: string) => {
      notice.byError(msg); // 报错
    });
}
/**
 * 获得一个远程对象数组
 * @param url
 * @param data
 * @param handle
 */
function postList(
  url: string,
  data: object,
  handle: (list: []) => void,
  isLoading?: Ref<boolean, boolean> | null
) {
  if (isLoading) {
    isLoading.value = true;
  }
  setTimeout(() => {
    farm2Request
      .submit(url, RequestTypeEnum.post, null, data)
      .then((response: DataResponse) => {
        if (isLoading) {
          isLoading.value = false;
        }
        handle(response.data as []);
      })
      .catch((msg: string) => {
        if (isLoading) {
          isLoading.value = false;
        }
        notice.byError(msg); // 报错
      });
  }, 100);
}
function postObject(
  url: string,
  data: object,
  handle: (obj: object) => void,
  isLoading?: Ref<boolean, boolean> | null
) {
  if (isLoading) {
    isLoading.value = true;
  }
  setTimeout(() => {
    farm2Request
      .submit(url, RequestTypeEnum.post, null, data)
      .then((response: DataResponse) => {
        if (isLoading) {
          isLoading.value = false;
        }
        handle(response.data as object);
      })
      .catch((msg: string) => {
        if (isLoading) {
          isLoading.value = false;
        }
        notice.byError(msg); // 报错
      });
  }, 100);
}

export default {
  getNum,
  postList,
  postObject,
};
