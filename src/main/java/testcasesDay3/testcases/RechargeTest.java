package testcasesDay3.testcases;

import com.alibaba.fastjson.JSONObject;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import testcasesDay3.common.BaseTest;
import testcasesDay3.data.Environment;
import testcasesDay3.pojo.ExcelPojo;

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
 * 封装了发送请求数据的代码
 **/
public class RechargeTest extends BaseTest {
    String token;
    int memberID;

    @BeforeTest
    public void setup() {
        //前置条件，读取excel的前两条测试用例，保证这条数据是注册和登录过的

        List<ExcelPojo> listDatas = readSpecifyData( 2, 0, 2);
        for (int i = 0; i <= 1; i++) {
            String requestHeader = listDatas.get(i).getRequestHeader();
            Map<String, Object> requestHeaderMap = JSONObject.parseObject(requestHeader, Map.class);
            //发起注册和登录请求
            Response res= request(listDatas.get(i),"充值模块");

            memberID = res.jsonPath().get("data.id");
            //存到环境变量中去
            //Env/ironment.memberID=memberID;

            token = res.jsonPath().get("data.token_info.token");
           // Environment.token=token;
        }
    }

    @Test(dataProvider = "getRechargeDatas")
    public void testRecharge(ExcelPojo excelPojo) {
        RestAssured.config = RestAssured.config().jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAL));

    //在测试之前替换member_ID为环境变量中保存对应的值
        //暂时注释
      //String reallyBody=regexReplace(excelPojo.getInputParams(), Environment.memberID+"");//替换之后的值
       // excelPojo.setInputParams(reallyBody);//修改excelpojo里面的值为替换之后的值,用之前excelpojo的set方法重新赋值

        //直接用封装的方法请求数据
         request(excelPojo,"充值模块");
    }

    @DataProvider
    //easypoi实现读取整个sheet的数据。数据驱动，先新建一个实体类，写excel里面的字段，它是通过注解实现的
    public Object[] getRechargeDatas() {

        //直接使用封装的方法
        List<ExcelPojo> listDatas = readSpecifyData(2, 2, 1);
        //把集合转换为一个一维数组
        return listDatas.toArray();
    }



     public static void main(String[] args) {
         System.out.println(regexReplace("AAAAA{{token}}BBBBB{{memebrID}}CCCCCC","11111"));
 }
     public static String regexReplace(String orgStr,String replaceStr){
        //orgStr:原始的字符串；replaceStr:需要替换的字符串

         //Pattern.compile正则表达式匹配器
         Pattern pattern1 = Pattern.compile("\\{\\{(.*?)}}");
         //matcher 去匹配哪个字符串，得到匹配对象
         Matcher matcher1 = pattern1.matcher(orgStr);
         //while循环
         String result=orgStr;//定义一个变量用来接收返回值,有多个需要替换的值

         while(matcher1.find()){
             //group0表示获取到整个匹配到的内容
             String outStr1 = matcher1.group(0);
             //group1表示获取{{}}包裹的内容
             String innerStr1 = matcher1.group(1);
             //replace
              result = result.replace(outStr1,replaceStr);//while循环结束后返回
     }
             return result;

 }
     //封装了请求方法
    //excelpojo保存了一条用例需要的所有数据
    //返回值就是接口响应的结果
//     public Response request(ExcelPojo excelPojo){
//         RestAssured.baseURI = "http://api.lemonban.com/futureloan";
//        //请求的url
//        String url= excelPojo.getUrl();
//        //请求方法
//         String method=excelPojo.getMethod();
//         //请求头,并转换为MAP
//         String headers=excelPojo.getRequestHeader();
//         Map<String,Object> headersMap=JSONObject.parseObject(headers,Map.class);
//
//         //请求参数
//         String params=excelPojo.getInputParams();
//         //用if来判断请求,先声明一个空的变量res
//         Response res=null;
//         if("get".equalsIgnoreCase(method)){
//              res= given().headers(headersMap).when().get(url).then().log().all().extract().response();
//         }
//         else if("post".equalsIgnoreCase(method)){
//              res= given().headers(headersMap).body(params).when().post(url).then().log().all().extract().response();
//
//         }
//         else if ("patch".equalsIgnoreCase(method)){
//             res= given().headers(headersMap).body(params).when().patch(url).then().log().all().extract().response();
//
//         } else if ("put".equalsIgnoreCase(method)) {
//             res= given().headers(headersMap).body(params).when().put(url).then().log().all().extract().response();
//
//         }
//         return res;
//     }
}
