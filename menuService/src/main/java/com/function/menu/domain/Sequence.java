package com.function.menu.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;

@Getter
@Document(collection = "sequence")
public class Sequence {

	@Id
	private String id;
	private long seq;

}
