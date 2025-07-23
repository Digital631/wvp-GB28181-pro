package com.genersoft.iot.vmp.conf.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
/**
 * @author MysticShadow
 * @date 2025年07月23日 16:30
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "security")
@EnableConfigurationProperties
public class SecurityIgnoreProperties {
    private List<String> ignores;
}
