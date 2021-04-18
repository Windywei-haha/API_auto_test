package testcasesDay3.testcases;

import com.alibaba.fastjson.JSONObject;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import testcasesDay3.common.BaseTest;
import testcasesDay3.pojo.ExcelPojo;

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
public class LogonTest extends BaseTest {

     @BeforeTest
     public void setup(){
         //前置条件，读取excel的第一条测试用例，保证这条数据是注册过的

         List<ExcelPojo> listDatas = readSpecifyData(1, 0, 1);
         //执行接口请求
       request(listDatas.get(0),"Logon");

     }
    @Test(dataProvider = "getLogonDatas")
    public void testLogon(ExcelPojo excelPojo) {
        //json小数返回类型是BigDecimal
        RestAssured.config = RestAssured.config().jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAL));
        //把期望结果转为MAP
        String expected = excelPojo.getExpected();
        Map<String,Object> expectedMap=JSONObject.parseObject(expected,Map.class);

        Response res=request(excelPojo,"Logon");

        for (String key : expectedMap.keySet()) {
            //获取期望结果MAP里面的Key
            System.out.println(key);
            //获取期望结果的value
            Object exceptValue = expectedMap.get(key);

            //获取接口返回的实际结果（jsonpath)，通过Key
            Object actualValue=res.jsonPath().get(key);//通过Object接收期望结果
            //断言，实际结果和期望结果的数据类型必须一致
            Assert.assertEquals(actualValue,exceptValue);
        }

    }
     @DataProvider
    //easypoi实现读取整个sheet的数据。数据驱动，先新建一个实体类，写excel里面的字段，它是通过注解实现的
    public Object[] getLogonDatas() {

        //直接使用封装的方法
         List<ExcelPojo> listDatas = readSpecifyData(1, 1);
        //把集合转换为一个一维数组
        return listDatas.toArray();
    }

}
