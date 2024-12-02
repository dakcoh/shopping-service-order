package gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
public class RedisConfig {
    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    // ReactiveRedisTemplate을 생성하는 Bean 정의
    @Bean
    public ReactiveRedisTemplate<String, String> reactiveRedisTemplate(ReactiveRedisConnectionFactory factory) {
        // Redis에서 데이터를 읽고 쓸 때 사용할 직렬화 설정 (문자열 타입)
        RedisSerializationContext<String, String> context = RedisSerializationContext
                .<String, String>newSerializationContext(RedisSerializer.string())  // 문자열 직렬화 설정
                .build();

        // ReactiveRedisTemplate을 반환. 이 템플릿은 비동기식으로 Redis와 상호작용
        return new ReactiveRedisTemplate<>(factory, context);
    }

    // ReactiveRedisConnectionFactory를 생성하는 Bean 정의
    @Bean
    public ReactiveRedisConnectionFactory reactiveRedisConnectionFactory() {
        // RedisStandaloneConfiguration을 사용하여 Redis 서버와의 연결을 설정
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();

        // 주입받은 Redis 호스트와 포트를 설정
        configuration.setHostName(redisHost);  // Redis 서버 호스트
        configuration.setPort(redisPort);      // Redis 서버 포트

        // LettuceConnectionFactory는 비동기식 Redis 클라이언트인 Lettuce를 사용하여 연결을 설정
        return new LettuceConnectionFactory(configuration);  // Lettuce 연결 팩토리 반환
    }
}
