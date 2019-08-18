package com.wyj.apps.nevermore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created
 * Author: wyj
 * Date: 2019/8/14
 */

@SpringBootApplication
public class NevermoreApplication {

    public static final String APP_URL_PREFIX = "/nvm";

    public static void main(String[] args) {
        SpringApplication.run(NevermoreApplication.class, args);
    }
}
