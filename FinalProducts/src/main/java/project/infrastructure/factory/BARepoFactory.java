package project.infrastructure.factory;

import org.springframework.stereotype.Component;
import project.domain.ports.BAccountPort;
import project.infrastructure.adapters.CurrAccAdapter;
import project.infrastructure.adapters.FxdTermAdapter;
import project.infrastructure.adapters.SavingsAccAdapter;
import project.infrastructure.exceptions.throwable.WrongAccountType;

@Component
public class BARepoFactory {
  private final SavingsAccAdapter savingsAccAdapter;
  private final FxdTermAdapter fxdTermAdapter;
  private final CurrAccAdapter currAccAdapter;

  public BARepoFactory(SavingsAccAdapter savingsAccAdapter, FxdTermAdapter fxdTermAdapter, CurrAccAdapter currAccAdapter) {
    this.savingsAccAdapter = savingsAccAdapter;
    this.fxdTermAdapter = fxdTermAdapter;
    this.currAccAdapter = currAccAdapter;
  }

  public BAccountPort getAdapter(String type) {
    switch (type) {
      case "AHORRO":
        return savingsAccAdapter;
      case "CUENTA_CORRIENTE":
        return currAccAdapter;
      case "PLAZO_FIJO":
        return fxdTermAdapter;
      default:
        throw new WrongAccountType("Unsupported account type: " + type);
    }
  }
}
