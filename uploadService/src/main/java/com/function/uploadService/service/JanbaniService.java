package com.function.uploadService.service;

import org.springframework.stereotype.Service;

import com.function.uploadService.domain.image.JanbanCode;
import com.function.uploadService.domain.janban.JanbanFeature;
import com.function.uploadService.domain.janban.Janbani;
import com.function.uploadService.domain.janban.JanbaniRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JanbaniService {

	private final JanbaniRepository janbaniRepository;

	public Janbani createJanban(String userId, String feature) {
		// System.out.println("feature: " + feature);
		JanbanFeature janbanFeature = JanbanFeature.fromValue(feature);
		// System.out.println("janbanFeature: " + janbanFeature);

		JanbanCode janbanCode = janbanFeature.getRandomJanbanCode();
		// System.out.println("janbanCode: " + janbanCode);

		Janbani janbani = Janbani.builder()
			.userId(userId)
			.feature(feature)
			.janbanCode(janbanCode)
			.build();
		return janbaniRepository.save(janbani);
	}


}