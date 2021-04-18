package testcasesDay3.testcasesDay02;

import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import testcasesDay3.common.BaseTest;
import testcasesDay3.data.Environment;
import testcasesDay3.pojo.ExcelPojo;
import testcasesDay3.utils.PhoneRadomUtils;

import java.util.List;

import static testcasesDay3.common.BaseTest.request;

/**
 * @Project: part2
 * @Author: 阿线现
 * @Create: 2021-04-13 12:12
 * @Desc： 数据库断言
 **/
public class Register extends BaseTest {
    @BeforeClass
    public void setup(){

        //生成随机的手机号
        String phone1 = PhoneRadomUtils.getUnregPhone();
        String phone2 = PhoneRadomUtils.getUnregPhone();
        String phone3 = PhoneRadomUtils.getUnregPhone();

        //把手机号保存至环境变量中
        Environment.envData.put("phone1",phone1);
        Environment.envData.put("phone2",phone2);
        Environment.envData.put("phone3",phone3);
        }
    @Test(dataProvider ="getAddLoanDatas")
    public void addLoan(ExcelPojo excelPojo){

        //在请求之前先替换数据
        excelPojo=replace(excelPojo);
        //发送请求
        Response res = request(excelPojo,"注册模块");
        //响应断言
        assertResponse(excelPojo,res);
        //数据库断言
        assertSQL(excelPojo);

    }
    @DataProvider
    public Object[] getAddLoanDatas() {
        List<ExcelPojo> listDatas = readSpecifyData(0, 0);
        return listDatas.toArray();
    }

}
