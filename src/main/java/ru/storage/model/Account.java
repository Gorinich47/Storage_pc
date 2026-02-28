package ru.storage.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "accounts")
@Data
@AllArgsConstructor
@NoArgsConstructor
/* Движения, состояния боксов*/
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @CreationTimestamp
    @Column(name = "date", nullable = false, updatable = false)
    private LocalDateTime date; /* Дата создания счета */
    private Date dateStart; /* Дата открытия счета */
    private Date dateEnd; /* дата закрытие */
    //    @OneToMany
//    @JoinColumn(name="box_id", nullable = false, updatable = false)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "account_box",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "box_id")
    )
    private List<Box> box; /* бокс */
    @Enumerated(EnumType.STRING)
    private StatusBox statusBox; /* статус */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id", nullable = false, updatable = false)
    private Client client;
    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false, updatable = false)
    private Employee employee;
    private double sumAmount; /* сумма счета*/

}
