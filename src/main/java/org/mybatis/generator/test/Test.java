package org.mybatis.generator.test;

/**
 * <b>类说明：</b>
 * <p>
 * <b>详细描述：</b>
 *
 * @Author tanzibiao
 * @Date 2021-04-15 15:02:01
 **/
public class Test {
    public static void main(String[] args) {
        String s = "02007321040901334365592425603132|1|1000.00|1000.00|0.00|0.00|0.00|0.00|2022-01-31|2021-04-09 14:45:30|1|2|36M00331000013000827|20200324105000049";
        System.out.println(s.split("\\|").length);
    }
}
