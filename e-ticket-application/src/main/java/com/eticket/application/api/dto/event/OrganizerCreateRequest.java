package com.eticket.application.api.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Valid
public class OrganizerCreateRequest {
    private Integer id;
    @NotBlank
    private String name;
    @NotBlank
    private String address;
    @Email
    private String email;
    @NotBlank
    @Length(min = 10, max = 10)
    @Pattern(regexp = "^\\d+$")
    private String mobile;
    @NotBlank
    private String representative;
    @NotBlank
    private String taxCode;
    private String summary;
}
