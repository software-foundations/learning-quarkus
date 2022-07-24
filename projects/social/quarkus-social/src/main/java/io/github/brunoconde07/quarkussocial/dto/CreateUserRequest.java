package io.github.brunoconde07.quarkussocial.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Data
public class CreateUserRequest {

	@NotBlank( message = "Name is Required" )
	private String name;

	@NotNull( message = "Age is Required" )
	private Integer age;
}