package com.hellsfood.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hellsfood.domain.image.JanbanCode;
import com.hellsfood.domain.janban.JanbanFeature;
import com.hellsfood.domain.janban.Janbani;
import com.hellsfood.domain.janban.JanbaniRepository;
import com.hellsfood.dto.JanbaniRequestDto;
import com.hellsfood.dto.JanbaniUpdateRequestDto;

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

	@Transactional
	public Janbani updateJanbanCode(JanbaniUpdateRequestDto requestDto) {
		Janbani janbani = janbaniRepository.findByUserId(requestDto.getUserId())
			.orElseThrow(() -> new IllegalArgumentException("해당하는 잔반이가 없습니다."));

		String propName = requestDto.getPropName();
		System.out.println("propName = " + propName);
		System.out.println("janbani.getFeature().toString() = " + janbani.getFeature().toString());
		JanbanCode janbanCode = JanbanCode.findByPropNameAndJanbanFeature(propName, janbani.getFeature().toString());
		// if (janbanCode != null && janbanCode.getJanbanFeature().equals("GRD")) {
		// 	do something
		// }
		String janbanCodeString = janbanCode.name();
		System.out.println("janbanCode = " + janbanCodeString);
		janbani.updateJanbanCode(janbanCode);
		janbaniRepository.save(janbani);
		return janbani;
	}

	public String getJanban(String userId) {
		Janbani janbani = janbaniRepository.findByUserId(userId)
			.orElseThrow(() -> new IllegalArgumentException("해당하는 잔반이가 없습니다."));
		return janbani.getJanbanCode().name();
	}

	public Boolean hasJanbanInDate(String userId, String date) {
		Optional<Janbani> optionalJanbani = janbaniRepository.findByUserId(userId);

		if (optionalJanbani.isEmpty()) {
			return false;
		}

		Janbani janbani = optionalJanbani.get();
		LocalDate updateDate = janbani.getUpdatedAt().toLocalDate();

		return updateDate.isEqual(parseDate(date));
	}

	private LocalDate parseDate(String dateStr) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return LocalDate.parse(dateStr, formatter);
	}

}