package testcasesDay2;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.alibaba.fastjson.JSONObject;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import testcasesDay3.pojo.ExcelPojo;

import java.io.File;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.BIG_DECIMAL;

/**
 * @Project: part2
 * @Author: 阿线现
 * @Create: 2021-04-01 16:17
 * @Desc：没有token,登录依赖的测试：注册
 * 封装了读取指定行，读取所有行的方法
 **/
public class LogonTest {

     @BeforeTest
     public void setup(){
         RestAssured.baseURI = "http://api.lemonban.com/futureloan";
         //前置条件，读取excel的第一条测试用例，保证这条数据是注册过的
         File file = new File("D:\\test\\api_testcases_futureloan_practice_v1.xls");
         List<ExcelPojo> listDatas = readSpecifyData(file, 1, 0, 1);
         String requestHeader = listDatas.get(0).getRequestHeader();
         Map<String,Object> requestHeaderMap = JSONObject.parseObject(requestHeader, Map.class);
         //执行接口请求

                        given().
                                    headers(requestHeaderMap).//MAP格式
                                    body(listDatas.get(0).getInputParams()).//JSON格式
                         when().
                                    post(listDatas.get(0).getUrl()).

                         then().
                                   log().all();

     }

    @Test(dataProvider = "getLogonDatas")
    public void testLogon(ExcelPojo excelPojo) {
        //RestAssured全局配置
        //json小数返回类型是BigDecimal
        RestAssured.config = RestAssured.config().jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAL));
        //BaseUrl全局配置
        RestAssured.baseURI = "http://api.lemonban.com/futureloan";
        //接口入参
        //String strJson03 = "{\"mobile_phone\": " + mobilePhone + ",\"pwd\": " + psw + "}";
        String inputParams = excelPojo.getInputParams();
        //接口地址
        String url = excelPojo.getUrl();

        //请求头
        String requestHeader = excelPojo.getRequestHeader();
        //json转换为MAP
        //Map requestsHeadersMap = (Map) JSON.parse(requestHeader);
        Map requestsHeadersMap = JSONObject.parseObject(requestHeader, Map.class);


        //期望的响应结果
        String expected = excelPojo.getExpected();
        //把响应结果转成map
        //Map<String,Object> expectedMap = (Map) JSON.parse(expected);
        Map<String,Object> expectedMap = JSONObject.parseObject(expected, Map.class);

        //1、循环变量响应map，取到里面每一个key（实际上就是我们设计的jsonPath表达式）
        //2、通过res.jsonPath.get(key)取到实际的结果，再跟期望的结果做对比（key对应的value）

        Response resLogonManager =
                given().
                        headers(requestsHeadersMap).//MAP格式
                        body(inputParams).//JSON格式
                        when().
                        post(url).

                        then().log().all()
                        .extract().response();


        for (String key : expectedMap.keySet()) {
            //获取期望结果MAP里面的Key
            System.out.println(key);
            //获取期望结果的value
            Object exceptValue = expectedMap.get(key);

            //获取接口返回的实际结果（jsonpath)，通过Key
            Object actualValue=resLogonManager.jsonPath().get(key);//通过Object接收期望结果
            //断言，实际结果和期望结果的数据类型必须一致
            Assert.assertEquals(actualValue,exceptValue);
        }
    }
     @DataProvider
    //easypoi实现读取整个sheet的数据。数据驱动，先新建一个实体类，写excel里面的字段，它是通过注解实现的
    public Object[] getLogonDatas() {

        File file = new File("D:\\test\\api_testcases_futureloan_practice_v1.xls");
        //直接使用封装的方法
         List<ExcelPojo> listDatas = readSpecifyData(file, 1, 1, 6);
        //把集合转换为一个一维数组
        return listDatas.toArray();
    }

    //easypoi实现读取excel里面第一个测试用例
//    public static void main(String[] args) {
//
//        File file = new File("D:\\test\\api_testcases_futureloan_practice_v1.xls");
//        //实例化ImportParams,来设置行数
//        ImportParams importParams = new ImportParams();
//        //读取第二个Sheet
//        importParams.setStartSheetIndex(1);
//        //设置读取的起始行
//        importParams.setStartRows(0);
//        //设置读取的总行数
//        importParams.setReadRows(1);
//        //读取Excel
//        List<ExcelPojo> listDatas = ExcelImportUtil.importExcel(file,ExcelPojo.class,importParams);
//        //for循环打印
//        for(ExcelPojo list:listDatas){
//            System.out.println(list);
//        }
//    }
       //对读取进行封装
       //读取excel指定sheet的所有数据，
       //FILE是文件对象， sheets是指定的页数编号
      public List<ExcelPojo> readAllData(File file,int sheetNumber){

          //导入的参数对象
          ImportParams importParams = new ImportParams();
          //读取第二个Sheet
          importParams.setStartSheetIndex(sheetNumber);
          //读取Excel
          List<ExcelPojo> listData01 = ExcelImportUtil.importExcel(file,ExcelPojo.class,importParams);
          return listData01;
      }
      //读取指定行的excel数据
      public List<ExcelPojo> readSpecifyData(File file,int sheetNumber,int startRow,int readRows){

          //导入的参数对象
          ImportParams importParams = new ImportParams();
          //读取第二个Sheet
          importParams.setStartSheetIndex(sheetNumber);
          //设置读取的起始行
          importParams.setStartRows(startRow);
          //设置读取的总行数
          importParams.setReadRows(readRows);
          //读取Excel
          List<ExcelPojo> listData02 = ExcelImportUtil.importExcel(file,ExcelPojo.class,importParams);
          return listData02;
      }

}
