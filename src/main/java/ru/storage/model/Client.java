package ru.storage.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "clients")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Client extends Person {
    private String comment;

    //    public Client(){
//        super();
//    }
    public Client(String firstName, String lastName, Date birthDate, String phoneNumber, String emailAddress, String address, String comment) {
        super();
    }
}
