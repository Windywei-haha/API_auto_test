package testcasesDay3.utils;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import testcasesDay3.data.Constants;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @Project: part2
 * @Author: 阿线现
 * @Create: 2021-04-10 09:31
 * @Desc： JDBC工具类,,dbutils工具类是连接数据库的工具类
 * 具体的操作：1.连接数据库
 * 2.写数据库的操作语句，update,query
 **/
public class JDBCUtils {
    public static Connection getConnection() {
 //定义数据库连接
        //String url = "jdbc:mysql://api.lemonban.com/futureloan? useUnicode=true&characterEncoding=utf-8";
 //Oracle：jdbc:oracle:thin:@localhost:1521:DBName
 //SqlServer：jdbc:microsoft:sqlserver://localhost:1433; DatabaseName=DBName
 //MySql：jdbc:mysql://localhost:3306/DBName；数据库的名字

        String url = "jdbc:mysql://"+Constants.urlSQL+Constants.DBName+"? useUnicode=true&characterEncoding=utf-8";
        String user = Constants.userNameSQL;
        String password = Constants.passwordSQL;
  //定义数据库连接对象
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
    public static void main(String[] args) {
        //建立数据库连接
        Connection connection=getConnection();
        //实例化Querryrunner
        QueryRunner queryRunner=new QueryRunner();
        //新增的sql语句
        //String add_sql= "insert into member value(20223,'阿现','25D55AD283AA400AF464C76D713C07AD','13319127650',1,1000.00,'2021-01-28 15:24:20')";
        //修改的sql语句
        String update_sql="update member set reg_name='小柠'   where id='20223'";
        //删除的sql语句----没有权限
        //String delete_sql="delete FROM member where id=1;"

        //对数据库进行update的操作操作，没有返回值
//        try {
//            queryRunner.update(connection,update_sql);
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
        //查询的sql语句
//        String search_sql="SELECT * FROM member WHERE id=1";
//        //对数据库进行query的操作,这个是有返回值的
//        try {
//            Map<String, Object> query = queryRunner.query(connection, search_sql, new MapHandler());
//            System.out.println(query);
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
        //查询的sql语句
//        String search_sql="SELECT * FROM member WHERE id<10";
//        //对数据库进行query的操作,这个是有返回值的
//        try {
//            List<Map<String, Object>> list_query = queryRunner.query(connection, search_sql, new MapListHandler());
//            System.out.println(list_query);
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
        //查询的sql语句
        String search_sql="select count(*) from member where id<10";
        //对数据库进行query的操作,这个是有返回值的
        try {
            Long long_query = queryRunner.query(connection, search_sql,new ScalarHandler<Long>());
            System.out.println(long_query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        finally {
            closeDB(connection);
        }
    }
   //封装数据库的更新操作（包括增加，修改，删除）
    public static void updateMethod(String sql){
        //建立数据库连接
        Connection connection=getConnection();
        //实例化Querryrunner
        QueryRunner queryRunner=new QueryRunner();
        //对数据库进行update的操作操作，没有返回值
        try {
            queryRunner.update(connection,sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        finally {
            closeDB(connection);
        }
    }
     //封装数据库的查询操作（三种方式）
    //查询多条数据LIst集合
    public static List<Map<String, Object>> queryAll(String sql){
        //建立数据库连接
        Connection connection=getConnection();
        //实例化Querryrunner
        QueryRunner queryRunner=new QueryRunner();
        //对数据库进行query的操作操作，有返回值
        List<Map<String, Object>> result=null;
        try {
            result = queryRunner.query(connection, sql, new MapListHandler());

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        finally {
            closeDB(connection);
        }
        return result;
    }
  //查询一条Map数据
    public static Map<String, Object> queryOne(String sql){

        Connection connection=getConnection();

        QueryRunner queryRunner=new QueryRunner();

        Map<String, Object> result=null;
        try {
            result = queryRunner.query(connection, sql, new MapHandler());

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        finally {
            closeDB(connection);
        }
        return result;
    }
  //查询单条数据
    public static Object querySingleData(String sql){

        Connection connection=getConnection();
        QueryRunner queryRunner=new QueryRunner();
        Object result=null;
        try {
            result = queryRunner.query(connection, sql, new ScalarHandler<Object>());

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        finally {
            closeDB(connection);
        }
        return result;
    }
    //关闭数据库连接
     public static void closeDB(Connection connection){
        if (connection!=null){
            try {
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

     }
    }