import FarmNotice from "@/components/msg/FarmNotice";
import Farm2Request from "@/service/remoteRequests/Farm2Request";
import type { DataResponse } from "@/types/commons/DataResponse";
import { RequestTypeEnum } from "@/types/commons/RequestTypeEnum";
import type { Ref } from "vue";
import type { user } from "../../localuser/utils/user";
import type { minPost } from "../../localuser/utils/minPost";
import farmClientUtils from "@/hook/farmClientUtils";
import type { LocalOrg } from "../../localorg/utils/localorg";
import type { DataResult } from "@/types/commons/DataResult";
import type { AuthGrade } from "../../authgrade/utils/authgrade";
import type { LocalUserInfo } from "../../localuserinfo/utils/localuserinfo";

/**
 * 展示信息（加载用户信息）
 */
const loadUserViewData = async (
  id: string,
  userHandle: (user: user) => void,
  userFilesHandle: (posts: Array<minPost>) => void,
  isLoading?: Ref<boolean, boolean> | null
) => {
  Farm2Request.submit("api/localuser/" + id, RequestTypeEnum.get, isLoading)
    .then((response: DataResponse) => {
      userHandle(response.data as user);
      const dataPlus = response.data as {
        posts: Array<minPost>;
      };
      userFilesHandle(dataPlus.posts);
    })
    .catch((msg: Error) => {
      FarmNotice.byError(msg.message); //报错
    });
};
const loadOrgPath = (
  orgPathHandle: (orgPath: Array<string>) => void,
  orgid?: string,
  isLoading?: Ref<boolean, boolean> | null
) => {
  if (orgid && orgid != "NONE") {
    farmClientUtils.postList(
      "api/localorg/loadOrgPath",
      { id: orgid },
      (list: []) => {
        const types = list as Array<LocalOrg>;
        orgPathHandle(types.map((node) => node.id!));
      },
      isLoading
    );
  }
};
const loadGrads = (orgPathHandle: (list: Array<AuthGrade>) => void) => {
  farmClientUtils.postList("api/authgrade/lists", {}, (list: []) => {
    orgPathHandle(list as Array<AuthGrade>);
  });
};
const orgidProps = {
  children: "children",
  label: "NAME",
  isLeaf: "isLeaf",
};
const loadOrgidTreeNode = (
  node: { data: { ID: string } },
  resolve: (data: unknown) => void
) => {
  const theRules = [];
  if (node.data.ID) {
    theRules.push({ key: "parentid", value: node.data.ID, compara: "=" });
  }
  Farm2Request.search("api/localorg/tree", { rules: theRules })
    .then((remoteResult: DataResult) => {
      // 对返回的数据进行处理，给 id 为 'NONE' 的节点加上 disabled
      const processedData = (remoteResult.data || []).map((item) => {
        const treeNode = item as { ID: string };
        if (treeNode.ID === "NONE") {
          return { ...treeNode, disabled: true };
        }
        return item;
      });
      resolve(processedData);
    })
    .catch((msg: Error) => {
      FarmNotice.byError(msg.message);
    });
};

const loadUserInfoData = async (
  id: string,
  userHandle: (user: LocalUserInfo) => void,
  isLoading?: Ref<boolean, boolean> | null
) => {
  Farm2Request.submit("api/localuserinfo/" + id, RequestTypeEnum.get, isLoading)
    .then((response: DataResponse) => {
      userHandle(response.data as LocalUserInfo);
    })
    .catch((msg: string) => {
      FarmNotice.byError(msg); //报错
    });
};

export default {
  loadUserViewData,
  loadUserInfoData,
  loadOrgPath,
  orgidProps,
  loadOrgidTreeNode,
  loadGrads,
};
