package com.example.rcadashboard.ws;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple broadcast handler that keeps a set of sessions and broadcasts text messages to all connected clients.
 */
@Component
public class WebSocketBroadcastHandler extends TextWebSocketHandler {

    // thread-safe set of sessions
    private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
        sessions.remove(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // We don't expect clients to send messages, but this safely echoes or ignores.
        // Optionally implement client -> server features here.
    }

    /**
     * Broadcast a raw text message to all connected sessions (non-blocking best-effort).
     */
    public void broadcast(String payload) {
        TextMessage msg = new TextMessage(payload);
        for (WebSocketSession s : sessions) {
            try {
                if (s.isOpen()) s.sendMessage(msg);
            } catch (Exception e) {
                // remove bad sessions
                try { s.close(); } catch (Exception ignored) {}
                sessions.remove(s);
            }
        }
    }

    // helper for tests / counts
    public int sessionCount() {
        return sessions.size();
    }
}
