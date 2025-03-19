package com.mickey.dinggading.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import java.io.Serializable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

/**
 * A pet type.
 */
//A pet type.


@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class PetType  implements Serializable {
  private static final long serialVersionUID = 1L;

  @NotNull @Size(min = 1, max = 80)   
  @JsonProperty("name")
  
  private String name;

  @NotNull @Min(0)   
  @JsonProperty("id")
  
  private Integer id;

  public PetType name(String name) {
    this.name = name;
    return this;
  }

  /**
   * The name of the pet type.
   * @return name
  */

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public PetType id(Integer id) {
    this.id = id;
    return this;
  }

  /**
   * The ID of the pet type.
   * minimum: 0
   * @return id
  */

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PetType petType = (PetType) o;
    return Objects.equals(this.name, petType.name) &&
        Objects.equals(this.id, petType.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, id);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PetType {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
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

