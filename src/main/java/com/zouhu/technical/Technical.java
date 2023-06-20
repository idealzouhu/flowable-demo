package com.zouhu.technical;

import java.util.Scanner;

public class Technical {

    public static void main(String[] args) {

        //输入id
        Scanner myScanner = new Scanner(System.in);
        System.out.println("请输入userId： ");
        String  userId = myScanner.next();

        //调用
        TechnicalRun technicalRun = new TechnicalRun();
        technicalRun.before();
        technicalRun.completeTask(userId);

    }
}
