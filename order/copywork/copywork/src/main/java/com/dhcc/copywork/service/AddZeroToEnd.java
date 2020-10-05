package com.dhcc.copywork.service;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

public class AddZeroToEnd extends AbstractMethod {
    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        String sqlMethod="findAll";
        String sql="SELECT * FROM" + tableInfo.getTableName();

        SqlSource sqlSource=languageDriver.createSqlSource(configuration,sql,modelClass);
        return this.addSelectMappedStatement(mapperClass,sqlMethod,sqlSource,modelClass,tableInfo);
    }
}
