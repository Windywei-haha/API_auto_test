package day1;

import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

/**
 * @Project: part2
 * @Author: 阿线现
 * @Create: 2021-03-30 18:52
 * @Desc：
 **/
public class TestInvest {
    //声明全局变量
    String token_Invest;
    int memberID_Invest;

    @Test(priority = 1)
    public void logonManager() {
        //投资人登录
        String strJson05 = "{\"mobile_phone\": \"18390129011\",\"pwd\": \"12345678\"}";
        Response resLogonInvest =
                given().
                        header("Content-Type", "application/json").
                        header("X-Lemonban-Media-Type", "lemonban.v2").
                        body(strJson05).
                        when().
                        post("http://api.lemonban.com/futureloan/member/login").

                        then().
                        extract().response();

        //获取全局变量投资人token
        token_Invest = resLogonInvest.jsonPath().get("data.token_info.token");
        System.out.println("投资人的token:" + token_Invest);
        //获取全局变量投资人的id
        memberID_Invest = resLogonInvest.jsonPath().get("data.id");
        System.out.println("投资人的id:" + memberID_Invest);
    }

    //投资人充值请求
    @Test(priority = 2)
    public void rechargeManager() {

        String strJson06 = "{\"member_id\": " + memberID_Invest + ", \"amount\": 2500}";
        Response resRechargePerson =
                given().
                        header("Content-Type", "application/json").
                        header("X-Lemonban-Media-Type", "lemonban.v2").
                        header("Authorization", "Bearer " + token_Invest).
                        body(strJson06).
                        when().
                        post("http://api.lemonban.com/futureloan/member/recharge").

                        then().
                        log().all().extract().response();
    }

    //投资人投资项目。用类名点的方式获取其他类里面的静态属性,这个字段依赖上面类里面的结果字段，所以必须先执行上面的类
    @Test(priority = 3)
    public void loadInvest() {
        String strJson09 = "{\"member_id\":" + memberID_Invest + ",\"loan_id\":"+ TestBManager.loan_id+",\"amount\":200}";
        Response resInvest =
                given().
                        header("Content-Type", "application/json").
                        header("X-Lemonban-Media-Type", "lemonban.v2").
                        header("Authorization", "Bearer " + token_Invest).
                        body(strJson09).
                        when().
                        post("http://api.lemonban.com/futureloan/member/invest").

                        then().
                        log().all().extract().response();
    }
}