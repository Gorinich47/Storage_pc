package ru.storage.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Date;
import java.time.LocalDateTime;

@Entity
@Table(name = "prices")
@Data
@AllArgsConstructor
@NoArgsConstructor
/* тарифы на каждый бокс отдельно*/
public class Price {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "box_id", nullable = false, unique = false, updatable = false)
    private Box box;
    private double sumPrice;
    private Date dateStart;
    private Date dateEnd;
    @UpdateTimestamp
    @Column(name = "date_edit", nullable = false)
    private LocalDateTime dateEdit;
}
