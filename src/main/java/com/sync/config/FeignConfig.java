package com.sync.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import feign.Logger;
import feign.Request;
import feign.codec.Decoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import okhttp3.ConnectionPool;
import okhttp3.Protocol;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Configuration
public class FeignConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
    }

    @Bean
    public Decoder feignDecoder(ObjectMapper objectMapper) {
        return new JacksonDecoder(objectMapper);
    }

    @Bean
    public Encoder feignEncoder(ObjectMapper objectMapper) {
        return new SpringFormEncoder(new JacksonEncoder(objectMapper));
    }

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public Request.Options requestOptions(XubioProperties xubioProperties) {
        return new Request.Options(
                xubioProperties.getConnectTimeout(), TimeUnit.MILLISECONDS,
                xubioProperties.getReadTimeout(), TimeUnit.MILLISECONDS,
                true
        );
    }

    /**
     * Fuerza HTTP/1.1 y limita el pool de conexiones.
     * La API de Xubio queda detrás de un AWS ALB que no cierra correctamente
     * los streams HTTP/2: una vez que un stream se cuelga, OkHttp sigue
     * reusando esa misma conexión multiplexada para requests posteriores
     * (incluso a otros endpoints), y todas cuelgan hasta el read-timeout.
     * Forzando HTTP/1.1 cada request usa su propia conexión.
     */
    @Bean
    public okhttp3.OkHttpClient okHttpClient(XubioProperties xubioProperties) {
        return new okhttp3.OkHttpClient.Builder()
                .protocols(Collections.singletonList(Protocol.HTTP_1_1))
                .connectTimeout(xubioProperties.getConnectTimeout(), TimeUnit.MILLISECONDS)
                .readTimeout(xubioProperties.getReadTimeout(), TimeUnit.MILLISECONDS)
                .callTimeout(xubioProperties.getReadTimeout() + xubioProperties.getConnectTimeout(), TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true)
                .connectionPool(new ConnectionPool(5, 30, TimeUnit.SECONDS))
                .build();
    }

    /**
     * Bean explícito de feign.Client envolviendo el OkHttpClient de arriba.
     * Sin este bean, Spring Cloud OpenFeign no garantiza usar el
     * okhttp3.OkHttpClient definido acá: puede terminar resolviendo
     * feign.Client.Default (java.net.HttpURLConnection), que no tiene el
     * fix de HTTP/1.1 ni el callTimeout, y por eso seguía colgándose 300s.
     */
    @Bean
    public feign.Client feignClient(okhttp3.OkHttpClient okHttpClient) {
        return new feign.okhttp.OkHttpClient(okHttpClient);
    }

    @Bean
    public feign.RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("Accept", "application/json");
            requestTemplate.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
        };
    }
}
