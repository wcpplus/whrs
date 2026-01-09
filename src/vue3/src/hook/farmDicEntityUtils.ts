import type { DicType } from "@/views/system/dictype/utils/dictype";
import farmClientUtils from "./farmClientUtils";

/**
 * 获取草稿
 * @param key 唯一标识符前缀
 * @param id 唯一ID
 * @returns 转义后的字符串 | null
 */
function getOptions(
  keyid: string,
  options: (options: Array<DicType>) => void
): void {
  farmClientUtils.postList(
    "api/dicentity/dicTypes",
    { id: keyid },
    (list: Array<DicType>) => {
      options(list);
    }
  );
}

export default { getOptions };
