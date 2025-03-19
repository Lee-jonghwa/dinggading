package com.mickey.dinggading.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.mickey.dinggading.model.ValidationMessage;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import java.io.Serializable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

/**
 * The schema for all error responses.
 */
//The schema for all error responses.


@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ProblemDetail  implements Serializable {
  private static final long serialVersionUID = 1L;

  @Valid   
  @JsonProperty("type")
  
  private URI type;

    
  @JsonProperty("title")
  
  private String title;

  @Min(400) @Max(600)   
  @JsonProperty("status")
  
  private Integer status;

    
  @JsonProperty("detail")
  
  private String detail;

  @Valid   
  @JsonProperty("timestamp")
  
  @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime timestamp;

  @NotNull @Valid   
  @JsonProperty("schemaValidationErrors")
  
  private List<ValidationMessage> schemaValidationErrors = new ArrayList<>();

  public ProblemDetail type(URI type) {
    this.type = type;
    return this;
  }

  /**
   * Full URL that originated the error response.
   * @return type
  */

  public URI getType() {
    return type;
  }

  public void setType(URI type) {
    this.type = type;
  }

  public ProblemDetail title(String title) {
    this.title = title;
    return this;
  }

  /**
   * The short error title.
   * @return title
  */

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public ProblemDetail status(Integer status) {
    this.status = status;
    return this;
  }

  /**
   * HTTP status code
   * minimum: 400
   * maximum: 600
   * @return status
  */

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public ProblemDetail detail(String detail) {
    this.detail = detail;
    return this;
  }

  /**
   * The long error message.
   * @return detail
  */

  public String getDetail() {
    return detail;
  }

  public void setDetail(String detail) {
    this.detail = detail;
  }

  public ProblemDetail timestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * The time the error occurred.
   * @return timestamp
  */

  public OffsetDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public ProblemDetail schemaValidationErrors(List<ValidationMessage> schemaValidationErrors) {
    this.schemaValidationErrors = schemaValidationErrors;
    return this;
  }

  public ProblemDetail addschemaValidationErrorsItem(ValidationMessage schemaValidationErrorsItem) {
    this.schemaValidationErrors.add(schemaValidationErrorsItem);
    return this;
  }

  /**
   * Validation errors against the OpenAPI schema.
   * @return schemaValidationErrors
  */

  public List<ValidationMessage> getSchemaValidationErrors() {
    return schemaValidationErrors;
  }

  public void setSchemaValidationErrors(List<ValidationMessage> schemaValidationErrors) {
    this.schemaValidationErrors = schemaValidationErrors;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProblemDetail problemDetail = (ProblemDetail) o;
    return Objects.equals(this.type, problemDetail.type) &&
        Objects.equals(this.title, problemDetail.title) &&
        Objects.equals(this.status, problemDetail.status) &&
        Objects.equals(this.detail, problemDetail.detail) &&
        Objects.equals(this.timestamp, problemDetail.timestamp) &&
        Objects.equals(this.schemaValidationErrors, problemDetail.schemaValidationErrors);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, title, status, detail, timestamp, schemaValidationErrors);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProblemDetail {\n");
    
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    detail: ").append(toIndentedString(detail)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    schemaValidationErrors: ").append(toIndentedString(schemaValidationErrors)).append("\n");
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

