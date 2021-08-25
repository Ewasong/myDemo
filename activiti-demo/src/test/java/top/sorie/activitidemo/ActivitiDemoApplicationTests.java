package top.sorie.activitidemo;

import org.activiti.engine.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

class ActivitiDemoApplicationTests {
    /***
     * 默认创建的方式来创建表
     */
    @Test
    public void testCreateDbTable() {
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();

    }

    @Test
    public void testCommnonEngine() {
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource(
                "activiti.cfg.xml",
                "processEngeneConfiguration"
        );
        ProcessEngine engine = configuration.buildProcessEngine();
        RuntimeService runtimeService = engine.getRuntimeService();
        RepositoryService repositoryService = engine.getRepositoryService();
        TaskService taskService = engine.getTaskService();
    }
}
