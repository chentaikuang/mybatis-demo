package com.example.demo.controller;

import com.example.demo.service.SqlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class SqlController {

    @Autowired
    SqlService sqlService;

    //http://localhost:8888/test/sql
    @RequestMapping("/sql")
    public Map sql() {

        String sqlScript = "select id, name, age, remark from t_user_info where id = 1";
        sqlScript = "select id, name, age, remark from micromall.t_user_info where id = 1";
        sqlScript = "SELECT mt.* FROM micromall.t_user_info mt INNER join xiaochen.tb_user xt ON mt.id = xt.id WHERE mt.id = 2";
        Map map = sqlService.executeSql(sqlScript);
        if (map == null) {
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