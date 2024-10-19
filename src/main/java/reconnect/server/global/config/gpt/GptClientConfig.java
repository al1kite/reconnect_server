package reconnect.server.global.config.gpt;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GptClientConfig {

    @Value("${openai.api.key}")
    private String apiToken;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("Authorization", "Bearer " + apiToken);
            requestTemplate.header("Content-Type", "application/json");
        };
    }
}

