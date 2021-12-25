package top.sorie.activitidemo;

import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivitiTestGroupTask {
    private String testKey = "test5";
    @Test
    public void testDeploy() {
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = defaultProcessEngine.getRepositoryService();
        Deployment deployment = repositoryService.createDeployment()
                .name("出差申请-group")
                .addClasspathResource("bpmn/test5.bpmn20.xml")
                .deploy();
        // 输出部署信息
        System.out.println("deploymentId=" + deployment.getId());
        System.out.println("deploymentName=" + deployment.getName());
    }

    @Test
    public void testFindGroupTaskList() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        String candicateUser = "王五";
        // 启动流程实例

        List<Task> list = taskService.createTaskQuery()
                .processDefinitionKey(testKey)
                .taskCandidateUser(candicateUser)
                .list();
        for (Task task : list) {
            System.out.println("id:" + task.getId());
            System.out.println("负责人" + task.getAssignee());
        }
    }

    @Test
    public void testClaimTask() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        String candicateUser = "王五";
        String taskId = "40005";
        // 启动流程实例
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .taskCandidateUser(candicateUser)
                .singleResult();
        if (task != null) {
            taskService.claim(taskId, candicateUser);
            System.out.println("拾取");
        }
    }

    @Test
    public void tesReturnTask() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        String assigne = "王五";
        String taskId = "40005";
        // 启动流程实例
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .taskAssignee(assigne)
                .singleResult();
        if (task != null) {
//            taskService.setAssignee(taskId, null);
            taskService.unclaim(taskId);
            System.out.println("归还");
        }
    }
}
