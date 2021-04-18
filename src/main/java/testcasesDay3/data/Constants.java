package testcasesDay3.data;

import io.restassured.RestAssured;

/**
 * @Project: part2
 * @Author: 阿线现
 * @Create: 2021-04-04 21:52
 * @Desc： 常量类
 **/
public class Constants {
    //日志输出到控制台还是Allure报表中的控制
    public static final boolean LOG_TO_FILE=true;
    //把读取的路径地址写在一个路径下，用类名点的方式读取
    public static final String EXCEL_FILE_PATH="src/main/resources/api_testcases_futureloan_practice_v1.xls";
    //接口base_URL地址
    public static final String BASE_URL = "http://api.lemonban.com/futureloan";
    //数据库baseeul
    public static final String urlSQL = "api.lemonban.com/";
    //数据库名字
    public static final String DBName = "futureloan";
    //数据库登录名
    public static final String userNameSQL = "future";
    //数据库登录密码
    public static final String passwordSQL = "123456";



}
