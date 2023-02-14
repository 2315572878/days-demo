package com.example.daysdemo.controller;


import cn.hutool.core.date.DateUtil;
import com.example.daysdemo.service.DaysDemoService;
import com.example.daysdemo.utils.CustomTaskScheduler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.ScheduledMethodRunnable;
import org.springframework.util.ReflectionUtils;

import java.util.Date;
import java.util.Objects;

/**
 * @Author taochen
 * @Date 2023/2/13 15:50
 * @Version 1.0
 */
@Slf4j
@Configuration
public class DaysDemoController {
    @Autowired
    private DaysDemoService daysDemoService;

    @Autowired
    private CustomTaskScheduler customTaskScheduler;

    //如果数据库内无值则从此值开始计算
    private String str = "2023-12-32";

    private int count;

    //每天十点执行,执行五十次后结束
    @Scheduled(cron = "* * 10 * * ?")
    private void process() {
        log.info("任务执行次数：{}", count + 1);
        count++;
        // 执行50次后自动停止
        if (count >= 50) {
            log.info("任务已执行指定次数，现在自动停止");
            boolean cancelled = customTaskScheduler.getScheduledTasks().get(this).cancel(true);
            if (cancelled) {
                count = 0;
                ScheduledMethodRunnable runnable = new ScheduledMethodRunnable(this, ReflectionUtils.findMethod(this.getClass(), "printTask"));
                customTaskScheduler.schedule(runnable, new CronTrigger("* * 10 * * ?"));
            }
        } else {
            Date days = daysDemoService.selectMinDays();
            if (Objects.isNull(days)) {
                days = DateUtil.parse(str);
            }
            String msg = daysDemoService.daysApi(DateUtil.formatDate(DateUtil.offsetDay(days, -1)));
            log.info("msg:{}", msg);
        }
    }

}
