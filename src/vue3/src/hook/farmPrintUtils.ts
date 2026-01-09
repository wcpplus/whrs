

/**
 *   通过css控制打印样式，如base.css中的：@media print {
 * @returns
 */
const print = () => {
  // 获取所有需要懒加载的图片（有 data-src 的 img）
  const lazyImages = document.querySelectorAll<HTMLElement>("img[data-src]");

  if (lazyImages.length === 0) {
    // 没有懒加载图片，直接打印
    window.print();
    return;
  }

  // 创建一个 Promise 数组，用于等待所有图片加载完成
  const loadPromises = Array.from(lazyImages).map((img) => {
    return new Promise<void>((resolve) => {
      const src = img.getAttribute("data-src");
      if (!src) {
        resolve(); // 没有 data-src，跳过
        return;
      }

      // 确保 img 被视为 HTMLImageElement
      const image = img as HTMLImageElement;

      // 创建一个临时 Image 对象来预加载
      const preloadImg = new Image();
      preloadImg.onload = () => {
        // 加载成功后，将 img 的 src 替换为高清图（用于打印）
        image.src = src;
        resolve();
      };
      preloadImg.onerror = () => {
        console.warn("图片加载失败:", src);
        // 即使失败也继续，避免卡住
        resolve();
      };
      preloadImg.src = src; // 开始加载
    });
  });

  // 等待所有图片加载完成后再打印
  Promise.all(loadPromises).then(() => {
    // 可选：延迟一点确保渲染（某些浏览器需要）
    setTimeout(() => {
      window.print();
    }, 100);
  });
};

export default { print };
