package cat.itacademy.s05.t02.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Schema(description = "Pet entity that represent every existing pet. ")
@Document(collection = "pet")
public class Pet {

	@Id
	private String id;
	@Getter
	@Setter
	private String user;
	@Getter
	@Setter
	private String name;

	public Pet(String name) {
		this.name = name;
	}

}