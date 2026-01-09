// async function sha256(message: string) {
//   // encode as UTF-8
//   const msgBuffer = new TextEncoder().encode(message);

//   // hash the message
//   const hashBuffer = await crypto.subtle.digest("SHA-256", msgBuffer);

//   // convert ArrayBuffer to Array
//   const hashArray = Array.from(new Uint8Array(hashBuffer));

//   // convert bytes to hex string
//   const hashHex = hashArray
//     .map((b) => b.toString(16).padStart(2, "0"))
//     .join("");
//   return hashHex;
// }

// export default { sha256 };

import { sha256 as jsSHA256 } from "js-sha256"; // 引入 js-sha256

async function sha256(message: string): Promise<string> {
  // 使用 js-sha256 直接生成哈希值（无需手动编码和转换）
  const hashHex = jsSHA256(message);

  return hashHex;
}

export default { sha256 };
