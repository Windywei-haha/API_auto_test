package testcasesDay3.common;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.alibaba.fastjson.JSONObject;

import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import testcasesDay3.data.Constants;
import testcasesDay3.data.Environment;
import testcasesDay3.pojo.ExcelPojo;
import testcasesDay3.utils.JDBCUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.restassured.RestAssured.given;
import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.BIG_DECIMAL;


/**
 * @Project: part2
 * @Author: 阿线现
 * @Create: 2021-04-04 20:09
 * @Desc：
 **/
public class BaseTest {
    @BeforeTest
    public void setupTest() throws FileNotFoundException {
        //restassured 里面如果返回值是json小数，那么其类型是float,会丢失精度。
        // 问题解决方案：声明restassured返回json小数的类型是bigdecimal，也就是把float和double的类型全部转为Bigdcimal类型的

        //在做断言的时候这个全局变量把实际返回的json值转化为bigdeciml，
        // 但是自己也需要把期望的结果转为bigDecimal
        //BigDecimal bigDecimal=BigDecimal.valueOf(400.00);

        RestAssured.config = RestAssured.config().jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAL));
        //BaseUrl全局配置
        RestAssured.baseURI = Constants.BASE_URL;

        //测试用例全部的结果日志全局存储到本地文件中
        //创建log文件夹，项目的根目录的获取方式：System.getProperty("user.dir")
