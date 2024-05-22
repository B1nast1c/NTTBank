package com.microservices_credit.microcervicescredit.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "creditss")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class credits {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer id_credit;
    private String credit_number;
    private String credit_type;
    private Double saldo;
}
