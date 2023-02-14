package com.example.daysdemo.service.impl;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.example.daysdemo.mapper.DaysDemoMapper;
import com.example.daysdemo.service.DaysDemoService;
import com.example.daysdemo.utils.OkHttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

/**
 * @Author taochen
 * @Date 2023/2/13 15:55
 * @Version 1.0
 */
@Service
@Slf4j
public class DaysDemoServiceImpl implements DaysDemoService {

    @Autowired
    private DaysDemoMapper daysDemoMapper;

    private static final String url = "http://v.juhe.cn/calendar/day";

    private static final String key = "bd4b37cc54ab0ca98ea851039bcc0d14";

    public Date selectMinDays() {
        return daysDemoMapper.selectMinDays();
    }

    @Override
    public String daysApi(String day) {
        log.info("day{}", day.toString());
        String backData = OkHttpUtils.builder().addParam("date", day.toString()).addParam("key", key).url(url).post(false).sync();
        String result = JSON.parseObject(backData).getString("result");
        log.info("result{}",result);
        Integer code  = JSON.parseObject(backData).getInteger("error_code");
        if(code.equals(0)){
            String data = JSON.parseObject(result).getString("data");
            log.info("data{}",data);
            String date = JSON.parseObject(data).getString("date");
            log.info("date{}",date);
            String desc = JSON.parseObject(data).getString("desc");
            log.info("desc{}",desc);
            String weekday = JSON.parseObject(data).getString("weekday");
            log.info("weekday{}",weekday);
            int isHoliday = 0;
            if (!desc.equals("")) {
                isHoliday = 1;
            }
            daysDemoMapper.add(IdUtil.getSnowflakeNextId(), date, isHoliday, weekday);
            return JSON.parseObject(backData).getString("reason");
        }else {
            return JSON.parseObject(backData).getString("reason");
        }

    }
}
