package com.mickey.dinggading.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.mickey.dinggading.model.Pet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;
import java.io.Serializable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

/**
 * A pet owner.
 */
//A pet owner.


@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Owner  implements Serializable {
  private static final long serialVersionUID = 1L;

  @NotNull @Pattern(regexp = "^[a-zA-Z]*$") @Size(min = 1, max = 30)   
  @JsonProperty("firstName")
  
  private String firstName;

  @NotNull @Pattern(regexp = "^[a-zA-Z]*$") @Size(min = 1, max = 30)   
  @JsonProperty("lastName")
  
  private String lastName;

  @NotNull @Size(min = 1, max = 255)   
  @JsonProperty("address")
  
  private String address;

  @NotNull @Size(min = 1, max = 80)   
  @JsonProperty("city")
  
  private String city;

  @NotNull @Pattern(regexp = "^[0-9]*$") @Size(min = 1, max = 20)   
  @JsonProperty("telephone")
  
  private String telephone;

  @Min(0)   
  @JsonProperty("id")
  
  private Integer id;

  @Valid   
  @JsonProperty("pets")
  
  private List<Pet> pets = new ArrayList<>();

  public Owner firstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  /**
   * The first name of the pet owner.
   * @return firstName
  */

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public Owner lastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  /**
   * The last name of the pet owner.
   * @return lastName
  */

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public Owner address(String address) {
    this.address = address;
    return this;
  }

  /**
   * The postal address of the pet owner.
   * @return address
  */

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public Owner city(String city) {
    this.city = city;
    return this;
  }

  /**
   * The city of the pet owner.
   * @return city
  */

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public Owner telephone(String telephone) {
    this.telephone = telephone;
    return this;
  }

  /**
   * The telephone number of the pet owner.
   * @return telephone
  */

  public String getTelephone() {
    return telephone;
  }

  public void setTelephone(String telephone) {
    this.telephone = telephone;
  }

  public Owner id(Integer id) {
    this.id = id;
    return this;
  }

  /**
   * The ID of the pet owner.
   * minimum: 0
   * @return id
  */

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Owner pets(List<Pet> pets) {
    this.pets = pets;
    return this;
  }

  public Owner addpetsItem(Pet petsItem) {
    this.pets.add(petsItem);
    return this;
  }

  /**
   * The pets owned by this individual including any booked vet visits.
   * @return pets
  */

  public List<Pet> getPets() {
    return pets;
  }

  public void setPets(List<Pet> pets) {
    this.pets = pets;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Owner owner = (Owner) o;
    return Objects.equals(this.firstName, owner.firstName) &&
        Objects.equals(this.lastName, owner.lastName) &&
        Objects.equals(this.address, owner.address) &&
        Objects.equals(this.city, owner.city) &&
        Objects.equals(this.telephone, owner.telephone) &&
        Objects.equals(this.id, owner.id) &&
        Objects.equals(this.pets, owner.pets);
  }

  @Override
  public int hashCode() {
    return Objects.hash(firstName, lastName, address, city, telephone, id, pets);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Owner {\n");
    
    sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");
    sb.append("    lastName: ").append(toIndentedString(lastName)).append("\n");
    sb.append("    address: ").append(toIndentedString(address)).append("\n");
    sb.append("    city: ").append(toIndentedString(city)).append("\n");
    sb.append("    telephone: ").append(toIndentedString(telephone)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    pets: ").append(toIndentedString(pets)).append("\n");
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

