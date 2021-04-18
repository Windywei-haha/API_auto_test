package day2;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.alibaba.fastjson.JSONObject;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.BIG_DECIMAL;

/**
 * @Project: part2
 * @Author: 阿线现
 * @Create: 2021-03-30 22:11
 * @Desc： //数据驱动的断言怎么处理
 *   1、循环变量响应map，取到里面每一个key（实际上就是我们设计的jsonPath表达式）
 * //2、通过res.jsonPath.get(key)取到实际的结果，再跟期望的结果做对比（key对应的value）
 **/
public class DataProviderResponseAssert {

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
           // System.out.println(key);
            //获取期望结果的value
            Object exceptValue = expectedMap.get(key);

            //获取接口返回的实际结果（jsonpath)，通过Key
            Object actualValue=resLogonManager.jsonPath().get(key);//通过Object接收期望结果
            //断言，实际结果和期望结果的数据类型必须一致
            Assert.assertEquals(actualValue,exceptValue);
        }
    }


    @DataProvider
// 二维数组，数据少的情况
//        public Object[][] getLogonDatas(){
//        Object[][] datas={{"13319130988","12345678"},
//                {"18390129011","12345678"}
//        };
//        return datas;
      //easypoi实现数据驱动，先新建一个实体类，写excel里面的字段，它是通过注解实现的
    public Object[] getLogonDatas() {

            File file = new File("D:\\test\\api_testcases_futureloan_practice.xls");
            //导入的参数对象
            ImportParams importParams = new ImportParams();
            importParams.setStartSheetIndex(1);
            //读取Excel
            List<ExcelPojo> listDatas = ExcelImportUtil.importExcel(file,ExcelPojo.class,importParams);
            //把集合转换为一个一维数组
            return listDatas.toArray();

    }

//    public static void main(String[] args) {
//        //创建第一个字段file
//        File file=new File("D:\\test\\api_testcases_futureloan_practice.xls");
//        //导入参数对象，创建第三个字段
//        ImportParams importParams=new ImportParams();
//        importParams.setStartSheetIndex(1);
//
//        //读取excel
//        List<Object> listDatas = ExcelImportUtil.importExcel(file, ExcelPojo.class, importParams);
//        for(Object object:listDatas){
//            System.out.println(object);
//        }


}
