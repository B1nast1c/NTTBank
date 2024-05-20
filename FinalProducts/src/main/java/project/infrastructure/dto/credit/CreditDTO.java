package project.infrastructure.dto.credit;

import lombok.Data;

import java.io.Serializable;

@Data
public class CreditDTO implements Serializable {
  String creditId;
  String clientId;
  double creditAmmount;
}