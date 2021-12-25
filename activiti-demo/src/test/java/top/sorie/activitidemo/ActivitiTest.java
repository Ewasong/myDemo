package top.sorie.activitidemo;

import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.List;
import java.util.zip.ZipInputStream;

public class ActivitiTest {

    @Test
    public void testDeploy() {
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = defaultProcessEngine.getRepositoryService();
        Deployment deployment = repositoryService.createDeployment()
                .name("出差申请-uel")
                .addClasspathResource("bpmn/test1.bpmn20.xml")
                .addClasspathResource("bpmn/test1.png")
                .deploy();
        // 输出部署信息
        System.out.println("deploymentId=" + deployment.getId());
        System.out.println("deploymentName=" + deployment.getName());
    }

    @Test
    public void testDeployByZip() {
        // 创建engine
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        // 获取RepositoryService
        RepositoryService repositoryService = defaultProcessEngine.getRepositoryService();
        // 使用service进行流程部署, 定义一个流程名字, 把bpmn文件和png部署到数据库中。
        InputStream inputStream = this.getClass().getClassLoader()
                .getResourceAsStream("bpmn/evection.zip");
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        Deployment deployment = repositoryService.createDeployment()
                .addZipInputStream(zipInputStream)
                .deploy();
        System.out.println("流程部署id=" + deployment.getId());
        System.out.println("流程部署名字=" + deployment.getName());
    }

    @Test
    public void testStartProcess() {
        // 创建engine
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        // 获取RuntimeService
        RuntimeService runtimeService = defaultProcessEngine.getRuntimeService();
        // 根据id启动流程
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("test1");
        // 输出内容
        System.out.println("流程定义id" + instance.getProcessDefinitionId());
        System.out.println("流程实例id" + instance.getId());
        System.out.println("当前活动的id" + instance.getActivityId());
    }

    @Test
    public void testFindPersonalTaskList() {
        // 创建engine
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        // 获取TaskService
        TaskService taskService = defaultProcessEngine.getTaskService();
        // 根据流程key和任务负责人查询任务
        List<Task> list = taskService.createTaskQuery()
                .processDefinitionKey("test1")
                .taskAssignee("zhangsan")
                .list();
        // 输出
        list.forEach((task) -> {
            System.out.println("流程实例id" + task.getProcessInstanceId());
            System.out.println("任务id" + task.getId());
            System.out.println("任务负责人=" + task.getAssignee());
            System.out.println("任务名称" + task.getName());
        });
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
                .taskAssignee("rose")
                .singleResult();
        System.out.println("流程实例id" + task.getProcessInstanceId());
        System.out.println("任务id" + task.getId());
        System.out.println("任务负责人=" + task.getAssignee());
        System.out.println("任务名称" + task.getName());
        taskService.complete(task.getId());
    }

    @Test
    public void queryProcessDefinition() {
        // 创建engine
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        // RepositoryService
        RepositoryService repositoryService = defaultProcessEngine.getRepositoryService();
        // 获取ProcessDefinitionQuery对象 查询当前所有流程定义
        List<ProcessDefinition> definitions = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("test1")
                .orderByProcessDefinitionVersion()
                .desc()
                .list();
        // 输出信息
        definitions.forEach((p) -> {
            System.out.println("流程定义id：" + p.getId());
            System.out.println("流程定义名称：" + p.getName());
            System.out.println("流程定义key：" + p.getKey());
            System.out.println("流程定义版本：" + p.getVersion());
            System.out.println("流程部署id:" + p.getDeploymentId());
        });
    }
    @Test
    public void deleteDeploymentTest() {
        // 创建engine
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        // RepositoryService
        RepositoryService repositoryService = defaultProcessEngine.getRepositoryService();
        // 通过部署id来删除流程信息
        String deploymentId = "4efc5571-0651-11ec-9ccf-70cf496007a2";
        repositoryService.deleteDeployment(deploymentId, true);
//        repositoryService.deleteDeployment(deploymentId, true);
    }

    /***
     * 下载 资源文件
     * 方案1: 使用activiti的api
     * 方案2: 自己写代码 从数据库中，使用jdbc对blob类型，读取出来，保存到文件目录
     * 解决io操作: commons-io.jar
     * 这里使用方案1
     */
    @Test
    public void testDownloadResource() throws IOException {
        // 创建engine
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        // RepositoryService
        RepositoryService repositoryService = defaultProcessEngine.getRepositoryService();
        // 获取查询对象 ProcessDefinitionQuery，查询定义
        ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery();
        // 通过流程定义信息, 获取部署ID
        ProcessDefinition definition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("test4")
                .singleResult();
        // 通过RepositoryService，传递部署id参数，获取资源信息
        // --获取png图片的流 为啥是null 注意png文件不能有多个点
        // https://github.com/Activiti/Activiti/issues/3714
        System.out.println(definition.getDiagramResourceName());
//        InputStream pngResourceAsStream = repositoryService.getResourceAsStream(
//                definition.getDeploymentId(), definition.getDiagramResourceName());
        // --获取bpmn的流
        System.out.println(definition.getResourceName());
        InputStream bpmnResourceAsStream = repositoryService.getResourceAsStream(
                definition.getDeploymentId(), definition.getResourceName());
        // 构造Outputstream流
//        File pngFile = new File("D:/temp/evectionFlow01.png");
        File bpmn = new File("D:/temp/evectionFlow04.bpmn20.xml");
//        FileOutputStream pngFileOutputSteam = new FileOutputStream(pngFile);
        FileOutputStream bpmnFileOutputSteam = new FileOutputStream(bpmn);

        // 输入流，输出流的转换
//        IOUtils.copy(pngResourceAsStream, pngFileOutputSteam);
        IOUtils.copy(bpmnResourceAsStream, bpmnFileOutputSteam);
        // 关闭流
//        IOUtils.close(pngResourceAsStream);
        IOUtils.close(bpmnResourceAsStream);
//        IOUtils.close(pngFileOutputSteam);
        IOUtils.close(bpmnFileOutputSteam);
    }

    @Test
    public void queryHistoryInfo() {
        // 创建engine
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        // HistoryService
        HistoryService historyService = defaultProcessEngine.getHistoryService();
        HistoricActivityInstanceQuery historicActivityInstanceQuery =
                historyService.createHistoricActivityInstanceQuery();
        // 查询
        List<HistoricActivityInstance> list = historicActivityInstanceQuery.processInstanceId("15001").list();
        list.forEach((p) -> {
            System.out.println(p.getActivityId());
            System.out.println(p.getActivityName());
            System.out.println(p.getProcessDefinitionId());
            System.out.println(p.getProcessInstanceId());
            System.out.println("============================");
        });
    }
}
