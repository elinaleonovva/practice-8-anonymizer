package ru.mirea.prks.practice8.export.config;

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
import ru.mirea.prks.practice8.export.event.ExportReadyEvent;
import ru.mirea.prks.practice8.export.event.ExportRequestedEvent;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitTopologyConfig {
    public static final String EXCHANGE = "anonymization.events";
    public static final String READY_QUEUE = "export-service.export-ready";
    public static final String REQUESTED_QUEUE = "anonymizer-service.export-requested";

    @Bean TopicExchange anonymizationExchange() { return new TopicExchange(EXCHANGE, true, false); }
    @Bean Queue exportReadyQueue() { return new Queue(READY_QUEUE, true); }
    // The publisher declares the queue too, so an event is retained if its consumer is still starting.
    @Bean Queue exportRequestedQueue() { return new Queue(REQUESTED_QUEUE, true); }
    @Bean Binding exportReadyBinding(@Qualifier("exportReadyQueue") Queue exportReadyQueue, TopicExchange anonymizationExchange) {
        return BindingBuilder.bind(exportReadyQueue).to(anonymizationExchange).with("export.ready");
    }
    @Bean Binding exportRequestedBinding(@Qualifier("exportRequestedQueue") Queue exportRequestedQueue, TopicExchange anonymizationExchange) {
        return BindingBuilder.bind(exportRequestedQueue).to(anonymizationExchange).with("export.requested");
    }
    @Bean MessageConverter messageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        DefaultJackson2JavaTypeMapper mapper = new DefaultJackson2JavaTypeMapper();
        Map<String, Class<?>> types = new HashMap<>();
        // Short type names keep a consumer independent from the publisher's Java package.
        types.put("exportRequested", ExportRequestedEvent.class);
        types.put("exportReady", ExportReadyEvent.class);
        mapper.setIdClassMapping(types);
        converter.setJavaTypeMapper(mapper);
        return converter;
    }
}
