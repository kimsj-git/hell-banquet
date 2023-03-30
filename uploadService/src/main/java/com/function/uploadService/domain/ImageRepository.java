package com.function.uploadService.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {

	Image findOneByPropsName(CharacterCode propsName);
}
