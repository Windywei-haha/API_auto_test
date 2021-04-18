package testcasesDay3.testcases;

import com.alibaba.fastjson.JSONObject;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import testcasesDay3.common.BaseTest;
import testcasesDay3.pojo.ExcelPojo;

import java.util.List;
import java.util.Map;

import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.BIG_DECIMAL;

/**
 * @Project: part2
 * @Author: 阿线现
 * @Create: 2021-04-04 23:25
 * @Desc： 封装了正则取数值的表达式
 **/
public class RechargeTest03 extends BaseTest {

    @BeforeTest
    public void setup() {
        RestAssured.config = RestAssured.config().jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAL));
        List<ExcelPojo> listDatas = readSpecifyData( 2, 0, 2);
        //发起注册请求
        Response resRecharge = request(listDatas.get(0),"Recharge03");

        extraToEnvironment(listDatas.get(0),resRecharge);
        //参数替换
        replace(listDatas.get(1));

        //发起登录请求

        Response resLogon = request(listDatas.get(1),"Recharge03");
        //使用封装的方法
        extraToEnvironment(listDatas.get(1),resLogon);

    }

    @Test(dataProvider = "getRechargeDatas")
    public void testRecharge(ExcelPojo excelPojo) {
        RestAssured.config = RestAssured.config().jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAL));
        //在测试之前替换member_ID为环境变量中保存对应的值

        excelPojo= replace(excelPojo);

        Response res=request(excelPojo,"Recharge03");
        //响应断言的判断
        String expected = excelPojo.getExpected();
        Map<String,Object> exceptMap= JSONObject.parseObject(expected,Map.class);
        for(String key:exceptMap.keySet()){
            Object except=exceptMap.get(key);
            Object actual = res.jsonPath().get(key);
            Assert.assertEquals(actual,except);
        }
    }
    @DataProvider
    public Object[] getRechargeDatas() {
        List<ExcelPojo> listDatas = readSpecifyData(2, 2);
        return listDatas.toArray();
    }
}
