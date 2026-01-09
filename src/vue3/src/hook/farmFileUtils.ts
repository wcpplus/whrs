import Farm2Request from "@/service/remoteRequests/Farm2Request";
import type { DataResponse } from "@/types/commons/DataResponse";
import { RequestTypeEnum } from "@/types/commons/RequestTypeEnum";
import notice from "@/components/msg/FarmNotice";
async function uploadBase64File(
  base64Str: string,
  handle: (url: string) => void
) {
  Farm2Request.submit("api/files/base64", RequestTypeEnum.post, null, {
    base64: base64Str,
  })
    .then((response: DataResponse) => {
      //console.log(response.data);
      handle(
        Farm2Request.getFullPath(
          "api/files/download/" + (response.data as { id: string }).id
        )
      );
    })
    .catch((msg: string) => {
      notice.byError(msg); //报错
      handle("images/info/none.png");
    });
}
/**
 * 从文件名中提取不带扩展名的标题部分。
 *
 * 该函数会移除文件名中最后一个点（.）之后的所有内容（即扩展名），
 * 但会保留以点开头的隐藏文件名（如 `.gitignore`）不变，避免误处理。
 *
 * @param fileName - 可选的原始文件名字符串。如果为 `undefined` 或空字符串，则返回空字符串。
 * @returns 去除扩展名后的文件名；若输入无效或无有效扩展名，则返回原输入或空字符串。
 *
 * @example
 * getFileTitle('document.pdf')      // 'document'
 * getFileTitle('archive.tar.gz')    // 'archive.tar'
 * getFileTitle('.env')              // '.env'
 * getFileTitle('README')            // 'README'
 * getFileTitle(undefined)           // ''
 */
const getFileTitle = (fileName?: string): string => {
  if (fileName) {
    const lastDotIndex = fileName.lastIndexOf(".");
    // 确保有点，并且点不在开头（排除 .env, .gitignore 等）
    if (lastDotIndex > 0) {
      return fileName.substring(0, lastDotIndex);
    }
  }
  // 如果没有有效后缀，或 fileName 为空/undefined，返回原值（或空字符串）
  return fileName || "";
};

export default { uploadBase64File, getFileTitle };
