package org.voroniuk.prokat.connectionpool;

import org.apache.log4j.Logger;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

/**
 * Class for getting database connection
 *
 * @author M. Voroniuk
 *
 */

public class DBManager {

    private static final Logger LOG = Logger.getLogger(DBManager.class);

    private static DBManager dbManager;
    private String conUrl;

    DataSource dataSource;
    private String user;
    private String password;

    private DBManager() {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(new File(this.getClass().getClassLoader().getResource("/").getPath() + "app.properties")));
            conUrl = properties.getProperty("connection.url");
            user = properties.getProperty("user");
            password = properties.getProperty("password");
            dataSource = getDataSource();


        } catch (IOException e) {
            LOG.error(e.getMessage());
        }
    }

    public static DBManager getInstance() {
        if (dbManager == null) {
            dbManager = new DBManager();
        }
        return dbManager;
    }

//    public Connection getConnectionFromDriverManager() throws SQLException {
//        return DriverManager.getConnection(conUrl);
//    }

    /**
     *
     * Configure TomCat connection pool
     *
     * @return configured DataSource
     */
    public DataSource getDataSource(){

        PoolProperties p = new PoolProperties();
        p.setUrl(conUrl);
        p.setDriverClassName("com.mysql.cj.jdbc.Driver");
        p.setUsername(user);
        p.setPassword(password);
        p.setJmxEnabled(true);
        p.setTestWhileIdle(false);
        p.setTestOnBorrow(true);
        p.setValidationQuery("SELECT 1");
        p.setTestOnReturn(false);
        p.setValidationInterval(30000);
        p.setTimeBetweenEvictionRunsMillis(30000);
        p.setMaxActive(1000);
        p.setInitialSize(10);
        p.setMaxWait(10000);
        p.setRemoveAbandonedTimeout(60);
        p.setMinEvictableIdleTimeMillis(30000);
        p.setMinIdle(10);
        p.setLogAbandoned(true);
        p.setRemoveAbandoned(true);
        p.setJdbcInterceptors(
                "org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;" +
                        "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
        DataSource ds = new DataSource();
        ds.setPoolProperties(p);
        return ds;
    }

    public Connection getConnection() throws SQLException {

        Connection connection = dataSource.getConnection();
        return connection;
    }
}



