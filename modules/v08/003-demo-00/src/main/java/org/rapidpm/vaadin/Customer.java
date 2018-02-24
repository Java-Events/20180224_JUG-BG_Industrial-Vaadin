package org.rapidpm.vaadin;

import org.rapidpm.dependencies.core.logger.HasLogger;

import java.io.Serializable;
import java.time.LocalDate;

/**
 */
@SuppressWarnings("serial")
public class Customer implements Serializable, HasLogger {

  private Long id;
  private String firstName = "";
  private String lastName  = "";
  private LocalDate      birthDate;
  private CustomerStatus status;
  private String email = "";

  public boolean isPersisted() {
    return id != null;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (this.id == null) {
      return false;
    }

    if (obj instanceof Customer && obj.getClass().equals(getClass())) {
      return this.id.equals(((Customer) obj).id);
    }

    return false;
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 43 * hash + (id == null ? 0 : id.hashCode());
    return hash;
  }

  @Override
  public String toString() {
    return firstName + " " + lastName;
  }


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public LocalDate getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(LocalDate birthDate) {
    this.birthDate = birthDate;
  }

  public CustomerStatus getStatus() {
    return status;
  }

  public void setStatus(CustomerStatus status) {
    this.status = status;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}