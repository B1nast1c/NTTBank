package application.controller;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import project.application.controller.ClientController;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = ClientController.class)
@Import(ClientController.class)
public class ClientControllerTest {
}
