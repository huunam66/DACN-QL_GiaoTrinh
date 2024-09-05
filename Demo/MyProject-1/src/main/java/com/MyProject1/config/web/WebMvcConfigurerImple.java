package com.MyProject1.config.web;

import com.MyProject1.config.support.Maker;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfigurerImple implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String path = Maker.PATH_SOURCE() + "\\src\\main\\upload\\static\\pictures\\";
        registry.addResourceHandler("/pictures/**").addResourceLocations("file:///" + path);
        WebMvcConfigurer.super.addResourceHandlers(registry);
    }
}
