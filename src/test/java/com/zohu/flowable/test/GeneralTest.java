package com.zohu.flowable.test;

import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.TaskService;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.task.api.Task;
import org.junit.Before;
import org.junit.Test;
import sun.font.TrueTypeFont;

import java.util.HashMap;
import java.util.Map;

/**
 *  总经理对方案继续进行审批
 */
public class GeneralTest {


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
     *  同意该方案
     */
    @Test
    public void approveScheme(){

        // 获取流程引擎对象
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();

        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery()
                .processDefinitionId("Scheme:4:12504")
                .taskAssignee("GuoSi")
                .singleResult();

        // 添加流程变量
        Map<String,Object> variables = new HashMap<>();
        variables.put("approved", true);   //同意该方案


        // 完成任务
        taskService.complete(task.getId(),variables);

    }


    /**
     *  拒绝该方案
     */
    @Test
    public void rejectScheme(){

        // 获取流程引擎对象
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();

        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery()
                .processDefinitionId("Scheme:4:12504")
                .taskAssignee("GuoSi")
                .singleResult();

        // 添加流程变量
        Map<String,Object> variables = new HashMap<>();
        variables.put("approved", false); // 拒绝该方案


        // 完成任务
        taskService.complete(task.getId(),variables);


    }

}
