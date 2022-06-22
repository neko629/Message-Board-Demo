package com.laorunzi.msgboard;

import com.laorunzi.msgboard.model.MbUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootApplication
public class MsgboardApplication {

    @Value("${access-control-allow-origin}")
    private String origin;

    public static void main(String[] args) {
        SpringApplication.run(MsgboardApplication.class, args);
    }

    /**
     * 请求跨域处理
     * @return
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(origin)
                        .allowedMethods("PUT", "DELETE","GET","POST")
                        .allowedHeaders("*")
                        .exposedHeaders("access-control-allow-headers","access-control-allow-methods","access-control-allow-origin", "access-control-max-age","X-Frame-Options")
                        .allowCredentials(false).maxAge(3600);
            }
        };
    }

}
