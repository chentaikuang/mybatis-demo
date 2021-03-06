package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.service.SqlService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RestController
@RequestMapping("/test")
public class SqlController {

    @Autowired
    SqlService sqlService;

    //http://localhost:8888/test/querySql
    @RequestMapping("/querySql")
    public Map querySql(@RequestParam(value = "batch", defaultValue = "0") String batch) {

        Map map = new HashMap();
        String sqlScript = "";
        if ("1".equals(batch)) {
            sqlScript = "select * from xiaochen.t_user_info where id > 1";
            List<Map<String, Object>> list = sqlService.selectList(sqlScript);
            if (CollectionUtils.isEmpty(list)) {
                return Collections.emptyMap();
            }
            for (Map<String, Object> item : list) {
                System.out.println(JSONObject.toJSONString(item));
                map.put(item.get("id"), item);
            }
            return map;
        }

        sqlScript = "select id, name, age, remark from xiaochen.t_user_info where id = 1";
        sqlScript = "select id, name, age, remark from micromall.t_user_info where id = 1";
        sqlScript = "SELECT mt.* FROM micromall.t_user_info mt INNER join xiaochen.tb_user xt ON mt.id = xt.id WHERE mt.id = 2";
        sqlScript = "SELECT COUNT(1) totalCount FROM xiaochen.t_user_info WHERE age > 10";
        map = sqlService.selectOne(sqlScript);
        if (map == null || map.isEmpty()) {
            map = new HashMap();
            map.put("tips", "null");
            return map;
        }

        String targetFieldName = "age";
        String targetFieldVal = "10";
        if (map.containsKey(targetFieldName)) {
            boolean eq = map.get(targetFieldName).toString().equals(targetFieldVal);
            map.put("eq", eq);
        }
        Object[] values = map.values().toArray();
        Arrays.stream(values).forEach(val -> System.out.println(val.getClass().getTypeName()));

        Iterator<String> itr = map.keySet().iterator();
        while (itr.hasNext()) {
            String key = itr.next();
            System.out.println(key + "," + map.get(key) + "," + map.get(key).getClass().getTypeName());
        }
        return map;
    }

    //http://localhost:8888/test/modifySql
    @RequestMapping("/modifySql")
    public Map modifySql(@RequestParam(value = "name", defaultValue = "defaultName") String name) {
        String sqlScript = "INSERT INTO xiaochen.t_user_info(name,age,remark) VALUES ('micromall', '18', 'micromall')";
        int rst = 0;
        Map map = new HashMap();
        int maxId = getMaxId();
        if ("update".equals(name)) {
            name = name + new Random().nextInt(100);
            sqlScript = "UPDATE xiaochen.t_user_info SET name = '" + (name) + "' WHERE id = " + maxId;
            rst = sqlService.update(sqlScript);
        } else if ("delete".equals(name)) {
            sqlScript = "delete from xiaochen.t_user_info where id = " + maxId;
            rst = sqlService.delete(sqlScript);
        } else {
            rst = sqlService.insertOne(sqlScript);
            maxId = getMaxId();
        }
        map.put("maxId", maxId);
        map.put("name", name);
        map.put("rst", rst);
        return map;
    }

    //http://localhost:8888/test/mq
    @RequestMapping("/mq")
    public Map mq() {
        String sqlScript = "select id, name, age, remark from xiaochen.t_user_info where id = [userId] or name = [orderId]";
        if (sqlScript.indexOf("[") > 0 || sqlScript.indexOf("]") > 0) {
            Map<String, Object> params = new HashMap();
            params.put("[userId]", 1);
            params.put("[orderId]", "123456789");
            Set<Map.Entry<String, Object>> set = params.entrySet();
            for (Map.Entry<String, Object> entry : set) {
                if (entry.getValue() instanceof Integer) {
                    sqlScript = sqlScript.replace(entry.getKey(), entry.getValue().toString());
                } else {
                    sqlScript = sqlScript.replace(entry.getKey(), "'" + entry.getValue().toString() + "'");
                }
            }
        }
        checkSql(sqlScript);
        Map map = sqlService.selectOne(sqlScript);
        return map;
    }

    private void checkSql(String sqlScript) {
        log.info("sqlScript:{}", sqlScript);
        Assert.isTrue(sqlScript.indexOf("#") == -1, "参数缺或规则SQL配置有误");
    }

    private int getMaxId() {
        String sqlScript;
        sqlScript = "select max(id) maxId from xiaochen.t_user_info";
        Map map = sqlService.selectOne(sqlScript);
        int maxId = map.get("maxId") == null || !map.containsKey("maxId") ? 0 : Integer.parseInt(map.get("maxId").toString());
        System.out.println("maxId:" + maxId);
        return maxId;
    }

    //http://localhost:8888/test/batchInsert?count=1000000
    @RequestMapping("/batchInsert")
    public Map batchInsert(@RequestParam("count") Integer count) {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        long startTime = System.currentTimeMillis();
        while (count-- > 0) {
            String sqlScript = "INSERT INTO t_user_info(name,age,remark) VALUES ('"
                    + new Random().nextInt(888888)
                    + "', '" + new Random().nextInt(888888) + "', '"
                    + RandomStringUtils.randomAlphanumeric(5) + "');";
            sqlService.insertOne(sqlScript);
            atomicInteger.incrementAndGet();
        }
        Map map = new HashMap();
        map.put("time", (System.currentTimeMillis() - startTime) / 1000 + " s");
        map.put("rst", atomicInteger.get());
        return map;
    }


}