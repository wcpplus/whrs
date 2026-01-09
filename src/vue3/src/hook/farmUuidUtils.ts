const getUUID = (): string => {
  return "xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx".replace(/[xy]/g, (char) => {
    const random = (Math.random() * 16) | 0;
    const value = char === "x" ? random : (random & 0x3) | 0x8;
    return value.toString(16);
  });
};

const getUUIDMin = (): string => {
  return getUUID().replaceAll("-", "").substring(16);
};

export default { getUUID, getUUIDMin };
