package com.zohu.flowable.test;

import org.flowable.engine.IdentityService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.ProcessEngines;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.idm.api.Group;
import org.flowable.idm.api.User;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class UserTest {


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
     * 维护员工
     */
    @Test
    public void createUser(){
        // 获取流程引擎对象
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();

        // 通过 IdentityService 完成相关的用户和组的管理
        IdentityService identityService = processEngine.getIdentityService();

        // 创建员工1
        User user1 = identityService.newUser("Aa");
        user1.setFirstName("A");
        user1.setLastName("a");
        user1.setEmail("Aa.com");
        // 将用户保存到数据库里面
        identityService.saveUser(user1);

        // 创建员工2
        User user2 = identityService.newUser("Bb");
        user2.setFirstName("B");
        user2.setLastName("b");
        user2.setEmail("Bb@qq.com");
        // 将用户保存到数据库里面
        identityService.saveUser(user2);

        // 创建员工3
        User user3 = identityService.newUser("Cc");
        user3.setFirstName("C");
        user3.setLastName("c");
        user3.setEmail("Cc@qq.com");
        // 将用户保存到数据库里面
        identityService.saveUser(user3);
    }


    /**
     * 维护组
     */
    @Test
    public void createGroup(){
        // 获取流程引擎对象
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();

        // 通过 IdentityService 完成相关的用户和组的管理
        IdentityService identityService = processEngine.getIdentityService();

        // 创建Group对象并指定相关的信息
        Group group1 = identityService.newGroup("employee");
        group1.setName("员工");
        group1.setType("type1");
        // 创建Group对应的表结构数据
        identityService.saveGroup(group1);

        // 创建Group对象并指定相关的信息
        Group group2 = identityService.newGroup("technical");
        group2.setName("技术");
        group2.setType("type2");
        // 创建Group对应的表结构数据
        identityService.saveGroup(group2);

        // 创建Group对象并指定相关的信息
        Group group3 = identityService.newGroup("project");
        group3.setName("项目");
        group3.setType("type3");
        // 创建Group对应的表结构数据
        identityService.saveGroup(group3);

    }




    /**
     * 将用户分配给对应的Group
     */
    @Test
    public void userGroup(){
        // 获取流程引擎对象
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();

        // 通过 IdentityService 完成相关的用户和组的管理
        IdentityService identityService = processEngine.getIdentityService();

        // 根据组的编号找到对应的Group对象
        Group group = identityService.createGroupQuery().groupId("employee").singleResult();
        List<User> list = identityService.createUserQuery().list();
        for (User user : list) {
            // 将用户分配给对应的组
            identityService.createMembership(user.getId(),group.getId());
        }


    }






}
