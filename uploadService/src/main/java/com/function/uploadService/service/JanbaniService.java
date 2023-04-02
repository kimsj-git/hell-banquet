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
		Janbani janbani = janbaniRepository.findByUserId(userId)
			.orElse(Janbani.builder()
				.userId(userId)
				.feature(feature)
				.janbanCode(null)
				.build());

		JanbanFeature janbanFeature = JanbanFeature.fromValue(feature);
		JanbanCode janbanCode = janbanFeature.getRandomJanbanCode();

		janbani.update(janbanFeature, janbanCode);
		return janbaniRepository.save(janbani);
	}
	public String getJanban(String userId) {
		Janbani janbani = janbaniRepository.findByUserId(userId)
			.orElseThrow(() -> new IllegalArgumentException("해당하는 잔반이가 없습니다."));

		JanbanCode janbanCode = janbani.getJanbanCode();
		return janbanCode.toString();
	}
}