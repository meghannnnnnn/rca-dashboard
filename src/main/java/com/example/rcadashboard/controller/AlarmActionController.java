package com.example.rcadashboard.controller;

import com.example.rcadashboard.model.Alarm;
import com.example.rcadashboard.repository.AlarmRepository;
import com.example.rcadashboard.ws.WebSocketBroadcastHandler;
import com.example.rcadashboard.service.SummaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/alarms")
public class AlarmActionController {

    private final AlarmRepository repo;
    private final WebSocketBroadcastHandler broadcaster;
    private final SummaryService summaryService;

    public AlarmActionController(AlarmRepository repo, WebSocketBroadcastHandler broadcaster, SummaryService summaryService) {
        this.repo = repo;
        this.broadcaster = broadcaster;
        this.summaryService = summaryService;
    }

    @PostMapping("/{id}/ack")
    public ResponseEntity<?> acknowledge(@PathVariable Long id, @RequestBody(required = false) Map<String,String> body) {
        Optional<Alarm> opt = repo.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();

        Alarm a = opt.get();
        a.setAcknowledged(true);
        String user = (body != null && body.get("user") != null) ? body.get("user") : "unknown";
        a.setAckBy(user);
        a.setAckAt(LocalDateTime.now());
        repo.save(a);

        Map<String,Object> out = new HashMap<>();
        out.put("type","ack");
        out.put("id", a.getId());
        out.put("ackBy", a.getAckBy());
        out.put("ackAt", a.getAckAt().toString());
        out.put("acknowledged", true);
        try { broadcaster.broadcast(new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(out)); } catch(Exception e){}

        // broadcast updated summary
        summaryService.broadcastSummary();

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/resolve")
    public ResponseEntity<?> resolve(@PathVariable Long id) {
        Optional<Alarm> opt = repo.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();

        Alarm a = opt.get();
        a.setResolved(true);
        a.setResolvedAt(LocalDateTime.now());
        repo.save(a);

        Map<String,Object> out = new HashMap<>();
        out.put("type","resolve");
        out.put("id", a.getId());
        out.put("resolvedAt", a.getResolvedAt().toString());
        out.put("resolved", true);
        try { broadcaster.broadcast(new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(out)); } catch(Exception e){}

        // broadcast updated summary
        summaryService.broadcastSummary();

        return ResponseEntity.ok().build();
    }
}