//        File file=new File(System.getProperty("user.dir")+"\\log");
//        if(!file.exists()){
//            //创建
//            file.mkdir();
//        }
//
//        PrintStream fileOutPutStream = new PrintStream(new File("log/test_all.log"));
//        RestAssured.filters(new RequestLoggingFilter(fileOutPutStream),new ResponseLoggingFilter(fileOutPutStream));
    }


    //封装了请求方法
    //excelpojo保存了一条用例需要的所有数据
    //返回值就是接口响应的结果
    public static Response request(ExcelPojo excelPojo,String sheetName) {
        String logPath;
        //判断控制为true的话,log自动生成到文件夹下。不赋值，默认为true?
        if(Constants.LOG_TO_FILE) {
            //生成该测试用例的单独日志信息
            //创建log文件夹，下面的sheetName文件夹。项目的根目录的获取方式：System.getProperty("user.dir")
            File file = new File(System.getProperty("user.dir") + "\\log\\"+ sheetName);
            if (!file.exists()) {
                //创建多目录层级
                file.mkdirs();
            }
            //为每个请求单独做日志保存,把日志保存到logPath路径下
            logPath = file+"\\test" + excelPojo.getCaseId()+".log";
            PrintStream fileOutPutStream = null;
            try {
                fileOutPutStream = new PrintStream(new File(logPath));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            RestAssured.config = RestAssured.config().logConfig(LogConfig.logConfig().defaultStream(fileOutPutStream));
        }

        //请求的url
        String url = excelPojo.getUrl();
        //请求方法
        String method = excelPojo.getMethod();
        //请求头,并转换为MAP
        String headers = excelPojo.getRequestHeader();
        Map<String, Object> headersMap = JSONObject.parseObject(headers, Map.class);

        //请求参数
        String params = excelPojo.getInputParams();
        //用if来判断请求,先声明一个空的变量res
        Response res = null;
        if ("get".equalsIgnoreCase(method)) {
            res = given().headers(headersMap).log().all().when().get(url).then().log().all().extract().response();
        } else if ("post".equalsIgnoreCase(method)) {
            res = given().headers(headersMap).body(params).log().all().when().post(url).then().log().all().extract().response();

        } else if ("patch".equalsIgnoreCase(method)) {
            res = given().headers(headersMap).body(params).log().all().when().patch(url).then().log().all().extract().response();

        } else if ("put".equalsIgnoreCase(method)) {
            res = given().headers(headersMap).body(params).log().all().when().put(url).then().log().all().extract().response();

        }
        //把日志加到Allure报表中
        if(Constants.LOG_TO_FILE) {
            try {
                Allure.addAttachment("接口请求响应信息",
                        new FileInputStream(logPath));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return res;

    }

    //对读取进行封装
    //读取excel指定sheet的所有数据，
    //FILE是文件对象， sheets是指定的页数编号
    public List<ExcelPojo> readAllData(int sheetNumber) {
        File file = new File(Constants.EXCEL_FILE_PATH);

        //导入的参数对象
        ImportParams importParams = new ImportParams();
        //读取第二个Sheet
        importParams.setStartSheetIndex(sheetNumber);
        //读取Excel
        List<ExcelPojo> listData01 = ExcelImportUtil.importExcel(file, ExcelPojo.class, importParams);
        return listData01;
    }

    //读取指定行的excel数据
    public List<ExcelPojo> readSpecifyData(int sheetNumber, int startRow, int readRows) {
        File file = new File(Constants.EXCEL_FILE_PATH);

        //导入的参数对象
        ImportParams importParams = new ImportParams();
        //读取第二个Sheet
        importParams.setStartSheetIndex(sheetNumber);
        //设置读取的起始行
        importParams.setStartRows(startRow);
        //设置读取的总行数
        importParams.setReadRows(readRows);
        //读取Excel
        List<ExcelPojo> listData02 = ExcelImportUtil.importExcel(file, ExcelPojo.class, importParams);
        return listData02;
    }

    //读取从指定行到最后一行的数据
    public List<ExcelPojo> readSpecifyData(int sheetNumber, int startRow) {
        //读取行数不做限制的话默认读取到最后一行
        File file = new File(Constants.EXCEL_FILE_PATH);
        //导入的参数对象
        ImportParams importParams = new ImportParams();
        //读取第二个Sheet
        importParams.setStartSheetIndex(sheetNumber);
        //设置读取的起始行
        importParams.setStartRows(startRow);
        //读取Excel
        List<ExcelPojo> listData02 = ExcelImportUtil.importExcel(file, ExcelPojo.class, importParams);
        return listData02;
    }

    //封装提取表达式的方法,将对应的接口返回字段提取到环境变量中
    //extract提取返回的json字符串、、、换为excelpojo
    //res 接口返回
    public void extraToEnvironment(ExcelPojo excelPojo, Response res) {

        Map<String, Object> extractMap = JSONObject.parseObject(excelPojo.getExtract(), Map.class);

        //for循环遍历extractMap
        for (String key : extractMap.keySet()) {
            //通过key得到value
            Object value = extractMap.get(key);
            //得到需要替换的所有值
            Object values = res.jsonPath().get(value.toString());
            //存储到环境变量中去.环境变量声明为一个MAP集合来存储这些键值对
            Environment.envData.put(key, values);
        }
    }

    //从环境变量中取得对应的值，进行正则替换
    public String regexReplace(String orgStr) {
        if (orgStr != null) {
            //用if判断如果没有需要替换的正则表达式，就返回原字符串
            //if (orgStr!=null) {
            //orgStr:原始的字符串；replaceStr:需要替换的字符串
            //Pattern.compile正则表达式匹配器
            Pattern pattern1 = Pattern.compile("\\{\\{(.*?)}}");
            //matcher 去匹配哪个字符串，得到匹配对象
            Matcher matcher1 = pattern1.matcher(orgStr);
            //while循环
            String result = orgStr;//定义一个变量用来接收返回值,有多个需要替换的值

            while (matcher1.find()) {
                //group0表示获取到整个匹配到的内容
                String outStr1 = matcher1.group(0);
                //group1表示获取{{}}包裹的内容
                //获取到member_ID这个字段
                String innerStr1 = matcher1.group(1);
                //从环境变量中取到实际的值，通过memberID这个字段在环境变量中找到data.id.得到需要替换的值
                Object replaceStr = Environment.envData.get(innerStr1);
                //replace
                result = result.replace(outStr1, replaceStr + "");//while循环结束后返回
            }
            return result;
            // }
            // return orgStr;
        }
        return orgStr;
    }
    //对正则替换进行封装

    public ExcelPojo replace(ExcelPojo excelPojo) {
        //正则替换，参数输入
        String reallyBody = regexReplace(excelPojo.getInputParams());
        excelPojo.setInputParams(reallyBody);
        //正则替换，请求头
        String headers = regexReplace(excelPojo.getRequestHeader());
        excelPojo.setRequestHeader(headers);
        //正则替换，url地址
        String url = regexReplace(excelPojo.getUrl());
        excelPojo.setUrl(url);
        //正则替换响应结果
        String expected = regexReplace(excelPojo.getExpected());
        excelPojo.setExpected(expected);
        //正则替换数据库断言
        String assertDB = regexReplace(excelPojo.getAssertDB());
        excelPojo.setAssertDB(assertDB);

        return excelPojo;
    }

    //响应断言，做一个非空判断，不为空的话再做断言
    public static void assertResponse(ExcelPojo excelPojo, Response res) {


        if (excelPojo.getExpected() != null) {

            Map<String, Object> exceptMap = JSONObject.parseObject(excelPojo.getExpected(), Map.class);
            for (String key : exceptMap.keySet()) {
                Object except = exceptMap.get(key);
                Object actual = res.jsonPath().get(key);
                Assert.assertEquals(actual, except);
            }
        }
    }

    //数据库断言,做一个非空判断，不为空的话再做断言
    //数据类型的转化：因为SQL语句（count*)得到的结果是long类型的
    //需要对每种类型的数据做判断
    public static void assertSQL(ExcelPojo excelPojo) {

        String dbAssert = excelPojo.getAssertDB();
        if (dbAssert != null) {
            Map<String, Object> exceptMap = JSONObject.parseObject(dbAssert, Map.class);

            //key是要执行的SQL语句

            for (String key : exceptMap.keySet()) {

                Object except = exceptMap.get(key);
                //如果期望的结果是bigdecimal类型，把实际结果转化为object
                if (except instanceof BigDecimal) {
                    Object actual = JDBCUtils.querySingleData(key);
                    Assert.assertEquals(actual, except);
                    //如果期望的结果是integer类型，强转为long
                } else if (except instanceof Integer) {
                    //再用longValue方法转成long类型
                    long except2 = ((Integer) except).longValue();
                    //通过数据库查询的方法得到结果
                    Object actual = JDBCUtils.querySingleData(key);
                    Assert.assertEquals(actual, except2);
                }

            }

        }
    }
}