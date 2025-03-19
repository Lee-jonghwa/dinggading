package com.mickey.dinggading.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.mickey.dinggading.model.Specialty;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;
import java.io.Serializable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

/**
 * Editable fields of a veterinarian.
 */
//Editable fields of a veterinarian.


@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class VetFields  implements Serializable {
  private static final long serialVersionUID = 1L;

  @NotNull @Pattern(regexp = "^[a-zA-Z]*$") @Size(min = 1, max = 30)   
  @JsonProperty("firstName")
  
  private String firstName;

  @NotNull @Pattern(regexp = "^[a-zA-Z]*$") @Size(min = 1, max = 30)   
  @JsonProperty("lastName")
  
  private String lastName;

  @NotNull @Valid   
  @JsonProperty("specialties")
  
  private List<@Valid Specialty> specialties = new ArrayList<>();

  public VetFields firstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  /**
   * The first name of the vet.
   * @return firstName
  */

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public VetFields lastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  /**
   * The last name of the vet.
   * @return lastName
  */

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public VetFields specialties(List<@Valid Specialty> specialties) {
    this.specialties = specialties;
    return this;
  }

  public VetFields addspecialtiesItem(Specialty specialtiesItem) {
    this.specialties.add(specialtiesItem);
    return this;
  }

  /**
   * The specialties of the vet.
   * @return specialties
  */

  public List<@Valid Specialty> getSpecialties() {
    return specialties;
  }

  public void setSpecialties(List<@Valid Specialty> specialties) {
    this.specialties = specialties;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VetFields vetFields = (VetFields) o;
    return Objects.equals(this.firstName, vetFields.firstName) &&
        Objects.equals(this.lastName, vetFields.lastName) &&
        Objects.equals(this.specialties, vetFields.specialties);
  }

  @Override
  public int hashCode() {
    return Objects.hash(firstName, lastName, specialties);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VetFields {\n");
    
    sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");
    sb.append("    lastName: ").append(toIndentedString(lastName)).append("\n");
    sb.append("    specialties: ").append(toIndentedString(specialties)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

