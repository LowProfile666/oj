package com.zsm.ojbackendjudgeservice;

import com.zsm.ojbackendjudgeservice.rabbitmq.MqInitMain;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.zsm.ojbackendserviceclient"})
public class OjBackendJudgeServiceApplication {

    public static void main(String[] args) {
        MqInitMain.main(args);
        SpringApplication.run(OjBackendJudgeServiceApplication.class, args);
    }

}
