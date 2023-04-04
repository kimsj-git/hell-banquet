package com.hellsfood.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.hellsfood.domain.image.JanbanCode;
import com.hellsfood.domain.janban.JanbanFeature;
import com.hellsfood.domain.janban.Janbani;
import com.hellsfood.domain.janban.JanbaniRepository;
import com.hellsfood.dto.JanbaniRequestDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JanbaniService {

	private final JanbaniRepository janbaniRepository;

	public Janbani createJanban(JanbaniRequestDto dto) {
		String userId = dto.getUserId();
		String feature = dto.getFeature();

		Janbani janbani = janbaniRepository.findByUserId(userId)
			.orElse(Janbani.builder()
				.userId(userId)
				.feature(feature)
				.janbanCode(null)
				.build());

		JanbanFeature janbanFeature = JanbanFeature.fromValue(feature);
		JanbanCode janbanCode = janbanFeature.getJanbanCode();
		janbani.update(janbanFeature, janbanCode);
		return janbaniRepository.save(janbani);
	}

	public String getJanban(String userId) {
		Janbani janbani = janbaniRepository.findByUserId(userId)
			.orElseThrow(() -> new IllegalArgumentException("해당하는 잔반이가 없습니다."));

		JanbanCode janbanCode = janbani.getJanbanCode();
		return janbanCode.toString();
	}

	public Boolean hasJanbanInDate(String userId, String date) {
		Optional<Janbani> optionalJanbani = janbaniRepository.findByUserId(userId);

		if (optionalJanbani.isEmpty()) {
			return false;
		}

		Janbani janbani = optionalJanbani.get();
		LocalDate updateDate = janbani.getUpdateAt().toLocalDate();

		return updateDate.isEqual(parseDate(date));
	}

	private LocalDate parseDate(String dateStr) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return LocalDate.parse(dateStr, formatter);
	}

}