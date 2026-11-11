package com.famicare.FamicareBackend.alerts.domain.model.aggregates;

import com.famicare.FamicareBackend.alerts.domain.model.commands.CreateAlertCommand;
import com.famicare.FamicareBackend.alerts.domain.model.valueobjects.AlertCategory;
import com.famicare.FamicareBackend.alerts.domain.model.valueobjects.AlertStatus;
import com.famicare.FamicareBackend.alerts.domain.model.valueobjects.AlertType;
import com.famicare.FamicareBackend.alerts.domain.model.valueobjects.ElderlyProfileId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "alerts")
@EntityListeners(AuditingEntityListener.class)
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private ElderlyProfileId elderlyProfileId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AlertType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AlertCategory category;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AlertStatus status;

    @Column(length = 150)
    private String attendedBy;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime resolvedAt;

    public Alert(CreateAlertCommand command) {
        this.elderlyProfileId = command.elderlyProfileId();
        this.type = command.type();
        this.category = command.category();
        this.title = command.title();
        this.description = command.description();
        this.status = AlertStatus.PENDING;
        this.attendedBy = null;
        this.createdAt = LocalDateTime.now();
    }

    public void markAsAttended(String attendedBy) {
        if (this.status == AlertStatus.RESOLVED) {
            throw new IllegalStateException("Cannot attend an resolved alert");
        }
        this.status = AlertStatus.ATTENDED;
        this.attendedBy = attendedBy;
    }

    public void markAsResolved() {
        this.status = AlertStatus.RESOLVED;
        this.resolvedAt = LocalDateTime.now();
    }
}