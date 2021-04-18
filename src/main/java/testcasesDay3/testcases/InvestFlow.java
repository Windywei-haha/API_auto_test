package testcasesDay3.testcases;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import testcasesDay3.common.BaseTest;
import testcasesDay3.pojo.ExcelPojo;

import java.util.List;

import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.BIG_DECIMAL;

/**
 * @Project: part2
 * @Author: 阿线现
 * @Create: 2021-04-09 12:56
 * @Desc： 整个流程
 **/
public class InvestFlow extends BaseTest {
    @BeforeTest
    public void setup(){
        RestAssured.config = RestAssured.config().jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAL));
        //读取测试用例从第一条到第九条
        List<ExcelPojo> list = readSpecifyData(3, 0, 9);

        for(int i=0;i<list.size();i++){
            //发送请求
            ExcelPojo excelPojo = list.get(i);
            //在请求之前替换数据
            excelPojo = replace(excelPojo);

            Response res = request(excelPojo,"Invest");
            //判断提取返回数据不为空则保存在环境变量中
            if(excelPojo.getExtract()!=null){

                extraToEnvironment(excelPojo,res);
            }
        }


    }
    @Test
    public void investFlow(){
        List<ExcelPojo> list = readSpecifyData(3, 9);

        request(replace(list.get(0)),"Invest");


    }
}
