package com.example.demo.service;

import com.example.demo.mapper.dao.SqlDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service(value = "sqlService")
public class SqlService {

    @Resource
    private SqlDao sqlDao;

    public Map executeSql(String sqlCommand){
        Map resultMap = sqlDao.executeSql(sqlCommand);
        return resultMap;
    }
}
