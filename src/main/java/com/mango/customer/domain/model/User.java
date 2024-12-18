package com.mango.customer.domain.model;

import com.mango.customer.domain.exceptions.MaxSlogansException;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user")
public class User {

  public static final int MAX_SLOGANS = 3;

  @Id
  private String id;

  private String name;

  private String city;

  private String address;

  private String email;

  private List<String> slogans = new ArrayList<>();

  public void addSlogan(String slogan) {
    if (this.slogans.size() == MAX_SLOGANS) {
      throw new MaxSlogansException("You have reached the max slogan possible (" + MAX_SLOGANS + ")");
    }
    this.slogans.add(slogan);
  }

}
