package top.sorie.activitidemo;

import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class ActivitiTestVariables {

    @Test
    public void testDeploy() {
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = defaultProcessEngine.getRepositoryService();
        Deployment deployment = repositoryService.createDeployment()
                .name("出差申请-variable")
                .addClasspathResource("bpmn/test4.bpmn20.xml")
                .deploy();
        // 输出部署信息
        System.out.println("deploymentId=" + deployment.getId());
        System.out.println("deploymentName=" + deployment.getName());
    }

    @Test
    public void testStartProcessSetVariables() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        // 启动流程实例
        Map<String, Object> uelParam = new HashMap<>();
        uelParam.put("days", 3);
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("test4", uelParam);
    }

    @Test
    public void testCompleteTask() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 创建engine
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        // 获取TaskService
        TaskService taskService = defaultProcessEngine.getTaskService();
        // 根据id完成任务
//        taskService.complete("2505");
        Task task = taskService.createTaskQuery()
                .processDefinitionKey("test4")
                .taskAssignee("王五")
                .singleResult();
        if (task != null) {
            System.out.println("流程实例id" + task.getProcessInstanceId());
            System.out.println("任务id" + task.getId());
            System.out.println("任务负责人=" + task.getAssignee());
            System.out.println("任务名称" + task.getName());
            taskService.complete(task.getId());
        }
    }

    @Test
    public void testCompleteTaskSetVar() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 创建engine
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        // 获取TaskService
        TaskService taskService = defaultProcessEngine.getTaskService();
        // 根据id完成任务
//        taskService.complete("2505");
        Task task = taskService.createTaskQuery()
                .processDefinitionKey("test4")
                .taskAssignee("王五")
                .singleResult();
        if (task != null) {
            System.out.println("流程实例id" + task.getProcessInstanceId());
            System.out.println("任务id" + task.getId());
            System.out.println("任务负责人=" + task.getAssignee());
            System.out.println("任务名称" + task.getName());
            Map<String, Object> uelParam = new HashMap<>();
            uelParam.put("days", 3);
            taskService.complete(task.getId(), uelParam);
        }
    }

    @Test
    public void setGlobalVariableByExecutionId() {
        String executionId = "40002";
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        runtimeService.setVariable(executionId, "days", 3);
        runtimeService.setVariables(executionId, new HashMap<>());
//        runtimeService.setVariableLocal();
    }
    @Test
    public void setGlobalVariableByTaskId() {
        String taskId = "40005";
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        taskService.setVariable(taskId, "days", 3);
        taskService.setVariables(taskId, new HashMap<>());
    }

    @Test
    public void setLocalVariableByTaskId() {
        String taskId = "40005";
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        taskService.setVariableLocal(taskId, "days", 3);
        taskService.setVariablesLocal(taskId, new HashMap<>());
    }
}
