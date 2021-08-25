package top.sorie.activitidemo;

import org.activiti.engine.*;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;

public class ActivitiBusinessDemo {

    @Test
    public void addBusinessKey() {
        // 创建engine
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        // 获取RuntimeService
        RuntimeService runtimeService = defaultProcessEngine.getRuntimeService();
        // 根据id启动流程
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("test1", "1001");
        // 输出内容
        System.out.println("流程定义id" + instance.getProcessDefinitionId());
        System.out.println("流程实例id" + instance.getId());
        System.out.println("当前活动的id" + instance.getActivityId());
        System.out.println("business key=" + instance.getBusinessKey());
    }

    @Test
    public void suspendAllProcessInstance() {
        // 创建engine
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        // 获取RepositoryService
        RepositoryService repositoryService = defaultProcessEngine.getRepositoryService();
        // 查询流程定义
        ProcessDefinition test1 = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("test1")
                .singleResult();
        // 获取当前流程实例是否都是挂起状态
        boolean suspended = test1.isSuspended();
        String id = test1.getId();
        // 如果是挂起，就激活
        // 如果是正常，改为挂起
        if (suspended) {
            // 参数1: 流程定义id 参数2: 是否激活 参数3 激活时间
            repositoryService.activateProcessDefinitionById(id, true, null);
            System.out.println("激活");
        } else {
            // 参数1: 流程定义id 参数2: 是否暂停 参数3 暂停时间
            repositoryService.suspendProcessDefinitionById(id, true, null);
            System.out.println("挂起");
        }
    }

    @Test
    public void suspendProcessInstance() {
        // 创建engine
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        // 获取RuntimeService
        RuntimeService runtimeService = defaultProcessEngine.getRuntimeService();
        // 获取实例对象
        ProcessInstance instance = runtimeService.createProcessInstanceQuery()
                .processInstanceId("17501")
                .singleResult();
        // 切换状态
        if (instance.isSuspended()) {
            runtimeService.activateProcessInstanceById(instance.getId());
            System.out.println("激活");
        } else {
            runtimeService.suspendProcessInstanceById(instance.getId());
            System.out.println("挂起");
        }
    }

    @Test
    public void completeTask() {
        // 创建engine
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        // 获取TaskService
        TaskService taskService = defaultProcessEngine.getTaskService();
        // 根据id完成任务
//        taskService.complete("2505");
        Task task = taskService.createTaskQuery()
                .processDefinitionKey("test1")
                .taskAssignee("jerry")
                .processInstanceBusinessKey("1001")
                .singleResult();

        System.out.println("流程实例id" + task.getProcessInstanceId());
        System.out.println("任务id" + task.getId());
        System.out.println("任务负责人=" + task.getAssignee());
        System.out.println("任务名称" + task.getName());
        taskService.complete(task.getId());
    }
}
