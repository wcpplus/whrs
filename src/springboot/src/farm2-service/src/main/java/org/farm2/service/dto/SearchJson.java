package org.farm2.service.dto;

import java.util.List;

/**
 * 封装检索视图的json条件对象
 */
public class SearchJson {
    private List<Type> types;
    private List<Tag> tags;
    private String vword;

    // Getters and Setters
    public static class Type {
        private String name;
        private String id;

        // Getters and Setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    public static class Tag {
        private String title;
        private String key;

        // Getters and Setters
        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }
    }

    public List<Type> getTypes() {
        return types;
    }

    public void setTypes(List<Type> types) {
        this.types = types;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public String getVword() {
        return vword;
    }

    public void setVword(String vword) {
        this.vword = vword;
    }
}
