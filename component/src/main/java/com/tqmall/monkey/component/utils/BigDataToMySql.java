package com.tqmall.monkey.component.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


/**
 * 大文件存储
 * Created by zxg on 15/8/28.
 */
@Slf4j
@Component
public class BigDataToMySql {
    private Connection conn = null;


    @Value(value = "${jdbc.driverClassName}")
    protected  String driver ;
    @Value(value = "${jdbc.url}")
    protected  String url ;
    @Value(value = "${jdbc.username}")
    protected  String username ;
    @Value(value = "${jdbc.password}")
    protected  String password ;




    /**
     *
     * load bulk data from InputStream to MySQL
     * String testSql = "LOAD DATA LOCAL INFILE 'sql.csv' IGNORE INTO TABLE test.test (a,b,c,d,e,f)";
     InputStream dataStream = getTestDataInputStream();
     */
    public int bulkLoadFromInputStream(String loadDataSql,
                                       InputStream dataStream) throws SQLException, ClassNotFoundException {
        if(dataStream==null){
            log.info("InputStream is null ,No data is imported");
            return 0;
        }

        Class.forName(driver);
        conn = DriverManager.getConnection(url, username, password);
//        conn = jdbcTemplate.getDataSource().getConnection();
        PreparedStatement statement = conn.prepareStatement(loadDataSql);

        int result = 0;

        if (statement.isWrapperFor(com.mysql.jdbc.Statement.class)) {

            com.mysql.jdbc.PreparedStatement mysqlStatement = statement
                    .unwrap(com.mysql.jdbc.PreparedStatement.class);

            mysqlStatement.setLocalInfileInputStream(dataStream);
            result = mysqlStatement.executeUpdate();
        }
        return result;
    }
}
