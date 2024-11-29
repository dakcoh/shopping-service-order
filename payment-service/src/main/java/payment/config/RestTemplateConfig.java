package payment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;

/**
 * RestTemplateConfig 클래스는 RestTemplate Bean을 등록합니다.
 * @LoadBalanced를 사용해 Eureka와 연동합니다.
 */
@Configuration
public class RestTemplateConfig {

    @Bean
    @LoadBalanced // Eureka 사용 시 필요
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
