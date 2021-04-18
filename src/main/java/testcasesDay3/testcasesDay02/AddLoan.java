package testcasesDay3.testcasesDay02;

import com.alibaba.fastjson.JSONObject;
import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import testcasesDay3.common.BaseTest;
import testcasesDay3.data.Environment;
import testcasesDay3.pojo.ExcelPojo;
import testcasesDay3.utils.PhoneRadomUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;

import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.BIG_DECIMAL;

/**
 * @Project: part2
 * @Author: 阿线现
 * @Create: 2021-04-10 16:06
 * @Desc： 新增项目的单接口测试
 **/
public class AddLoan extends BaseTest {
    @BeforeClass
    public void setup(){
        //读取excelpojo
        List<ExcelPojo> listDatas = readSpecifyData(4, 0, 2);
        //生成随机的手机号
        String phone = PhoneRadomUtils.getUnregPhone();
        //把手机号保存至环境变量中取
        Environment.envData.put("admin_phone",phone);
        for(int i=0;i<listDatas.size();i++){
            //获取excelpojo
            ExcelPojo excelPojo = listDatas.get(i);
            //在请求之前替换数据
            excelPojo = replace(excelPojo);
            //发送请求
            Response request = request(excelPojo,"创建项目模块");
            if(excelPojo.getExtract()!=null){
                extraToEnvironment(excelPojo,request);
            }
        }
    }
    @Test(dataProvider ="getAddLoanDatas")
    public void addLoan(ExcelPojo excelPojo) throws FileNotFoundException {

        //在请求之前先替换数据
        excelPojo= replace(excelPojo);
        //发送请求
        Response res = request(excelPojo,"创建项目模块");

        //数据库断言之前再替换loan_id,这个值必须是把请求发完才能得到
        //响应断言的判断
        assertResponse(excelPojo,res);

        //数据库断言
        assertSQL(excelPojo );


    }
    @DataProvider
    public Object[] getAddLoanDatas() {
        List<ExcelPojo> listDatas = readSpecifyData(4, 2);
        return listDatas.toArray();
    }


}
