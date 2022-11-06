package com.eticket.domain.entity.quartz;

import com.eticket.domain.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "schedule_job")
@Builder
public class ScheduleJob extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "object_id")
    private Integer objectId;
    @Column(name = "job_key")
    private String jobKey;
    @Column(name = "type")
    private String type;
    @Column(name = "removed")
    private boolean removed;
}
