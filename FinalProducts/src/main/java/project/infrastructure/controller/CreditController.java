package project.infrastructure.controller;

import org.springframework.web.bind.annotation.*;
import project.infrastructure.dto.credit.CreditDTO;

import java.util.List;

@RestController
@RequestMapping("/credits")
public class CreditController {


  @PostMapping
  public CreditDTO addClient(@RequestBody CreditDTO creditDTO) {
    return null;
  }

  @GetMapping
  public List<CreditDTO> getCredits() {
    return null;
  }

  @GetMapping("/client/{clientId}")
  public List<CreditDTO> getCreditsByClientId(@PathVariable String clientId) {
    return null;
  }

  @GetMapping("/{creditId}")
  public CreditDTO getCreditById(@PathVariable String creditId) {
    return null;
  }

  @PutMapping("/{creditId}")
  public String updateCredit(@PathVariable String creditId, @RequestBody CreditDTO creditDTO) {
    return "Credit updated successfully";
  }

  @DeleteMapping("/{creditId}")
  public String deleteCredit(@PathVariable String creditId) {
    return "Credit deleted successfully";
  }
}
