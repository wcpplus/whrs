import Farm2Request from "@/service/remoteRequests/Farm2Request";

/**进度处理信息回调
 *----------------------------回调信息----------------------------------------------
 * 定义进程
 *let eventSource: EventSource | null;
 //进程回调
 * eventSource = farmProcessUtils.processHandle(file.id, (msg: string, percent: number, isEnd: boolean) => {
 *
 *   });
 *onUnmounted(() => {
 //记得关闭
 * if (eventSource) {
 *   eventSource.close();
 * }
 *});
 *--------------------------------------------------------------------------
 * @param processKey
 * @param handleFunc
 * @returns
 */
function processHandle(
  processKey: string,
  processType: string,
  handleFunc: (msg: string, percent: number, isEnd: boolean) => void
) {
  const eventSource = new EventSource(
    Farm2Request.getFullPath("api/process/stream") +
      "?key=" +
      processKey +
      "&type=" +
      processType
  );
  eventSource.onmessage = (event: MessageEvent) => {
    // 将新消息追加到 messages 中
    const msg = JSON.parse(event.data) as { percent: number; msg: string }; // 将 \n 替换为 <br />
    handleFunc(msg.msg, msg.percent, msg.percent >= 100);
    if (msg.percent >= 100) {
      if (eventSource) {
        eventSource.close();
      }
    }
  };
  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  eventSource.onerror = (error: Event) => {
    // console.error('EventSource failed:', error);
    if (eventSource) {
      eventSource.close();
    }
  };

  return eventSource;
}

export default {
  processHandle,
};
