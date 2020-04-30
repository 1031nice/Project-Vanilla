package me.donghun.vanilla;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
 * 아래의 annotation 하나를 붙임으로써
 * 이 클래스는 Configuration 클래스의 역할도 한다.
 */

@SpringBootApplication
public class VanillaApplication {

    public static void main(String[] args) {

        SpringApplication.run(VanillaApplication.class, args);

    }

}
