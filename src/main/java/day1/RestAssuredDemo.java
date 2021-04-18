package day1;


import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import java.io.File;

import static io.restassured.RestAssured.given;

/**
 * @Project: part2
 * @Author: 阿线现
 * @Create: 2021-03-27 13:04
 * @Desc：课堂练习
 **/
public class RestAssuredDemo {
      @Test
     public void firstGetDemo(){
          given().//设置请求头，请求体，请求数据
                  when().get("https://www.baidu.com").
                  //.body只打印出响应体。 .all打印出请求头，响应数据所有的信息
                  then().log().body();
      }
    public void firstGetDemoo(){
        given().//设置请求头，请求体，请求数据
                when().get("https://www.baidu.com").
                //.body只打印出响应体。 .all打印出请求头，响应数据所有的信息
                        then().extract().response();
    }
      @Test
      public void getDemo01(){
          given().
                  queryParam("username","windy").queryParam("psd","12344").
                  when().
                  get("http://www.httpbin.org/get").
                  then().
                  log().body();

      }
      //post请求的四种传参方式
      @Test//form表单传参
      public void postDemo01(){
          given().
                  formParam("username","windy").
                  when().
                  post("http://www.httpbin.org/post").
                  then().
                  log().body();
      }
    @Test//json数据传参
    public void postDemo02(){
          String str="{\"mobile_phone\": \"13888882211\",\"pwd\": \"123456566\"}";
        given().body(str).contentType(ContentType.JSON).
                when().
                post("http://www.httpbin.org/post").
                then().
                log().body();
    }
    @Test//XML数据传参
    public void postDemo03(){
        String xml="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "    <groupId>org.example</groupId>\n" +
                "    <artifactId>part2</artifactId>\n" +
                "    <version>1.0-SNAPSHOT</version>";
        given().body(xml).contentType(ContentType.XML).
                when().
                post("http://www.httpbin.org/post").
                then().
                log().body();
    }
    @Test//多参数表单传参
    public void postDemo04(){

        given().multiPart(new File("D:\\阿线现\\test.txt")).
                when().
                post("http://www.httpbin.org/post").
                then().
                log().body();
    }

}
