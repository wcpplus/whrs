package com.farm2.lucene.result;


import com.farm2.lucene.domain.ResultTag;
import com.farm2.lucene.query.DocumentQuery;
import com.farm2.lucene.query.DocumentRule;
import com.farm2.lucene.query.DocumentSort;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.*;
import org.farm2.tools.bean.FarmBeanUtils;
import org.farm2.tools.db.DataResult;
import org.farm2.tools.db.ResultDataHandle;
import org.farm2.tools.json.FarmJsons;
import org.farm2.tools.web.Farm2HtmlUtils;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DocumentResult {
    private int totalSize;
    private int totalPage;
    private int page;
    private int pageSize;
    private List<Map<String, Object>> data;
    private DocumentSort sort;
    private List<ResultTag> tags = new ArrayList<>();
    private List<String> highlightWords;
    //---------------------
    private String searchType = "LU";//当前检索类型
    private String searchTitle = "";//当前检索类型


    public DocumentResult(DataResult result, Map<String, String> dic) {
        totalSize = result.getTotalSize();
        totalPage = result.getTotalPage();
        page = result.getPage();
        pageSize = result.getPageSize();
        searchType = "DQ";
        sort = new DocumentSort(result.getSorts(), dic);
        List<Map<String, Object>> dataTemp = new ArrayList<>();
        for (Map<String, Object> obj : result.getData()) {
            Map<String, Object> documentData = new HashMap<>();
            for (Map.Entry<String, Object> node : obj.entrySet()) {
                if (dic.get(node.getKey()) != null) {
                    documentData.put(dic.get(node.getKey()), node.getValue());
                }
            }
            dataTemp.add(documentData);
        }
        data = dataTemp;
    }

    public DocumentResult(List<Document> list, int num, DocumentQuery query, IndexSearcher searcher, Query luceneQuery) {
        if (num == 0) {
            num = list.size();
        }
        highlightWords = query.getHighlightWords();
        this.data = getPageData(list, query, searcher, luceneQuery);
        this.tags = getTags(list, query);
        this.sort = query.getSort();
        this.page = query.getPage();
        this.pageSize = query.getPageSize();
        this.totalSize = num;
        this.totalPage = num / query.getPageSize();
        if (num % query.getPageSize() > 0) {
            this.totalPage = this.totalPage + 1;
        }
    }

    /**
     * 对list分页，返回分页数据（从query提取分页数据）
     *
     * @param list     为全部数据
     * @param query     为当前页数
     * @return
     */
    private List<Map<String, Object>> getPageData(List<Document> list, DocumentQuery query, IndexSearcher searcher, Query luceneQuery) {
        List<Map<String, Object>> result = new ArrayList<>();
        if (list == null || list.isEmpty() || query.getPage() <= 0 || query.getPageSize() <= 0) {
            return result; // 返回空列表或处理异常情况
        }
        int totalRecords = list.size();
        int totalPages = (int) Math.ceil((double) totalRecords / query.getPageSize());
        if (query.getPage() > totalPages) {
            return result; // 如果请求的页数大于总页数，返回空列表
        }
        int fromIndex = (query.getPage() - 1) * query.getPageSize();
        int toIndex = Math.min(fromIndex + query.getPageSize(), totalRecords);
        // 获取分页数据
        List<Document> pageData = list.subList(fromIndex, toIndex);
        for (Document doc : pageData) {
            Map<String, Object> node = new HashMap<>();
            for (IndexableField field : doc.getFields()) {
                String val = doc.get(field.name());
                if (query.isHighlight()) {
                    try {
                        val = highlightField(searcher, field.name(), luceneQuery, doc.get(field.name()), query.getHighlightLen());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                node.put(field.name(), val);
            }
            result.add(node);
        }
        return result;
    }


    /**
     * 从结果集合中加载标签
     *
     * @param list
     * @return
     */
    private List<ResultTag> getTags(List<Document> list, DocumentQuery query) {
        if (query.getTagField() == null) {
            return new ArrayList<>();
        }
        List<ResultTag> tags = new ArrayList<>();
        Map<String, ResultTag> dic = new HashMap<>();
        for (Document doc : list) {
            if (tags.size() >= 30) {
                break;
            }
            String titles = doc.get(query.getTagField());
            if (StringUtils.isNotBlank(titles)) {
                for (String title : titles
                        .replace("，", ",")
                        .replace(" ", ",")
                        .replace("、", ",")
                        .split(",")) {
                    if (StringUtils.isNotBlank(title)) {
                        if (dic.keySet().contains(title)) {
                            dic.get(title).add(1);
                        } else {
                            ResultTag tag = new ResultTag(title);
                            tag.setKnowNum(1);
                            dic.put(title, tag);
                            tags.add(tag);
                        }
                    }
                }
            }
        }
        tags.sort(new Comparator<ResultTag>() {
            @Override
            public int compare(ResultTag o1, ResultTag o2) {
                return o2.getKnowNum() - o1.getKnowNum();
            }
        });
        return tags;
    }

    @Deprecated
    public DocumentResult() {
        data = new ArrayList<>();
    }

    /**
     * 对指定字段进行高亮处理
     *
     * @param searcher   IndexSearcher 实例
     * @param field      字段名
     * @param query      查询对象
     * @param fieldValue 字段值
     * @return 高亮后的字符串
     * @throws IOException
     * @throws InvalidTokenOffsetsException
     */
    private String highlightField(IndexSearcher searcher, String field, Query query, String fieldValue, int highLightSize) throws Exception {
        if (fieldValue == null || fieldValue.isEmpty()) {
            return null;
        }
        if (field.equals("title") || field.equals("text")) {
            //避免html内容在检索页面执行
            fieldValue = Farm2HtmlUtils.getTextByHtml(fieldValue);
        }
        int limitlen = highLightSize;
        Analyzer analyzer = new IKAnalyzer(false);
        QueryScorer scorer = null;
        if (highlightWords == null || highlightWords.size() <= 0) {
            scorer = new QueryScorer(query, field);
        } else {
            scorer = getHighlightQuery(field, analyzer, highlightWords);
        }
        SimpleHTMLFormatter formatter = new SimpleHTMLFormatter("<span class='lucene_index'>", "</span>");
        Highlighter highlighter = new Highlighter(formatter, scorer);
        highlighter.setTextFragmenter(new SimpleFragmenter(limitlen));
        TokenStream tokenStream = analyzer.tokenStream(field, fieldValue);
        String highlightedText = highlighter.getBestFragment(tokenStream, fieldValue);
        if (highlightedText != null) {
            return highlightedText;
        } else {
            if (fieldValue.length() > limitlen) {
                return fieldValue.substring(0, limitlen - 1);
            } else {
                return fieldValue;
            }
        }
    }

    /**
     * 按指定关键字生成高亮
     *
     * @param field
     * @param analyzer
     * @param words    指定关键字
     * @return
     * @throws IOException
     */
    private QueryScorer getHighlightQuery(String field, Analyzer analyzer, List<String> words) throws IOException {
        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        for (String keyword : words) {
            // 使用 Analyzer 对关键词进行分词
            TokenStream tokenStream = analyzer.tokenStream(field, keyword);
            CharTermAttribute termAttr = tokenStream.addAttribute(CharTermAttribute.class);
            tokenStream.reset();
            while (tokenStream.incrementToken()) {
                String term = termAttr.toString();
                // 构造 TermQuery 并加入 BooleanQuery
                builder.add(new TermQuery(new Term(field, term)), BooleanClause.Occur.SHOULD);
            }
            tokenStream.end();
            tokenStream.close();
        }
        Query customQuery = builder.build();
        return new QueryScorer(customQuery, field);
    }
    public DocumentSort getSort() {
        return sort;
    }

    public int getTotalSize() {
        return totalSize;
    }

    public String getSearchType() {
        return searchType;
    }
    public int getTotalPage() {
        return totalPage;
    }

    public List<Map<String, Object>> getData() {
        return data;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }
    public int getPage() {
        return page;
    }

    public int getPageSize() {
        return pageSize;
    }
    public String getSearchTitle() {
        return searchTitle;
    }

    public void setSearchTitle(String searchTitle) {
        this.searchTitle = searchTitle;
    }

    public List<ResultTag> getTags() {
        return tags;
    }

    public <T> List<T> getData(Class<T> dataClass) {
        List<T> entityList = new ArrayList<>();
        for (Map<String, Object> map : data) {
            try {
                T entity = dataClass.getDeclaredConstructor().newInstance();
                FarmBeanUtils.copyProperties(map, entity);
                entityList.add(entity);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return entityList;
    }

    public String toJson() {
        return FarmJsons.toFormatJson(this);
    }

    public void runFormatTime(String title) {
        runFormatTime(title, "yyyy-MM-dd HH:mm");
    }

    public void runFormatTime(String title, String yyyyMMddHHmmss) {
        List<Map<String, Object>> list = this.data;
        for (Map<String, Object> node : list) {
            try {
                String time = null;
                if (node.get(title) instanceof java.sql.Date) {
                    java.util.Date d = new java.util.Date(((java.sql.Date) node.get(title)).getTime());
                    SimpleDateFormat ormat = new SimpleDateFormat("yyyyMMdd");
                    time = ormat.format(d);
                } else {
                    time = (String) node.get(title);
                }
                if (time == null || time.trim().length() <= 0) {
                    continue;
                }
                if (time.indexOf("-") + time.indexOf(":") > 0) {
                    // 只要有-和：就算是已经被转义过的时间，就不用再转换了
                    continue;
                }
                if (12 == time.length()) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
                    SimpleDateFormat newSdf = new SimpleDateFormat(yyyyMMddHHmmss);
                    Date date = sdf.parse(time);
                    node.put(title, newSdf.format(date));
                } else {
                    try {
                        time = (time + "00000000000000").substring(0, 14);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                        SimpleDateFormat newSdf = new SimpleDateFormat(yyyyMMddHHmmss);
                        Date date = sdf.parse(time);
                        node.put(title, newSdf.format(date));
                    } catch (ParseException e) {
                        node.put(title, null);
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 迭代结果数据
     *
     * @param handler
     */
    public void runDataHandle(ResultDataHandle handler) {
        for (Map<String, Object> row : data) {
            handler.handle(row);
        }
    }

    /**
     * 对结果集合中某个字段值进行替换，用于数据字典的翻译
     *
     * @param str   字典描述字符串 "1:可用,2:禁用"
     * @param title 翻译字段名 如：TYPE
     */
    public DocumentResult runDictionary(String str, String title) {
        String[] mapStr = str.trim().split(",");
        Map<String, String> dictionary = new HashMap<String, String>();
        for (String keyValue : mapStr) {
            String[] node = keyValue.split(":");
            try {
                dictionary.put(node[0], node[1]);
            } catch (Exception e) {
                throw new RuntimeException(e + ",请检查字典项：'" + keyValue + "'");
            }
        }
        runDictionary(dictionary, title);
        return this;
    }

    /**
     * 对结果集合中某个字段值进行替换，用于数据字典的翻译
     *
     * @param dictionary 字典map
     * @param title      翻译字段名 如：TYPE
     */
    public DocumentResult runDictionary(Map<String, String> dictionary, String title) {
        for (Map<String, Object> node : data) {
            String key = String.valueOf(node.get(title));
            Object value = dictionary.get(key);
            if (value != null) {
                node.put(title, value);
            }
        }
        return this;
    }

    /**
     * 删除结果集中的高亮
     *
     * @param fields
     * @return
     */
    public DocumentResult disHighLight(String... fields) {
        for (String field : fields) {
            runDataHandle(new ResultDataHandle() {
                @Override
                public void handle(Map<String, Object> row) {
                    String val = (String) row.get(field);
                    if (StringUtils.isNotBlank(val)) {
                        row.put(field, Farm2HtmlUtils.removeXmlTag(val));
                    }
                }
            });
        }
        return this;
    }
}
