package ru.storage.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;

import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "box")
@Data
@AllArgsConstructor
@NoArgsConstructor
/* фиксированный субъект, количество и свойства не должны меняться */
public class Box {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; /* числовой идентификатор*/
    @NotBlank
    private String idBox; /* символьное обозначение бокса*/
    /* общие параметры */
    @NotBlank
    private double square; /* площадь*/
    @NotBlank
    private double length; /* длина*/
    @NotBlank
    private double width; /* ширина*/
    @NotBlank
    private double height; /* высота*/

    /* особые параметры*/
    @Enumerated(EnumType.STRING)
    private Entrance entrance; /* вход в помещение*/
    private Boolean isWarm; /* Отапливаемый */
    private Boolean isElectricity; /* Есть отдельное электричесвто*/
    private Boolean isWater; /* Есть вода */

    // тут пишем вычисляемое поле, как будто это делаем в sql запросе, псевдоним не нужно присваивать
    @Formula(value = "(id in (select ab.box_id from account_box ab where ab.account_id in (select a.id from accounts a where NOW() between a.date_start AND a.date_end)) )")
    //count(*)>0 as isFree from accounts a where a.box_id=1 and  NOW() between a.date_start AND a.date_end") //count(*)>0 From Accounts a where a.box_id=id and  NOW() between a.date_start AND a.date_end")
    private boolean isFree;
    @Formula(value = "COALESCE((select p.sum_price From Prices p Where p.box_id=id and NOW() between p.date_start AND p.date_end order by p.date_start desc limit 1), 0)")
    private double price;

}
