package project.Domain.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.annotation.PostConstruct;
import java.util.Random;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "clients")
public class Client {
    @Id
    private String customId;

    private ClientType clientType;

    @PostConstruct
    public void init() {
        createClientId();
    }

    public void createClientId() {
        String prefix = "CLIENT_";
        String randomPart = generateRandomNumber();
        String id = prefix + randomPart;
        this.setCustomId(id);
    }

    private String generateRandomNumber() {
        Random random = new Random();
        int randomNumber = random.nextInt(90000000) + 10000000;
        return String.valueOf(randomNumber);
    }
}
