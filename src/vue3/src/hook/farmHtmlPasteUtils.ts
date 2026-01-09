import farmFileUtils from "./farmFileUtils";

/**
 * 从excel的Html中获取表格片段
 * @param html
 * @returns
 */
function parsingTableByExcelHtml(html: string): string {
  return html;
}
/**
 * 从剪切板中获取图片url
 * @param clipboardData
 * @param func
 * @returns
 */
function PasteImg(clipboardData: DataTransfer, func: (url: string) => void) {
  // 遍历剪贴板中的每一项数据
  for (let i = 0; i < clipboardData.items.length; i++) {
    const item = clipboardData.items[i];
    // 检查数据项是否为图片类型
    if (item.type.indexOf("image") !== -1) {
      // 获取图片文件对象
      const blob = item.getAsFile();
      // 如果文件对象存在
      if (blob) {
        // 使用FileReader对象读取文件
        const reader = new FileReader();
        // 当文件读取完成后，将图片转换为base64编码，并插入到编辑器中
        reader.onload = (e) => {
          const base64Image = e.target?.result as string;
          farmFileUtils.uploadBase64File(base64Image, (url: string) => {
            func(url);
          });
        };
        // 读取文件为Data URL格式
        reader.readAsDataURL(blob);
      }
    }
  }
}

export default {
  parsingTableByExcelHtml,
  PasteImg,
};
