package com.example.demo.service;

import com.example.demo.mapper.dao.SqlDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service(value = "sqlService")
public class SqlService {

    @Autowired
    private SqlDao sqlDao;

    public Map selectOne(String sqlScript) {
        Map resultMap = sqlDao.selectOne(sqlScript);
        return resultMap;
    }

    public int insertOne(String sqlScript) {
        return sqlDao.insert(sqlScript);
    }

    public int delete(String sqlScript) {
        return sqlDao.delete(sqlScript);
    }

    public int update(String sqlScript) {
        return sqlDao.update(sqlScript);
    }

    public List selectList(String sqlScript) {
        return sqlDao.selectList(sqlScript);
    }

}
