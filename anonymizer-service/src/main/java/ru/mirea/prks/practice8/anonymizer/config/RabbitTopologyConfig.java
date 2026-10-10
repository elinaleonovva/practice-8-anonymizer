package ru.mirea.prks.practice8.anonymizer.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Qualifier;
import ru.mirea.prks.practice8.anonymizer.event.ExportReadyEvent;
import ru.mirea.prks.practice8.anonymizer.event.ExportRequestedEvent;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitTopologyConfig {
    public static final String EXCHANGE = "anonymization.events";
    public static final String REQUESTED_QUEUE = "anonymizer-service.export-requested";
    public static final String READY_QUEUE = "export-service.export-ready";

    @Bean TopicExchange anonymizationExchange() { return new TopicExchange(EXCHANGE, true, false); }
    @Bean Queue exportRequestedQueue() { return new Queue(REQUESTED_QUEUE, true); }
    // The result queue is also declared immediately, regardless of service startup order.
    @Bean Queue exportReadyQueue() { return new Queue(READY_QUEUE, true); }
    @Bean Binding exportRequestedBinding(@Qualifier("exportRequestedQueue") Queue exportRequestedQueue, TopicExchange anonymizationExchange) {
        return BindingBuilder.bind(exportRequestedQueue).to(anonymizationExchange).with("export.requested");
    }
    @Bean Binding exportReadyBinding(@Qualifier("exportReadyQueue") Queue exportReadyQueue, TopicExchange anonymizationExchange) {
        return BindingBuilder.bind(exportReadyQueue).to(anonymizationExchange).with("export.ready");
    }
    @Bean MessageConverter messageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        DefaultJackson2JavaTypeMapper mapper = new DefaultJackson2JavaTypeMapper();
        Map<String, Class<?>> types = new HashMap<>();
        types.put("exportRequested", ExportRequestedEvent.class);
        types.put("exportReady", ExportReadyEvent.class);
        mapper.setIdClassMapping(types);
        converter.setJavaTypeMapper(mapper);
        return converter;
    }
}
