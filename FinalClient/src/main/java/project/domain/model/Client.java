package project.domain.model;

import com.mongodb.lang.NonNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;


/**
 * Representa una entidad de cliente en el servicio.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "clients")
public class Client {
  @Id
  private String customId;

  private ClientType clientType;
  private String clientEmail = "";
  private String clientPhone = "";
  private Boolean status = true;
  private String createdAt = new Date().toString();

  @NonNull
  private String clientName = "";

  @NonNull
  private String clientAddress = "";

  @Indexed(unique = true)
  private String documentNumber;
}
