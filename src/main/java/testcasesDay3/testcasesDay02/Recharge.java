package testcasesDay3.testcasesDay02;
import com.alibaba.fastjson.JSONObject;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import testcasesDay3.common.BaseTest;
import testcasesDay3.data.Environment;
import testcasesDay3.pojo.ExcelPojo;
import testcasesDay3.utils.PhoneRadomUtils;

import java.util.List;
import java.util.Map;

import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.BIG_DECIMAL;

/**
 * @Project: part2
 * @Author: 阿线现
 * @Create: 2021-04-04 23:25
 * @Desc： 不用手动修改手机号，把手机号保存到环境变量中去
 **/
public class Recharge extends BaseTest {
        @BeforeClass
        public void setup() {
        List<ExcelPojo> listDatas = readSpecifyData( 2, 0, 2);
            ExcelPojo excelPojo = listDatas.get(0);
        //获取数据库不存在的手机号
            String phone = PhoneRadomUtils.getUnregPhone();
            //把获取的手机号存在环境变量中
            Environment.envData.put("phone",phone);
            //正则替换手机号

            //把替换的手机号保存在excelpojo里面
              excelPojo= replace(excelPojo);

            //发起注册请求
        Response resRecharge = request(excelPojo,"充值模块");
        //数据库断言
            assertSQL(excelPojo);
          //把需要替换的数据保存在环境变量中
        extraToEnvironment(listDatas.get(0),resRecharge);

        //参数替换
        replace(listDatas.get(1));

        //发起登录请求
        Response resLogon = request(listDatas.get(1),"充值模块");
        //使用封装的方法
        extraToEnvironment(listDatas.get(1),resLogon);
    }

    @Test(dataProvider = "getRechargeDatas")
    public void testRecharge(ExcelPojo excelPojo) {

        //在测试之前替换member_ID为环境变量中保存对应的值

        excelPojo= replace(excelPojo);
       //发起请求
        Response res=request(excelPojo,"充值模块");
        //响应断言的判断
        assertResponse(excelPojo,res);
        //数据库断言
        assertSQL(excelPojo);
    }
    @DataProvider
    public Object[] getRechargeDatas() {
        List<ExcelPojo> listDatas = readSpecifyData(2, 2);
        return listDatas.toArray();
    }
}
