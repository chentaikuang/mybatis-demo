package com.example.demo.mapper.dao;

import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface SqlDao {

    Map<String, Object> executeSql(@Param("sqlCommand") String sqlCommand);
}