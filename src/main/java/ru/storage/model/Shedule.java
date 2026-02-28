package ru.storage.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "shedules")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Shedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date date;
    private Date dateStart;
    private Date dateEnd;
    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false, unique = true, updatable = false)
    private Employee employee;
    private boolean isHoliday;
}
