package AES_1;

import org.apache.commons.dbutils.QueryRunner;

import java.sql.Connection;

/**
 * @author LYHstart
 * @create 2021-10-08 17:00
 *
 * 基础查询类    ->  有DBUtils之后是否需要??
 */
public abstract class BaseDAO
{
    public void update(Connection connection,String sql,Object ... args)
    {

    }
}
