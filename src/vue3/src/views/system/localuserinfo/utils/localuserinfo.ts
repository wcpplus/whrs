import type { ResourceFile } from "../../resourcefile/utils/resourcefile";

//用户条件查询的封装
export interface LocalUserInfo {
  id?: string;
  sex?: string;
  birthdate?: string;
  phonecode?: string;
  emptime?: string;
  email?: string;
  idcode?: string;
  citycode?: string;
  pan?: string;
  contractfid?: string;
  eqfid?: string;
  degfid?: string;
  merfid?: string;
  icardfid?: string;
  process?: number;
  userkey?: string;
  contractfFile?: ResourceFile;
  eqfFile?: ResourceFile;
  degfFile?: ResourceFile;
  merfFile?: ResourceFile;
  icardfFile?: ResourceFile;
}
