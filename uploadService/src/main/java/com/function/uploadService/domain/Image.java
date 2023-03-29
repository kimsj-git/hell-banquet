package com.function.uploadService.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "character_image")
public class Image {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "file_path", nullable = false)
	private String filePath;

	@Column(name = "props_name", nullable = false)
	private String propsName;

	@Builder
	public Image(String filePath, String propsName) {
		this.filePath = filePath;
		this.propsName = propsName;
	}

}
