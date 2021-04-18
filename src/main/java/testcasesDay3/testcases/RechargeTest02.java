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

import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.BIG_DECIMAL;

/**
 * @Project: part2
 * @Author: 阿线现
 * @Create: 2021-04-04 23:25
 * @Desc： 封装了一下data.id, token....
 **/
public class RechargeTest02 extends BaseTest {
    String token;
    int memberID;

    @BeforeTest
    public void setup() {
        //前置条件，读取excel的前两条测试用例，保证这条数据是注册和登录过的

        List<ExcelPojo> listDatas = readSpecifyData( 2, 0, 2);
//        for (int i = 0; i <= 1; i++) {
//            String requestHeader = listDatas.get(i).getRequestHeader();
//            Map<String, Object> requestHeaderMap = JSONObject.parseObject(requestHeader, Map.class);
//            //发起注册和登录请求
//            Response res= request(listDatas.get(i));
//
//            memberID = res.jsonPath().get("data.id");
//            //存到环境变量中去
//            Environment.memberID=memberID;
//
//            token = res.jsonPath().get("data.token_info.token");
//            Environment.token=token;
//        }
        //发起注册请求
        Response resRecharge = request(listDatas.get(0),"Recharge");
        //发起单独的登录请求
        Response resLogon = request(listDatas.get(1),"Logon");
        //使用封装的方法

        extraToEnvironment(listDatas.get(1),resLogon);
        //获取memberID的值“data.id"
        //Object value = extractMap.get("member_id");
        //得到的值为jsonPath表达式里面的值
        //memberID = resLogon.jsonPath().get(value.toString());
    }

    @Test(dataProvider = "getRechargeDatas")
    public void testRecharge(ExcelPojo excelPojo) {
        RestAssured.config = RestAssured.config().jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAL));

        //在测试之前替换member_ID为环境变量中保存对应的值
        //String reallyBody=regexReplace(excelPojo.getInputParams(),Environment.envData.get("member_id")+"");//替换之后的值
        String reallyBody=regexReplace(excelPojo.getInputParams());

        excelPojo.setInputParams(reallyBody);//修改excelpojo里面的值为替换之后的值,用之前excelpojo的set方法重新赋值

        //直接用封装的方法请求数据
        request(excelPojo,"Recharge");
    }
    @DataProvider
    //easypoi实现读取整个sheet的数据。数据驱动，先新建一个实体类，写excel里面的字段，它是通过注解实现的
    public Object[] getRechargeDatas() {

        //直接使用封装的方法
        List<ExcelPojo> listDatas = readSpecifyData(2, 2, 1);
        //把集合转换为一个一维数组
        return listDatas.toArray();
    }
}
