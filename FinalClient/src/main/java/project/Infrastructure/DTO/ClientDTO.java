package project.Infrastructure.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ClientDTO {

    @Id
    String customId;
    // String name;
    // String address;
    // String email;
    // String phone;
    // String status;
    // String dateCreate;
    // String document;
    String clientType;
}