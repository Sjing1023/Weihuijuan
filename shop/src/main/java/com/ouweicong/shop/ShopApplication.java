package com.ouweicong.shop;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@MapperScan("com.ouweicong.shop.dao")
@SpringBootApplication
@EnableScheduling // 启用定时任务
public class ShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopApplication.class, args);
    }

    @Component
    public static class ScheduledTasks {
        @Autowired
        private JdbcTemplate jdbcTemplate;
        @Scheduled(fixedRate = 30000) // 每5秒执行一次
        public void reportCurrentTime() {
            String sql = "SELECT 1"; // 你的SQL查询
            // 如果你知道查询会返回一行一列，你可以使用queryForObject
            Integer result = jdbcTemplate.queryForObject(sql, Integer.class);
        }
    }
}
