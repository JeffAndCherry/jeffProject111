package com.millery.spring_solr.test;

import com.millery.spring_solr.pojo.User;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Before;
import org.junit.Test;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by 孟亚运 on 2016/8/31.
 */
public class SpringSolrTest {
    private SpringSolr springSolr;

    @Before
    public void setUp() throws Exception {
        // 初始化Spring容器
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
                "applicationContext.xml", "applicationContext-solr.xml");

        //获取对象
        this.springSolr = applicationContext.getBean(SpringSolr.class);
    }

    @Test
    public void test() throws SolrServerException {
        //步骤一 添加用户对象到solr

        List<User> userList = new ArrayList<User>();

        for (Integer i = 0; i < 20; i++) {
            User u = new User();
            u.setId(i.toString());
            u.setUsername("name" + i);
            u.setDescription("描述" + i);
            u.setContent("内容" + i);
            userList.add(u);
        }

        //添加到solr
        this.springSolr.addUser(userList);

        //查询solr
        SolrQuery query = new SolrQuery();
        query.set("q", "内描5我都要 is good enounth");
        query.setHighlight(true);
        query.addHighlightField("content");// 高亮字段
        query.setHighlightSimplePre("<span style='color:red'>");// 标记
        query.setHighlightSimplePost("</span>");
        QueryResponse rsp = springSolr.Search(query);
        SolrDocumentList list = rsp.getResults();


        //取出高亮


        for (int i = 0; i < list.size(); i++) {

            SolrDocument sd = list.get(i);


            String id = (String) sd.getFieldValue("id");
            String description = (String) sd.getFieldValue("description");
            String content = (String) sd.getFieldValue("content");




            if (rsp.getHighlighting() != null) {

                if (rsp.getHighlighting().get(id) != null) {//先通过结果中的ID到高亮集合中取出文档高亮信息

                    Map<String, List<String>> map = rsp.getHighlighting().get(id);//取出高亮片段

                    if (map.get("content") != null) {

                        for (String s : map.get("content")) {

                            content = s;

                        }

                    }
                }
            }



            System.out.println(id + "-" + description + "-" + content);



        }
    }

}
