package AES_1;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.SortedMap;

/**
 * @author LYHstart
 * @create 2021-10-08 16:46
 *
 * 针对数据库连接的工具类
 */
public class JDBCUtils
{
    private static DataSource source;

    static {
        try
        {
            Properties pro = new Properties();
            FileInputStream fis = new FileInputStream("F:\\Java\\AES\\src\\AES_1\\DruidFile.properties");
            pro.load(fis);
            source = DruidDataSourceFactory.createDataSource(pro);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static Connection getConnection()
    {
        try
        {
            Connection connection = source.getConnection();
            return connection;
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }


}
