package com.zouhu.employee;

import java.util.Scanner;

public class Employee {

    public static void main(String[] args) {

        //输入id
        Scanner myScanner = new Scanner(System.in);
        System.out.println("请输入userId： ");
        String  userId = myScanner.next();

        //接收任务并完成任务
        EmployeeRun employeeRun = new EmployeeRun();
        employeeRun.before();
        employeeRun.testRunProcess();  //启动流程实例
        employeeRun.claimTaskCandidate(userId);
        employeeRun.completeTask(userId);

    }

}
