package top.sorie.activitidemo.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public class MyTaskListener implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        if ("创建出差申请".equals(delegateTask.getName())) {
            delegateTask.setAssignee("张三");
        }
    }
}
