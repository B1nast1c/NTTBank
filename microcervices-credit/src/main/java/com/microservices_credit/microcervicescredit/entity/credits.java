package com.microservices_credit.microcervicescredit.entity;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @Autor: Bryan Flores
 * Tarea: Microservicios de creditos
 * Fecha: 9/05/2024
 */


@Getter
@Setter
@Document(collection = "creditss")
@AllArgsConstructor
@NoArgsConstructor
public class credits {

    @Id
    private String id;
    private String credit_number;
    private String credit_type;
    private Double balance;
    private String client_id;
}
