function getFileSizeTitle(size: number): string {
  const units = ["B", "KB", "MB", "GB"];
  let unitIndex = 0;

  // Determine the appropriate unit
  while (size >= 1024 && unitIndex < units.length - 1) {
    size /= 1024;
    unitIndex++;
  }

  // Determine the number of decimal places based on the size value
  let formattedSize;
  if (size >= 100) {
    formattedSize = `${Math.round(size)} ${units[unitIndex]}`;
  } else if (size >= 10) {
    formattedSize = `${size.toFixed(1)} ${units[unitIndex]}`;
  } else {
    formattedSize = `${size.toFixed(2)} ${units[unitIndex]}`;
  }

  return formattedSize;
}

export default {
  getFileSizeTitle,
};
