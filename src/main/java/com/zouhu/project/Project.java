package com.zouhu.project;

import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.TaskService;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.task.api.Task;

import java.util.Scanner;

public class Project {


    public static void main(String[] args) {


        //输入id
        Scanner myScanner = new Scanner(System.in);
        System.out.println("请输入userId： ");
        String  userId = myScanner.next();


        //
        ProjectRun projectRun = new ProjectRun();
        projectRun.before();
        projectRun.completeTask(userId);

    }


}
