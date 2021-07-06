package me.zhengjie.modules.gen.utils;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author tuzhiyong
 * @version 1.0
 * @description: TODO
 * @date 2021/6/7 13:33
 */
@Slf4j
public class DBUtil {
    private DatabaseType databaseType = null;
    private String username;
    private String password;
    private String url;
    public static final String mysqldriver = "com.mysql.jdbc.Driver"; // mysql数据库的驱动类
    public static final String oracledriver = "oracle.jdbc.OracleDriver"; // oracles数据库的驱动类
    public static final String sql2005driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver"; // sqlserver数据库的驱动类
    public static final String sql2000driver = "net.sourceforge.jtds.jdbc.Driver"; // sqlserver数据库的驱动类

    public DBUtil(DatabaseType databaseType, String username,
                  String password,String url) {
        this.databaseType = databaseType;
        this.username = username;
        this.password = password;
        this.url = url;
        forName();
    }

    private void forName() {
        try {
            if (null == databaseType) {
                throw new RuntimeException("没有指定数据库类型");
            }
            if (databaseType == DatabaseType.MYSQL) {
                Class.forName(mysqldriver).newInstance();
            } else if (databaseType == DatabaseType.ORACLE) {
                Class.forName(oracledriver).newInstance();
            } else if (databaseType == DatabaseType.SQLSERVER2000) {
                Class.forName(sql2000driver).newInstance();
            } else if(databaseType == DatabaseType.SQLSERVER) {
                Class.forName(sql2005driver).newInstance();
            }
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            throw new RuntimeException("加载数据库驱动失败");
        }
    }

    /**
     * 如果程序发生异常，则表明无法连接
     * @return
     * @throws SQLException
     */
    public Connection testConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(
                url, username, password);// 获取连接对象
        return conn;
    }

    public Connection getConnection() {
        try {
            Connection conn = DriverManager.getConnection(
                    url, username, password);// 获取连接对象
            return conn;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean connIsOk(Connection conn) throws SQLException {
        if (null != conn && !conn.isClosed()) {
            return true;
        }
        return false;
    }

    public void closeConn(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
                conn = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public enum DatabaseType {
        MYSQL, ORACLE, SQLSERVER2000, SQLSERVER
    }
}
