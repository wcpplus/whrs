package org.farm2.base.elementplus;

import org.apache.commons.lang3.StringUtils;
import org.farm2.base.elementplus.domain.TreeNode;

import java.util.*;
import java.util.stream.Collectors;

public class FarmElTreeUtils {
    /**
     * 组合成treeNode结构数组，TreeNode为
     * {
     * String id;
     * String label;
     * List<TreeNode> children;
     * }
     *
     * @param data        数据集合列表，每个map为一条数据，包含节点id，上级节点id，排序号码 ，节点名称
     * @param idKey       Map中表示ID的key一般为ID
     * @param parentidKey Map中表示上级节点ID的key 一般为PARENTID
     * @param sortKey     map中表示排序号码的key 一般为SORTCODE
     * @param nameKey     map中表示名称的key 一般为NAME
     * @param rootParent  当parentid的值为rootParent时表示这个节点为根节点，可以逗号分隔传入多个根节点值
     * @return
     */
    public static List<TreeNode> getElTrees(List<Map<String, Object>> data, String idKey, String parentidKey, String nameKey, String sortKey, String rootParent) {

        if (data == null || data.isEmpty()) {
            return Collections.emptyList(); // 如果输入数据为空，返回空列表
        }

        // 创建一个映射来保存所有节点，以便根据ID快速查找
        Map<String, TreeNode> nodeMap = new HashMap<>();

        // 将rootParent字符串分割成一组根节点ID
        Set<String> rootParents = rootParent == null ? Collections.emptySet() : new HashSet<>(Arrays.asList(rootParent.split(",")));

        // 第一次遍历：创建所有节点并添加到映射中
        for (Map<String, Object> item : data) {
            String id = (String) item.get(idKey);
            String name = (String) item.get(nameKey);
            String icon = (String) item.get("icon");
            Boolean disabled = (Boolean) item.get("disabled");
            TreeNode node = new TreeNode();
            node.setId(id);
            if (StringUtils.isNotBlank(icon)) {
                node.setIcon(icon);
            }
            if (disabled != null) {
                node.setDisabled(disabled);
            }
            node.setLabel(name);
            nodeMap.put(id, node); // 将节点放入映射中
        }

        // 第二次遍历：为每个节点分配父节点，并将子节点加入父节点的children列表中
        List<TreeNode> rootNodes = new ArrayList<>(); // 用于存储顶级节点
        for (Map<String, Object> item : data) {
            String id = (String) item.get(idKey);
            String parentId = (String) item.get(parentidKey);
            Integer sortCode = parentId != null && item.containsKey(sortKey) ? (Integer) item.get(sortKey) : 0;
            TreeNode node = nodeMap.get(id);

            // 如果parentId为空或在rootParent中，则该节点为顶级节点
            if (parentId == null || rootParents.contains(parentId)) {
                rootNodes.add(node); // 添加到顶级节点列表
            } else {
                // 查找父节点并将当前节点作为其子节点
                TreeNode parentNode = nodeMap.get(parentId);
                if (parentNode != null) {
                    if (parentNode.getChildren() == null) {
                        parentNode.setChildren(new ArrayList<>());
                    }
                    parentNode.getChildren().add(node);
                }
            }
        }
        // 对每个节点的子节点按照sortKey进行排序
        for (TreeNode node : nodeMap.values()) {
            if (node.getChildren() != null) {
                node.setChildren(node.getChildren().stream().sorted(Comparator.comparingInt(child ->
                        // 从原始数据中找到对应的排序码
                        (int) data.stream().filter(d -> d.get(idKey).equals(child.getId())).findFirst().orElse(Collections.emptyMap()).getOrDefault(sortKey, 0))).collect(Collectors.toList()));
            }
        }
        return rootNodes; // 返回顶级节点列表
    }
}
