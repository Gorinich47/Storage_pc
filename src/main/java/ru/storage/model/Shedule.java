package ru.storage.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "shedules")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Shedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @UpdateTimestamp
    @Column(name = "date_edit", nullable = false)
    private LocalDateTime date;
    private LocalDateTime dateStart;
    private LocalDateTime dateEnd;
    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false, unique = false, updatable = false)
    private Employee employee;
    private Boolean isHoliday;
}
