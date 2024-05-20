
package project.transactionsservice.infrastructure.exceptions.throwable;

public class InvalidClient extends RuntimeException {
  public InvalidClient(String message) {
    super(message);
  }
}
