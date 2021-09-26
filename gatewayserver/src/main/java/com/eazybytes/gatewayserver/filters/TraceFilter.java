package com.eazybytes.gatewayserver.filters;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Order(1)
@Component
public class TraceFilter implements GlobalFilter {

    private static final Logger logger = LoggerFactory.getLogger(TraceFilter.class); 

    @Autowired
    private  FilterUtil filterUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        HttpHeaders requestHeaders = exchange.getRequest().getHeaders();
        if(isCorrelationIdPresent(requestHeaders)) {
            logger.debug("EzyBanck-correlation-id found in tracing filter: {}.", filterUtil.getCorrelationId(requestHeaders));
        } else {
            String correlationId = generateCorrelationId();
            exchange = filterUtil.setCorrelationId(exchange, correlationId);
            logger.debug("EzyBanck-correlation-id generated in tracing filter: {}.", correlationId);
        }
        return chain.filter(exchange);
    }    

    private boolean isCorrelationIdPresent(HttpHeaders requestHeaders) {
        return filterUtil.getCorrelationId(requestHeaders) != null;
    }

    private String generateCorrelationId() {
        return UUID.randomUUID().toString();
    }

}
