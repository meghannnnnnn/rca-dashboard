package com.example.rcadashboard.service;

import com.example.rcadashboard.model.Alarm;
import com.example.rcadashboard.repository.AlarmRepository;
import com.example.rcadashboard.ws.WebSocketBroadcastHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.List;
import java.util.HashMap;

@Service
public class KafkaConsumerService {

    private final AlarmRepository repo;
    private final ObjectMapper mapper = new ObjectMapper();
    private final WebSocketBroadcastHandler broadcaster;
    private final SummaryService summaryService;

    public KafkaConsumerService(AlarmRepository repo, WebSocketBroadcastHandler broadcaster, SummaryService summaryService) {
        this.repo = repo;
        this.broadcaster = broadcaster;
        this.summaryService = summaryService;
    }

    @KafkaListener(topics = "alarms", groupId = "rca-group")
    public void listen(String message) {
        System.out.println("📩 Received: " + message);
        try {
            Map<String, Object> m = mapper.readValue(message, new TypeReference<Map<String,Object>>() {});
            String host = (m.get("host") != null) ? m.get("host").toString() : "unknown";
            String service = (m.get("service") != null) ? m.get("service").toString() : "unknown";
            String state = (m.get("state") != null) ? m.get("state").toString() : "unknown";
            String output = (m.get("output") != null) ? m.get("output").toString() : message;

            Alarm alarm = repo.save(new Alarm(host, service, state, output));

            Map<String, Object> out = new HashMap<>();
            out.put("id", alarm.getId());
            out.put("host", alarm.getHost());
            out.put("service", alarm.getService());
            out.put("state", alarm.getState());
            out.put("message", alarm.getMessage());
            out.put("timestamp", alarm.getTimestamp().toString());
            out.put("acknowledged", alarm.isAcknowledged());
            out.put("ackBy", alarm.getAckBy());
            out.put("ackAt", alarm.getAckAt() != null ? alarm.getAckAt().toString() : null);
            out.put("resolved", alarm.isResolved());
            out.put("resolvedAt", alarm.getResolvedAt() != null ? alarm.getResolvedAt().toString() : null);

            String alarmJson = mapper.writeValueAsString(out);
            broadcaster.broadcast(alarmJson);

            // broadcast updated summary after saving
            summaryService.broadcastSummary();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Alarm> all() { return repo.findAll(); }
    public List<Alarm> byHost(String host) { return repo.findByHostContainingIgnoreCase(host); }
    public List<Alarm> byState(String state) { return repo.findByState(state); }

    public long countByState(String state) {
        return repo.findAll().stream().filter(a -> a.getState().equalsIgnoreCase(state)).count();
    }
}
