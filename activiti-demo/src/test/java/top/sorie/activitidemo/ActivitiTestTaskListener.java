package top.sorie.activitidemo;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class ActivitiTestTaskListener {

    @Test
    public void testDeploy() {
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = defaultProcessEngine.getRepositoryService();
        Deployment deployment = repositoryService.createDeployment()
                .name("出差申请-task")
                .addClasspathResource("bpmn/test3.bpmn20.xml")
                .deploy();
        // 输出部署信息
        System.out.println("deploymentId=" + deployment.getId());
        System.out.println("deploymentName=" + deployment.getName());
    }


    @Test
    public void testAssigneeListener() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        // 启动流程实例
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("test3");
    }
}
