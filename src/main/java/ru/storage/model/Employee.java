package ru.storage.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Entity
@Table(name = "employees")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee extends Person {
    @Enumerated(EnumType.STRING)
    private PostEmployee postEmployee; /* должность */
    private Date dateStart;
    private Date dateEnd;
    private Boolean isFullTime;
}
