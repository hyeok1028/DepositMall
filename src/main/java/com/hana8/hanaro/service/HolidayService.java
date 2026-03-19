package com.hana8.hanaro.service;

import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Service
public class HolidayService {

    /**
     * 주말(토, 일)인 경우 다음 영업일(월요일)로 이동 처리합니다.
     * 실제 서비스에서는 공휴일 데이터 연동이 필요하나, 과제 요건에 따라 최소한의 토/일 처리를 구현합니다.
     */
    public LocalDate toNextBusinessDay(LocalDate date) {
        if (date == null) {
            return null;
        }
        
        LocalDate result = date;
        while (isHoliday(result)) {
            result = result.plusDays(1);
        }
        return result;
    }

    public boolean isHoliday(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }
}
