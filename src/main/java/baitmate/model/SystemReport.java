package baitmate.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class SystemReport {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "report_id", updatable = false, nullable = false)
  private UUID reportId;

  @ManyToOne
  @JoinColumn(name = "generated_by")
  private Admin generatedBy;

  @Column(nullable = false)
  private LocalDateTime generationDate;

  @Enumerated(EnumType.STRING)
  private ReportType reportType;

  private LocalDate periodStart;
  private LocalDate periodEnd;
  private Float systemUptime;
  private Float serverResponseTime;
  private Integer activeUsers;
  private Integer newCatchRecords;
  private Integer hotspotVisits;
  private Integer errorCount;

  @Column(columnDefinition = "TEXT")
  private String reportSummary;

  @Column(length = 255)
  private String reportFilePath;

  private Boolean isArchived = false;

  public enum ReportType {
    PERFORMANCE,
    USER_ACTIVITY,
    ERROR_LOG,
    FISHING_DATA
  }
}
