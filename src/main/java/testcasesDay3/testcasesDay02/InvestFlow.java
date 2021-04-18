package testcasesDay3.testcasesDay02;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import testcasesDay3.common.BaseTest;
import testcasesDay3.data.Environment;
import testcasesDay3.pojo.ExcelPojo;
import testcasesDay3.utils.PhoneRadomUtils;

import java.util.List;

import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.BIG_DECIMAL;

/**
 * @Project: part2
 * @Author: 阿线现
 * @Create: 2021-04-09 12:56
 * @Desc： 不用手动修改手机号，把手机号保存到环境变量中去
 **/
public class InvestFlow extends BaseTest {
    @BeforeClass
    public void setup(){

        //读取测试用例从第一条到第九条
        List<ExcelPojo> list = readSpecifyData(3, 0, 9);
        //获取三种形式账号的注册的唯一手机号码
        String adminphone= PhoneRadomUtils.getUnregPhone();
        String investphone= PhoneRadomUtils.getUnregPhone();
        String borrowphone= PhoneRadomUtils.getUnregPhone();
        //把手机号保存至环境变量中
        Environment.envData.put("invest_phone",investphone);
        Environment.envData.put("borrow_phone",borrowphone);
        Environment.envData.put("admin_phone",adminphone);
        //正则替换，因为for语句里面有替换的方法，所以就不用写了


        for(int i=0;i<list.size();i++){
            //发送请求
            ExcelPojo excelPojo = list.get(i);
            //在请求之前替换数据,正则替换环境变量中的数据
            excelPojo = replace(excelPojo);
            //发起请求
            Response res = request(excelPojo,"投资模块");
            //判断提取返回数据不为空则保存在环境变量中
            if(excelPojo.getExtract()!=null){

                extraToEnvironment(excelPojo,res);
            }
        }
    }
    @Test
    public void investFlow(){

        List<ExcelPojo> list = readSpecifyData(3, 9);
        ExcelPojo excelPojo = list.get(0);
        excelPojo=replace(excelPojo);

        //发送请求数据
        Response res=request(excelPojo,"投资流程");
        //响应断言
        assertResponse(excelPojo,res);
        //数据库断言
        assertSQL(excelPojo);

    }
}
