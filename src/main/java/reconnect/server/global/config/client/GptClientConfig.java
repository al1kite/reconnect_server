package reconnect.server.global.config.client;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GptClientConfig {

    @Value("${openai.api.key}")
    private String apiToken;

    @Bean(name = "gptRequestInterceptor")
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("Authorization", "Bearer " + apiToken);
            requestTemplate.header("Content-Type", "application/json");
        };
    }
}

