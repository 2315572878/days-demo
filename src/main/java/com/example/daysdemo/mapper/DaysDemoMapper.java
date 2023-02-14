package com.example.daysdemo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * @Author taochen
 * @Date 2023/2/13 15:55
 * @Version 1.0
 */
@Mapper
public interface DaysDemoMapper {
    Date selectMinDays();

    void add(@Param("snowflakeNextId") Long snowflakeNextId, @Param("date") String date, @Param("isHoliday") Integer isHoliday, @Param("weekday") String weekday);
}
