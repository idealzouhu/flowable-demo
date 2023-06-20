package com.zouhu.project;

import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.task.api.Task;

import java.util.Map;
import java.util.Scanner;

public class ProjectRun {

    ProcessEngineConfiguration processEngineConfiguration = null;

    /**
     * 流程引擎配置，主要是连接数据库
     *
     */
    public void before(){
        // 获取 ProcessEngineConfiguration 对象
        processEngineConfiguration = new StandaloneProcessEngineConfiguration();

        // 配置 相关的数据库的连接信息
        processEngineConfiguration.setJdbcDriver("com.mysql.cj.jdbc.Driver");
        processEngineConfiguration.setJdbcUsername("root");
        processEngineConfiguration.setJdbcPassword("www987654.-");
        processEngineConfiguration.setJdbcUrl("jdbc:mysql://localhost:3306/flowable1?serverTimezone=UTC&nullCatalogMeansCurrent=true");
        // 如果数据库中的表结构不存在就新建
        processEngineConfiguration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);

    }


    /**
     *   完成“项目经理审批”任务
     *
     *   项目经理审批   项目经理唯一： LiSi
     *
     */
    public void completeTask(String userId) {
        // 获取流程引擎对象
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();

        RuntimeService runtimeService = processEngine.getRuntimeService();

        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery()
                .processDefinitionId("Scheme:1:4")
                .taskAssignee(userId)
                .singleResult();


        //获取变量
//        String taskId = task.getId();
//        Map<String, Object> variables=taskService.getVariables(taskId);
//        String time = (String) variables.get("time");
//        String technicalDescription = (String) variables.get("technicalDescription");

        //输出表单方案
        //System.out.printf("方案使用技术：%s \n", time);
        //System.out.printf("方案预估时间：%s \n ", technicalDescription);
        System.out.println("**********************");
        System.out.println("方案使用技术：java");
        System.out.println("方案预估时间：4");
        System.out.println("**********************");




        //提交表单方案
        Scanner myScanner = new Scanner(System.in);
        System.out.println("请选择是否批准： ");
        String  s = myScanner.next();



        // 完成项目经理审批
        if(task != null && s.equals("true")){
            // 完成任务
            taskService.complete(task.getId());
            System.out.println("完成项目经理审批方案Task");
        }
    }



}
