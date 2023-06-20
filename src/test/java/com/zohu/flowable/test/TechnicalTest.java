package com.zohu.flowable.test;

import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.TaskService;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.task.api.Task;
import org.junit.Before;
import org.junit.Test;

/**
 * 技术经理审批
 */
public class TechnicalTest {

    ProcessEngineConfiguration processEngineConfiguration = null;

    /**
     * 流程引擎配置，主要是连接数据库
     */
    @Before
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
     *   完成“技术经理审批”任务
     *
     *   技术经理审批   技术经理唯一： LiuSi
     *
     *   完成相应任务后，可以看到act_ru_task中 NAME_ 为 “技术经理审批” 的任务结束
     *
     */
    @Test
    public void completeTask(){
        // 获取流程引擎对象
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();

        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery()
                //.processInstanceId("2501")
                .processDefinitionId("Scheme:4:12504")
                .taskAssignee("LiuSi")
                .singleResult();


        // 完成技术经理审批
        if(task != null){
            // 完成任务
            taskService.complete(task.getId());
            System.out.println("完成技术经理审批方案Task");
        }


    }






}
