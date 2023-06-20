package com.zohu.flowable.test;

import org.flowable.engine.*;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.idm.api.Group;
import org.flowable.task.api.Task;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeTest {


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
     * 启动流程实例
     *
     * 员工employee提交方案
     * 要启动流程实例，需要提供一些初始化*流程变量*。一般来说，可以通过呈现给用户的表单，或者在流程由其他系统自动触发时通过REST API，来获取这些变量
     * 在这个例子里，我们简化直接在代码中定义了，我们使用*RuntimeService*启动一个流程实例
     *
     * 相关结果可以在 act_hi_taskinst 里面查询
     */
    @Test
    public void testRunProcess(){
        // 获取流程引擎对象
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();

        // 启动流程实例通过 RuntimeService 对象
        RuntimeService runtimeService = processEngine.getRuntimeService();

        // 通过 IdentityService 完成相关的用户和组的管理
        IdentityService identityService = processEngine.getIdentityService();

        // 构建流程变量
        Map<String,Object> variables = new HashMap<>();

        variables.put("candidate1","张三") ;// 谁申请请假
        variables.put("nrOfHolidays",3); // 请几天假
        variables.put("description","工作累了，想出去玩玩"); // 请假的原因

        Group group = identityService.createGroupQuery().groupId("employee").singleResult();
        variables.put("employee",group.getId());  // 给流程定义中的UEL表达式赋值



        // 启动流程实例，第一个参数是流程定义的id
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceById("Scheme:6:42504", variables);// 启动流程实例



        // 输出相关的流程实例信息
        System.out.println("流程定义的ID：" + processInstance.getProcessDefinitionId());
        System.out.println("流程实例的ID：" + processInstance.getId());
        System.out.println("当前活动的ID：" + processInstance.getActivityId());

    }



    /**
     * 根据登录的用户查询对应的可以拾取的任务
     *
     */
    @Test
    public void queryTaskCandidateGroup(){
        // 获取流程引擎对象
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();

        // 根据当前登录的用户找到对应的组
        IdentityService identityService = processEngine.getIdentityService();

        // 当前用户所在的组
        Group group = identityService.createGroupQuery().groupMember("Aa").singleResult();

        TaskService taskService = processEngine.getTaskService();
        List<Task> list = taskService.createTaskQuery()
                //.processInstanceId("2501")
                .processDefinitionId("Scheme:4:12504")
                .taskCandidateGroup(group.getId())
                .list();

        //输出可以拾取的任务
        for (Task task : list) {
            System.out.println("task.getId() = " + task.getId());
            System.out.println("task.getName() = " + task.getName());
        }
    }


    /**
     * 拾取任务
     *    一个候选人拾取了这个任务之后其他的用户就没有办法拾取这个任务了
     *    所以如果一个用户拾取了任务之后又不想处理了，那么可以退还
     *
     * 任务拾取成功后，可以在表格 act_ru_task 里面 看到 对应任务的 ASSIGNEE 里面不为空（例如员工Aa）
     */
    @Test
    public void claimTaskCandidate(){
        //当前登录的用户
        String userId = "Aa";

        // 获取流程引擎对象
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();

        // 根据当前登录的用户找到对应的组
        IdentityService identityService = processEngine.getIdentityService();

        // 当前用户所在的组
        Group group = identityService.createGroupQuery().groupMember(userId).singleResult();
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery()
                //.processInstanceId("2501")
                .processDefinitionId("Scheme:4:12504")
                .taskCandidateGroup(group.getId())
                .singleResult();

        // 任务拾取
        if(task != null) {
            taskService.claim(task.getId(),userId);
            System.out.println("任务拾取成功");
        }

    }



    /**
     *   完成“创建项目方案”任务
     *
     *   可以看到act_ru_task 里面的创建项目方案已经小时 ， 并且重新出现了NAME_分别为 项目经理审批 、 技术经理审批 的两个任务
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
                .taskAssignee("Aa")   //分配任务给
                .singleResult();

        // 完成任务
        if(task != null){
            taskService.complete(task.getId());
            System.out.println("完成用户创建项目方案Task");
        }
    }

}
