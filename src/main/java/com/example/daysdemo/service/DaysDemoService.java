package com.example.daysdemo.service;

import java.util.Date;

/**
 * @Author taochen
 * @Date 2023/2/13 15:55
 * @Version 1.0
 */
public interface DaysDemoService {
    Date selectMinDays();


    String daysApi(String day);
}
