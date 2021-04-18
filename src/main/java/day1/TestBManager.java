package day1;

import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class TestBManager {
    //声明全局变量并赋给初始值
    String mobilePhone = "13319130988";
    String psw = "12345678";
    //声明全局变量
    String tokenManager;
    int memberIDManager;
    //声明静态的全局变量
    static int loan_id;
    //管理人登录
    @Test(priority = 1)
    public void logonManager() {

        String strJson03 = "{\"mobile_phone\": " + mobilePhone + ",\"pwd\": " + psw + "}";
        Response resLogonManager =
                given().
                        header("Content-Type", "application/json").
                        header("X-Lemonban-Media-Type", "lemonban.v2").
                        body(strJson03).
                        when().
                        post("http://api.lemonban.com/futureloan/member/login").

                        then().log().all()
                        .extract().response();
        //获取管理员token
         tokenManager = resLogonManager.jsonPath().get("data.token_info.token");
        System.out.println("管理员的token:" + tokenManager);
        //获取管理员的id
        memberIDManager = resLogonManager.jsonPath().get("data.id");
        System.out.println("管理员的id:" + memberIDManager);
    }

    @Test(priority = 2)
    public void addProject() {
        //新增项目请求
        String strJson07 = "{ \"member_id\":" + memberIDManager + ", \"title\":\"报名 Java 全栈自动化课程\", \"amount\":5500.00, \"loan_rate\":12.0, \"loan_term\":12, \"loan_date_type\":1, \"bidding_days\":5 }";
        Response resAddProject =
                given().
                        header("Content-Type", "application/json").
                        header("X-Lemonban-Media-Type", "lemonban.v2").
                        header("Authorization", "Bearer " + tokenManager).
                        body(strJson07).
                        when().
                        post("http://api.lemonban.com/futureloan/loan/add").

                        then().
                        log().all().extract().response();
        //获取项目id
        loan_id = resAddProject.jsonPath().get("data.id");
        System.out.println("投资项目的Id为:" + loan_id);
    }
        @Test(priority = 3)
        public void reeProject() {
            //管理员项目审核请求
            String strJson08 = "{\"loan_id\": " + loan_id + ", \"approved_or_not\": true}";
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
        }
    }
