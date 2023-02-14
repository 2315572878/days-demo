package com.example.daysdemo.controller;


import cn.hutool.core.date.DateUtil;
import com.example.daysdemo.service.DaysDemoService;
import com.example.daysdemo.utils.CustomTaskScheduler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
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


    //如果数据库内无值则从此值开始计算
    private String str = "2023-12-32";


    private int count;

    //每天十点执行,执行五十次后结束
    @Scheduled(cron = "* * 10,11 * * ?")
    private void process() throws Exception {
        log.info("任务执行次数：{}", count + 1);
        count++;
        // 执行50次后自动停止
        if (count >= 50) {
            log.info("任务完成");
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
