package com.mango.customer.application.slogan;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SloganDto {

  @NotNull
  private String userId;

  @NotNull
  private String slogan;

}
