package com.example.rcadashboard.controller;

import com.example.rcadashboard.model.Alarm;
import com.example.rcadashboard.service.KafkaConsumerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import java.util.*;

@Controller
public class DashboardController {
    private final KafkaConsumerService service;
    public DashboardController(KafkaConsumerService service) { this.service = service; }

    @GetMapping("/")
    public String home(@RequestParam(required = false) String host,
                       @RequestParam(required = false) String state,
                       Model model) {
        List<Alarm> alarms;
        
        // Filtering logic based on request parameters
        if (host != null && !host.isEmpty()) alarms = service.byHost(host);
        else if (state != null && !state.isEmpty()) alarms = service.byState(state);
        else alarms = service.all();

        model.addAttribute("alarms", alarms);
        
        // Add summary counts to the model
        model.addAttribute("criticalCount", service.countByState("CRITICAL"));
        model.addAttribute("warningCount", service.countByState("WARNING"));
        model.addAttribute("okCount", service.countByState("OK"));
        return "index"; // Renders src/main/resources/templates/index.html
    }

    // JSON API: Returns all alarms as raw JSON
    @ResponseBody
    @GetMapping("/api/alarms")
    public List<Alarm> apiAll() { return service.all(); }

    // JSON API: Returns the summary counts
    @ResponseBody
    @GetMapping("/api/summary")
    public Map<String, Long> apiSummary() {
        Map<String, Long> map = new HashMap<>();
        map.put("CRITICAL", service.countByState("CRITICAL"));
        map.put("WARNING", service.countByState("WARNING"));
        map.put("OK", service.countByState("OK"));
        return map;
    }
}
