package top.sorie.activitibootdemo;

import org.activiti.api.process.model.ProcessDefinition;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.task.model.Task;
import org.activiti.api.task.model.builders.TaskPayloadBuilder;
import org.activiti.api.task.runtime.TaskRuntime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.sorie.activitibootdemo.utils.SecurityUtil;

import java.util.List;

@SpringBootTest
class ActivitiBootDemoApplicationTests {

    @Autowired
    private ProcessRuntime processRuntime;

    @Autowired
    private TaskRuntime taskRuntime;

    @Autowired
    private SecurityUtil securityUtil;
    /**
     * 前提activiti7可以自动部署流程
     */
    @Test
    public void findProcess() {
        securityUtil.logInAs("jack");
        Page<ProcessDefinition> processDefinitions =
                processRuntime.
                        processDefinitions(Pageable.of(0, 100));
        System.out.println(processDefinitions.getTotalItems());
        System.out.println(processDefinitions.getContent());
    }

    @Test
    public void startProcessRuntime() {
        securityUtil.logInAs("system");
        ProcessInstance test1 = processRuntime
                .start(ProcessPayloadBuilder.start()
                        .withProcessDefinitionKey("test1").build());
    }

    @Test
    public void doTask() {
        securityUtil.logInAs("zhangsan");
        // 查询
        Page<Task> taskPage = taskRuntime.tasks(Pageable.of(0, 10));
        // 拾取
        for (Task task : taskPage.getContent()) {
            taskRuntime.claim(TaskPayloadBuilder
                .claim()
                .withTaskId(task.getId())
                .build());
            // 完成
            taskRuntime.complete(TaskPayloadBuilder
                    .complete()
                    .withTaskId(task.getId())
                    .build());
        }
    }
}
