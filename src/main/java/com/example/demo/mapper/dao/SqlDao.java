package com.example.demo.mapper.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SqlDao {

    Map<String, Object> selectOne(@Param("sqlScript") String sqlScript);

    List<Map<String, Object>> selectList(@Param("sqlScript") String sqlScript);

    int insert(@Param("sqlScript") String sqlScript);

    int update(@Param("sqlScript") String sqlScript);

    int delete(@Param("sqlScript") String sqlScript);


}