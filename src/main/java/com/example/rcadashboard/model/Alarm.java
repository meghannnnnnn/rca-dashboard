package com.example.rcadashboard.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Alarm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String host;
    private String service;
    private String state;

    @Column(length = 1024)
    private String message;

    private LocalDateTime timestamp;

    // ack / resolve workflow
    private boolean acknowledged = false;
    private String ackBy;
    private LocalDateTime ackAt;

    private boolean resolved = false;
    private LocalDateTime resolvedAt;

    public Alarm() {
        this.timestamp = LocalDateTime.now();
    }

    public Alarm(String host, String service, String state, String message) {
        this.host = host;
        this.service = service;
        this.state = state;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    // getters and setters
    public Long getId() { return id; }
    public String getHost() { return host; }
    public String getService() { return service; }
    public String getState() { return state; }
    public String getMessage() { return message; }
    public LocalDateTime getTimestamp() { return timestamp; }

    public boolean isAcknowledged() { return acknowledged; }
    public String getAckBy() { return ackBy; }
    public LocalDateTime getAckAt() { return ackAt; }

    public boolean isResolved() { return resolved; }
    public LocalDateTime getResolvedAt() { return resolvedAt; }

    public void setId(Long id) { this.id = id; }
    public void setHost(String host) { this.host = host; }
    public void setService(String service) { this.service = service; }
    public void setState(String state) { this.state = state; }
    public void setMessage(String message) { this.message = message; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public void setAcknowledged(boolean acknowledged) { this.acknowledged = acknowledged; }
    public void setAckBy(String ackBy) { this.ackBy = ackBy; }
    public void setAckAt(LocalDateTime ackAt) { this.ackAt = ackAt; }

    public void setResolved(boolean resolved) { this.resolved = resolved; }
    public void setResolvedAt(LocalDateTime resolvedAt) { this.resolvedAt = resolvedAt; }
}
