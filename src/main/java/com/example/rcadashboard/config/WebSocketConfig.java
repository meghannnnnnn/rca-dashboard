package com.example.rcadashboard.config;

import com.example.rcadashboard.ws.WebSocketBroadcastHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final WebSocketBroadcastHandler broadcastHandler;

    public WebSocketConfig(WebSocketBroadcastHandler broadcastHandler) {
        this.broadcastHandler = broadcastHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(broadcastHandler, "/ws")
                .setAllowedOriginPatterns("*"); // allow all for local dev; restrict in production
    }
}
