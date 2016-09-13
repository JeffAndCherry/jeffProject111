package com.millery.spring_solr.test;

import com.millery.spring_solr.pojo.User;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created 孟亚运 hp on 2016/8/31.
 */
@Service("articleService")
public class SpringSolr {
    @Autowired
    private HttpSolrServer httpSolrServer;

    public User getUser(Long id) throws SolrServerException {

        //创建查询条件
        SolrQuery query = new SolrQuery();
        query.setQuery("id:" + id);

        //查询并返回结果
        QueryResponse queryResponse = this.httpSolrServer.query(query);
        System.out.println(queryResponse);
        return null;
    }

    public void addUser(List<User> uList) throws SolrServerException{
        Collection<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
        SolrInputDocument doc = null;
        for(User u:uList){
            doc = new SolrInputDocument();
            doc.addField("id", u.getId());
            doc.addField("name",u.getUsername());
            doc.addField("description",u.getDescription());
            doc.addField("content",u.getContent());
            docs.add(doc);
        }

        try {
            UpdateResponse updateResponse = httpSolrServer.add(docs);
            httpSolrServer.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public  QueryResponse Search(String[] field, String[] key, int start,
                                       int count, String[] sortfield, Boolean[] flag, Boolean hightlight) {
        //检测输入是否合法
        if (null == field || null == key || field.length != key.length) {
            return null;
        }
        if (null == sortfield || null == flag || sortfield.length != flag.length) {
            return null;
        }

        SolrQuery query = null;
        try {
            //初始化查询对象
            query = new SolrQuery(field[0] + ":" + key[0]);
            for (int i = 0; i < field.length; i++) {
                query.addFilterQuery(field[i] + ":" + key[i]);
            }
            //设置起始位置与返回结果数
            query.setStart(start);
            query.setRows(count);
            //设置排序
            for(int i=0; i<sortfield.length; i++){
                if (flag[i]) {
                    query.addSortField(sortfield[i], SolrQuery.ORDER.asc);
                } else {
                    query.addSortField(sortfield[i], SolrQuery.ORDER.desc);
                }
            }
            //设置高亮
            if (null != hightlight) {
                query.setHighlight(true); // 开启高亮组件
                query.addHighlightField("title");// 高亮字段
                query.setHighlightSimplePre("<font color='red'>");// 标记
                query.setHighlightSimplePost("</font>");
                query.setHighlightSnippets(1);//结果分片数，默认为1
                query.setHighlightFragsize(1000);//每个分片的最大长度，默认为100
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        QueryResponse rsp = null;
        try {
            rsp = httpSolrServer.query(query);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        //返回查询结果
        return rsp;
    }

    public  QueryResponse Search(SolrQuery query){
        QueryResponse rsp = null;
        //返回查询结果
        try {
            rsp =  httpSolrServer.query(query);
        } catch (SolrServerException e) {
            e.printStackTrace();
        }
        return rsp;
    }


}
