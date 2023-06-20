package com.zouhu.general;

import java.util.Scanner;

public class General {


    public static void main(String[] args) {


        //
        Scanner myScanner = new Scanner(System.in);
        System.out.println("请输入userId： ");
        String  userId = myScanner.next();

        //
        GeneralRun generalRun = new GeneralRun();
        generalRun.before();
        generalRun.approveScheme(userId);



    }

}
