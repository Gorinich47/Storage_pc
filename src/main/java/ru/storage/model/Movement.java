package ru.storage.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "movements")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Movement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @CreationTimestamp
    @Column(name = "date", nullable = false, updatable = false)
    private LocalDateTime date; /* дата вермя движения */
    @Enumerated(EnumType.STRING)
    private TypeMovements TypeMovements; /* тип операции */ /* обычно тут будет приход, но при перерасчёте может быть и расход*/
    private double summ; /* сумма */

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false, updatable = false)
    private Account account; /* счет */
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false, updatable = false)
    private Client client; /* клиент */
    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false, updatable = false)
    private Employee employee; /* сотрудник проводивший операцию */

}
