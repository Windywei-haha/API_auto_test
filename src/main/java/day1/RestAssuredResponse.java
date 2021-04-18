package day1;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.given;

/**
 * @Project: part2
 * @Author: 阿线现
 * @Create: 2021-03-27 16:01
 * @Desc： 获取响应结果的练习，(通过响应报文分析）
 **/
public class RestAssuredResponse {
    @Test
    //结果保存在response里面后面再调用方法
    public void getResponse() {
        String str = "{\"mobile_phone\": \"13888882211\",\"pwd\": \"123456566\"}";
        Response res =
                given().body(str).contentType(ContentType.JSON).
                        when().
                        post("http://www.httpbin.org/post").
                        then().extract().response();
        //获取响应时间
        System.out.println(res.time());
        //获取请求头中的ContecntType字段
        System.out.println(res.getHeader("Content-Type"));
    }
    @Test
    //获取响应报文
    //通过jsonPath.get方法获取json响应报文中具体的某个值(通过响应报文分析）
    public void getResponse01() {
        String str = "{\"mobile_phone\": \"13888882211\",\"pwd\": \"123456566\"}";
        Response res =
                given().body(str).header("Content-Type", "application/json").
                        header("X-Lemonban-Media-Type","lemonban.v1").
                        when().
                        post("http://api.lemonban.com/futureloan/member/login").
                        then().log().all().extract().response();
          System.out.println(res.jsonPath().get("data.id"));
    }
    @Test
    //获取响应报文
    //通过jsonPath.getlist方法获取json响应报文中的数组,再通过索引获取数组中的具体值.结果是一个数组
//    "slides": [
//    {
//        "title": "Wake up to WonderWidgets!",
//            "type": "all"
//    },
//    {
//        "items": [
//        "Why <em>WonderWidgets</em> are great",
//                "Who <em>buys</em> WonderWidgets"
//                ],
//        "title": "Overview",
//            "type": "all"
//    }
//        ],
    public void getResponse02() {

        Response res =
                       given().
                        when().
                                     get("http://www.httpbin.org/json").
                        then().
                                      log().all().extract().response();
        System.out.println(res.jsonPath().getList("slideshow.slides.title"));//得到所有值的集合
        //数组取值
        List<String> list=res.jsonPath().getList("slideshow.slides.title");
        System.out.println(list.get(0));

    }
    @Test
    //获取响应报文
    //通过jsonPath.getlist方法获取html响应报文中的数组,再通过索引获取数组中的具体值
    public void getResponse03() {
        Response res =
                        given().
                        when().
                                   get("http://www.baidu.com").
                        then().
                                   log().all().extract().response();
        System.out.println(res.htmlPath().get("html.head.title"));//得到的是值
        System.out.println(res.htmlPath().get("html.head.meta[0]"));//没有值。只是属性
        System.out.println(res.htmlPath().get("html.head.meta[0].@content"));//拿到属性
        System.out.println(res.htmlPath().getList("html.head.meta"));//得到所有的值，结果为空


    }
    //获取响应报文
    //通过jsonPath.getlist方法获取xml响应报文中的数组,再通过索引获取数组中的具体值
    @Test
    public void getResponse04() {
        Response res =
                given().
                        when().
                        get("http://www.httpbin.org/xml").
                        then().
                        log().all().extract().response();
        System.out.println(res.xmlPath().get("slideshow.slide[0].title"));//得到的是值
        System.out.println(res.xmlPath().get("slideshow.slide[0].@type"));


    }

}