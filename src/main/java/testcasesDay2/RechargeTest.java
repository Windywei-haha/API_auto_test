package testcasesDay2;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.alibaba.fastjson.JSONObject;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import testcasesDay3.pojo.ExcelPojo;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.restassured.RestAssured.given;
import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.BIG_DECIMAL;

/**
 * @Project: part2
 * @Author: 阿线现
 * @Create: 2021-04-02 17:46
 * @Desc：带有token的依赖测试
 * 封装了正则表达式读取字符串的方法
 **/
public class RechargeTest {
    String token;
    int memberID;

    @BeforeTest
    public void setup() {
        RestAssured.baseURI = "http://api.lemonban.com/futureloan";
        //前置条件，读取excel的第一条测试用例，保证这条数据是注册过的
        File file = new File("D:\\test\\api_testcases_futureloan_practice_v1.xls");

        List<ExcelPojo> listDatas = readSpecifyData(file, 3, 0, 2);
        for (int i = 0; i <= 1; i++) {
            String requestHeader = listDatas.get(i).getRequestHeader();
            Map<String, Object> requestHeaderMap = JSONObject.parseObject(requestHeader, Map.class);
            Response res =
                    given().
                            headers(requestHeaderMap).//MAP格式
                            body(listDatas.get(i).getInputParams()).//JSON格式
                            when().
                            post(listDatas.get(i).getUrl()).

                            then().
                            log().all().extract().response();
            memberID = res.jsonPath().get("data.id");
            token = res.jsonPath().get("data.token_info.token");

        }
        // String requestHeader = listDatas.get(0).getRequestHeader();
        //Map<String,Object> requestHeaderMap = JSONObject.parseObject(requestHeader, Map.class);
        //执行接口请求
    }

    @Test(dataProvider = "getRechargeDatas")
    public void testRecharge(ExcelPojo excelPojo) {
        //RestAssured全局配置
        //json小数返回类型是BigDecimal
        RestAssured.config = RestAssured.config().jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAL));
        //BaseUrl全局配置
        RestAssured.baseURI = "http://api.lemonban.com/futureloan";
        //接口入参
        //请求头
        String requestHeader = excelPojo.getRequestHeader();
        //json转换为MAP
        //Map requestsHeadersMap = (Map) JSON.parse(requestHeader);
        Map requestsHeadersMap = JSONObject.parseObject(requestHeader, Map.class);

        //把响应结果转成map
        //Map<String,Object> expectedMap = (Map) JSON.parse(expected);
        //Map<String,Object> expectedMap = JSONObject.parseObject(expected, Map.class);
        given().
                headers(requestsHeadersMap).//MAP格式
                body(excelPojo.getInputParams()).//JSON格式
                when().
                post(excelPojo.getUrl()).

                then().
                log().all();
    }

    @DataProvider
    //easypoi实现读取整个sheet的数据。数据驱动，先新建一个实体类，写excel里面的字段，它是通过注解实现的
    public Object[] getRechargeDatas() {

        File file = new File("D:\\test\\api_testcases_futureloan_practice_v1.xls");
        //直接使用封装的方法
        List<ExcelPojo> listDatas = readSpecifyData(file, 3, 2, 1);
        //把集合转换为一个一维数组
        return listDatas.toArray();
    }

    //对读取进行封装
    //读取excel指定sheet的所有数据，
    //FILE是文件对象， sheets是指定的页数编号
    public List<ExcelPojo> readAllData(File file, int sheetNumber) {

        //导入的参数对象
        ImportParams importParams = new ImportParams();
        //读取第二个Sheet
        importParams.setStartSheetIndex(sheetNumber);
        //读取Excel
        List<ExcelPojo> listData01 = ExcelImportUtil.importExcel(file, ExcelPojo.class, importParams);
        return listData01;
    }

    //读取指定行的excel数据
    public List<ExcelPojo> readSpecifyData(File file, int sheetNumber, int startRow, int readRows) {

        //导入的参数对象
        ImportParams importParams = new ImportParams();
        //读取第二个Sheet
        importParams.setStartSheetIndex(sheetNumber);
        //设置读取的起始行
        importParams.setStartRows(startRow);
        //设置读取的总行数
        importParams.setReadRows(readRows);
        //读取Excel
        List<ExcelPojo> listData02 = ExcelImportUtil.importExcel(file, ExcelPojo.class, importParams);
        return listData02;
    }

    //正则表达式  。匹配任意的单个字符    *连续多个    ？贪婪匹配
     public static void main(String[] args) {
//     String str = "AAABBB{{memberID}}11111";
//     String memberID = "99999";
//     //Pattern.compile正则表达式匹配器
//     Pattern pattern = Pattern.compile("\\{\\{(.*?)}}");
//     //matcher 去匹配哪个字符串，得到匹配对象
//     Matcher matcher = pattern.matcher(str);
//     //while循环
//     while (matcher.find()) {
//         //group0表示获取到整个匹配到的内容
//         String outStr = matcher.group(0);
//         //group1表示获取{{}}包裹的内容
//         String innerStr = matcher.group(1);
//         //replace
//         System.out.println(str.replace(outStr, memberID));
//     }
         System.out.println(regexReplace("AAAAA{{token}}BBBBB","11111"));
 }
     public static String regexReplace(String orgStr,String replaceStr){
        //orgStr:原始的字符串；replaceStr:需要替换的字符串

         //Pattern.compile正则表达式匹配器
         Pattern pattern1 = Pattern.compile("\\{\\{(.*?)}}");
         //matcher 去匹配哪个字符串，得到匹配对象
         Matcher matcher1 = pattern1.matcher(orgStr);
         //while循环
         String result=" ";//定义一个变量用来接收返回值

         while(matcher1.find()){
             //group0表示获取到整个匹配到的内容
             String outStr1 = matcher1.group(0);
             //group1表示获取{{}}包裹的内容
             String innerStr1 = matcher1.group(1);
             //replace
              result = orgStr.replace(outStr1,replaceStr);//while循环结束后返回
     }
           return result;

 }
}
