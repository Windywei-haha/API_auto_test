package day1;

import groovy.json.JsonOutput;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

/**
 * @Project: part2
 * @Author: 阿线现
 * @Create: 2021-03-27 17:57
 * @Desc：通过RestAssured完成注册->登录->充值->新增项目->项目审核->投资（管理员+投资人+借款人）
 **/
public class RestAssuredHomework {
    //借款人
    @Test
    public void restAssuredHomework() {
        //注册管理员请求
//        String strJson = "{\"mobile_phone\": \"13319130988\",\"pwd\": \"12345678\",\"type\":0,\"reg_name\":\"阿线现\"}";
//        Response resRegisterManager =
//                given().
//                        header("Content-Type", "application/json").
//                        header("X-Lemonban-Media-Type", "lemonban.v2").
//                        body(strJson).
//                        when().
//                        post("http://api.lemonban.com/futureloan/member/register").
//
//                        then().
//                                 extract().response();
//        String phoneNumberManger=resRegisterManager.jsonPath().get("data.mobile_phone");
//
//        //注册借款人请求
//
//        String strJson01 = "{\"mobile_phone\": \"18292089066\",\"pwd\": \"abc45678\",\"type\":1,\"reg_name\":\"小明\"}";
//        Response resRegisterLoan =
//                given().
//                        header("Content-Type", "application/json").
//                        header("X-Lemonban-Media-Type", "lemonban.v2").
//                        body(strJson01).
//                        when().
//                        post("http://api.lemonban.com/futureloan/member/register").
//
//                        then().extract().response();
//        String phoneNumberLoan=resRegisterLoan.jsonPath().get("data.mobile_phone");
//        //注册投资人
//                 String strJson02 = "{\"mobile_phone\": \"18390129011\",\"pwd\": \"12345678\",\"type\":1,\"reg_name\":\"投资人\"}";
//        Response resRegisterInvest =
//                      given().
//                                   header("Content-Type", "application/json").
//                                   header("X-Lemonban-Media-Type", "lemonban.v2").
//                                   body(strJson02).
//                        when().
//                              post("http://api.lemonban.com/futureloan/member/register").
//
//                        then().extract().response();
//        String phoneNumberInvest=resRegisterInvest.jsonPath().get("data.mobile_phone");

        //管理员登录请求

        String strJson03 = "{\"mobile_phone\": \"13319130988\",\"pwd\": \"12345678\"}";
        Response resLogonManager =
                given().
                        header("Content-Type", "application/json").
                        header("X-Lemonban-Media-Type", "lemonban.v2").
                        body(strJson03).
                        when().
                        post("http://api.lemonban.com/futureloan/member/login").

                        then()
                                .extract().response();
        //获取管理员token
        String tokenManager= resLogonManager.jsonPath().get("data.token_info.token");
        System.out.println("管理员的token:" + tokenManager);
        //获取管理员的id
        int memberIDManager = resLogonManager.jsonPath().get("data.id");
        System.out.println("管理员的id:" + memberIDManager);

        //借款人登录请求

        String strJson04 = "{\"mobile_phone\": \"18292089066\",\"pwd\": \"abc45678\"}";
        Response resLogonLoan =
                given().
                        header("Content-Type", "application/json").
                        header("X-Lemonban-Media-Type", "lemonban.v2").
                        body(strJson04).
                        when().
                        post("http://api.lemonban.com/futureloan/member/login").

                        then().
                              extract().response();
        //获取借款人token
        String token = resLogonLoan.jsonPath().get("data.token_info.token");
        System.out.println("借款人的token:" + token);
        //获取借款人的id
        int memberID = resLogonLoan.jsonPath().get("data.id");
        System.out.println("借款人的id:" + memberID);

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
        //获取投资人token
        String token_Invest = resLogonInvest.jsonPath().get("data.token_info.token");
        System.out.println("投资人的token:" + token_Invest);
        //获取投资人的id
        int memberID_Invest = resLogonInvest.jsonPath().get("data.id");
        System.out.println("投资人的id:" + memberID_Invest);


        //投资人充值请求

        String strJson06 = "{\"member_id\": "+memberID_Invest+", \"amount\": 2500}";
        Response resRechargePerson =
                given().
                        header("Content-Type", "application/json").
                        header("X-Lemonban-Media-Type", "lemonban.v2").
                        header("Authorization", "Bearer " +token_Invest).
                        body(strJson06).
                        when().
                        post("http://api.lemonban.com/futureloan/member/recharge").

                        then().
                              log().all().extract().response();


        //新增项目请求
        String strJson07 = "{ \"member_id\":"+memberID+", \"title\":\"报名 Java 全栈自动化课程\", \"amount\":5500.00, \"loan_rate\":12.0, \"loan_term\":12, \"loan_date_type\":1, \"bidding_days\":5 }";
        Response resAddProject =
                       given().
                               header("Content-Type", "application/json").
                               header("X-Lemonban-Media-Type", "lemonban.v2").
                               header("Authorization", "Bearer " +token).
                               body(strJson07).
                        when().
                                post("http://api.lemonban.com/futureloan/loan/add").

                        then().
                                 log().all(). extract().response();
        //获取项目id
        int loan_id=resAddProject.jsonPath().get("data.id");
        System.out.println("投资项目的Id为:"+resAddProject.jsonPath().get("data.id"));


        //管理员项目审核请求
        String strJson08 = "{\"loan_id\": "+loan_id+", \"approved_or_not\": true}";
        Response resAudit =
                given().
                        header("Content-Type", "application/json").
                        header("X-Lemonban-Media-Type", "lemonban.v2").
                        header("Authorization", "Bearer " + tokenManager).
                        body(strJson08).
                        when().
                        patch("http://api.lemonban.com/futureloan/loan/audit").

                        then().
                               log().all().extract().response();

        //投资人投资项目请求
        String strJson09 = "{\"member_id\":"+memberID_Invest+",\"loan_id\":"+loan_id+",\"amount\":200}";
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