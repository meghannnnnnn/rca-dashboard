package com.example.rcadashboard.service;

import com.example.rcadashboard.repository.AlarmRepository;
import com.example.rcadashboard.ws.WebSocketBroadcastHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SummaryService {

    private final AlarmRepository repo;
    private final WebSocketBroadcastHandler broadcaster;
    private final ObjectMapper mapper = new ObjectMapper();

    public SummaryService(AlarmRepository repo, WebSocketBroadcastHandler broadcaster) {
        this.repo = repo;
        this.broadcaster = broadcaster;
    }

    public void broadcastSummary() {
        long critical = repo.findAll().stream()
                .filter(a -> !a.isResolved())
                .filter(a -> "CRITICAL".equalsIgnoreCase(a.getState()))
                .count();
        long warning = repo.findAll().stream()
                .filter(a -> !a.isResolved())
                .filter(a -> "WARNING".equalsIgnoreCase(a.getState()))
                .count();
        long ok = repo.findAll().stream()
                .filter(a -> !a.isResolved())
                .filter(a -> "OK".equalsIgnoreCase(a.getState()))
                .count();

        Map<String, Object> m = new HashMap<>();
        m.put("type", "summary");
        m.put("CRITICAL", critical);
        m.put("WARNING", warning);
        m.put("OK", ok);

        try {
            String json = mapper.writeValueAsString(m);
            broadcaster.broadcast(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
