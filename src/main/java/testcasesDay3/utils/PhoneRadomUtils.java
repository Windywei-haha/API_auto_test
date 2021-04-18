package testcasesDay3.utils;

import java.util.Map;
import java.util.Random;

/**
 * @Project: part2
 * @Author: 阿线现
 * @Create: 2021-04-10 11:37
 * @Desc： 随机生成没有存在的手机号
 **/
public class PhoneRadomUtils {
    public static void main(String[] args) {
        System.out.println(getRandomPhone());
    }
   //生成随机数的方法
    public static String getRandomPhone(){
        //random,随机数； nextInt方法，生成一个随机数，范围是从0到你的参数范围之内
        Random random=new Random();

        String phonePrefix="133";
        //生成8位的循环整数，随机拼接
        for(int i=0;i<8;i++){
            //生成一个0-9的随机整数
            int num = random.nextInt(8);
            phonePrefix=phonePrefix+num;
        }
        return phonePrefix;

    }
    //判断手机号是否在数据库存在的方法
    public  static String getUnregPhone(){

        String randomPhone="";
        while (true){
          randomPhone = getRandomPhone();
            //查询数据
            Object result = JDBCUtils.querySingleData("select count(*) from member where mobile_phone=" + randomPhone);
            //System.out.println(result);
            if((Long)result==0){
                //表示没有被注册，符合需求
                break;
            }
            else {
                //表示已经被注册了，继续循环
                continue;
            }
        }
        return randomPhone;
    }
}
