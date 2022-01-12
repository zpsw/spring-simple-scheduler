package me.zpsw.task;

import me.zpsw.util.SysProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

/**
 * @Author: Zpsw
 * @Date: 2022/1/12
 * @Description:
 * @Version: 1.0
 */
@Component
public class TestTaskSchedulingConfigurer implements SchedulingConfigurer {

    @Autowired
    private TestService testService;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addTriggerTask(() -> {
            testService.test();
        },getTrigger());
    }

    public String getCron() {
        return SysProperties.getInstance().getProperty("test.rate");
    }
    private Trigger getTrigger() {
        return triggerContext -> {
            // 触发器
            CronTrigger trigger = new CronTrigger(getCron());
            return trigger.nextExecutionTime(triggerContext);
        };
    }
}
