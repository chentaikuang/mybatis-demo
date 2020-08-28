package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.service.SqlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

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
            sqlScript = "select * from xiaochen.t_user_info where id = 1";
            List<Map<String,Object>> list = sqlService.selectList(sqlScript);
            if (CollectionUtils.isEmpty(list)){
                return Collections.emptyMap();
            }
            for (Map<String, Object> item : list) {
                System.out.println(JSONObject.toJSONString(item));
                map.put(item.get("id"),item);
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


}