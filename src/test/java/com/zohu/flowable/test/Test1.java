package com.zohu.flowable.test;

import org.flowable.engine.*;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test1 {


    /**
     * 获取流程引擎对象
     */
    @Test
    public void testProcessEngine(){
        // 获取 ProcessEngineConfiguration 对象
        ProcessEngineConfiguration processEngineConfiguration = new StandaloneProcessEngineConfiguration();

        // 配置 相关的数据库的连接信息
        processEngineConfiguration.setJdbcDriver("com.mysql.cj.jdbc.Driver");
        processEngineConfiguration.setJdbcUsername("root");
        processEngineConfiguration.setJdbcPassword("www987654.-");
        processEngineConfiguration.setJdbcUrl("jdbc:mysql://localhost:3306/flowable?serverTimezone=UTC&nullCatalogMeansCurrent=true");
        // 如果数据库中的表结构不存在就新建
        processEngineConfiguration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);


        // 通过 ProcessEngineConfiguration 构建我们需要的 ProcessEngine 对象
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
    }

    ProcessEngineConfiguration processEngineConfiguration = null;

    @Before
    public void before(){
        // 获取 ProcessEngineConfiguration 对象
        processEngineConfiguration = new StandaloneProcessEngineConfiguration();

        // 配置 相关的数据库的连接信息
        processEngineConfiguration.setJdbcDriver("com.mysql.cj.jdbc.Driver");
        processEngineConfiguration.setJdbcUsername("root");
        processEngineConfiguration.setJdbcPassword("www987654.-");
        processEngineConfiguration.setJdbcUrl("jdbc:mysql://localhost:3306/flowable?serverTimezone=UTC&nullCatalogMeansCurrent=true");
        // 如果数据库中的表结构不存在就新建
        processEngineConfiguration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);

    }


    /**
     * 部署流程
     *
     */
    @Test
    public void testDeploy(){
        // 1.获取 ProcessEngine 对象
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
        // 2.获取RepositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 3.完成流程的部署操作
        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("Scheme.bpmn20.xml")// 关联要部署的流程文件
                .name("请求流程")
                .deploy() ;// 部署流程
        System.out.println("deploy.getId() = " + deploy.getId());
        System.out.println("deploy.getName() = " + deploy.getName());
    }


    /**
     * 查询流程定义的信息
     * 相当于获取 act_re_procdef 表中的信息
     */
    @Test
    public void testDeployQuery(){
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId("1")
                .singleResult();
        System.out.println("processDefinition.getDeploymentId() = " + processDefinition.getDeploymentId());
        System.out.println("processDefinition.getName() = " + processDefinition.getName());
        System.out.println("processDefinition.getDescription() = " + processDefinition.getDescription());
        System.out.println("processDefinition.getId() = " + processDefinition.getId());
    }


    /**
     * 删除流程定义
     */
    @Test
    public void testDeleteDeploy(){
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 删除部署的流程 第一个参数是 id  如果部署的流程启动了就不允许删除了
        // repositoryService.deleteDeployment("2501");
        // 第二个参数是级联删除，如果流程启动了 相关的任务一并会被删除掉
        repositoryService.deleteDeployment("1",true);
    }


    /**
     * 启动流程实例
     * 要启动流程实例，需要提供一些初始化*流程变量*。一般来说，可以通过呈现给用户的表单，或者在流程由其他系统自动触发时通过REST API，来获取这些变量
     * 在这个例子里，我们简化直接在代码中定义了，我们使用*RuntimeService*启动一个流程实例
     */
    @Test
    public void testRunProcess(){
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();

        // 我们需要通过RuntimeService来启动流程实例
        RuntimeService runtimeService = processEngine.getRuntimeService();
        // 构建流程变量
        Map<String,Object> variables = new HashMap<>();
        variables.put("employee","张三");
        variables.put("nrOfHolidays",3);
        variables.put("desciption","工作累了，出去玩玩");
        // 启动流程实例 ， 其中 "holidayRequest" 为流程的id
        ProcessInstance holidayRequest = runtimeService.startProcessInstanceByKey("holidayRequest", variables);
        System.out.println("holidayRequest.getProcessDefinitionId() = " + holidayRequest.getProcessDefinitionId());
        System.out.println("holidayRequest.getActivityId() = " + holidayRequest.getActivityId());
        System.out.println("holidayRequest.getId() = " + holidayRequest.getId());
    }

    /**
     * 测试任务查询
     */
    @Test
    public void testQueryTask(){
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        List<Task> list = taskService.createTaskQuery()
                .processDefinitionKey("holidayRequest") // 指定查询的流程编程
                .taskAssignee("zhangsan") // 查询这个任务的处理人
                .list();
        for (Task task : list) {
            System.out.println("task.getProcessDefinitionId() = " + task.getProcessDefinitionId());
            System.out.println("task.getName() = " + task.getName());
            System.out.println("task.getAssignee() = " + task.getAssignee());
            System.out.println("task.getDescription() = " + task.getDescription());
            System.out.println("task.getId() = " + task.getId());
        }
    }

    /**
     * 完成当前任务
     */
    @Test
    public void testCompleteTask(){
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery()
                .processDefinitionKey("holidayRequest")
                .taskAssignee("XiaoMing")
                .singleResult();
        // 创建流程变量
        Map<String,Object> map = new HashMap<>();
        map.put("approved",false);
        // 完成任务
        taskService.complete(task.getId(),map);

    }

    /**
     * 获取流程任务的历史数据
     */
    @Test
    public void testHistory(){
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
        HistoryService historyService = processEngine.getHistoryService();
        List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery()
                .processDefinitionId("holidayRequest:1:7503")
                .finished() // 查询的历史记录的状态是已经完成
                .orderByHistoricActivityInstanceEndTime().asc() // 指定排序的字段和顺序
                .list();
        for (HistoricActivityInstance history : list) {
            System.out.println(history.getActivityName()+":"+history.getAssignee()+"--"
                    +history.getActivityId()+":" + history.getDurationInMillis()+"毫秒");
        }

    }


}
