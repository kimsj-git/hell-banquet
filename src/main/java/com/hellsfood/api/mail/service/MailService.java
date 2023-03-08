package com.hellsfood.api.mail.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import com.hellsfood.api.mail.dto.MailDto;
import com.hellsfood.api.users.service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MailService {

	private final MailSender mailSender;
	private final UserService userService;

	@Value("${spring.mail.username}")
	String mailAuthor;

	/**
	 * @param userId 비밀번호를 재설정할 사용자 ID
	 * @param email 사용자임을 증명하기 위해 가입한 ID와 이메일 정보가 일치하는지 확인 및 임시 비밀번호 메일의 받는사람 주소.
	 * @return 생성된 임시 비밀번호 메일 Data Transfer Object
	 */
	public MailDto createMailAndMakeTempPassword(String userId, String email) {
		String tempPassword = userService.getTempPassword(userId);
		MailDto mailDto = new MailDto();
		mailDto.setAddress(email);
		mailDto.setTitle("[CoCo] 임시 비밀번호 안내");
		mailDto.setMessage(
			"안녕하세요. CoCo입니다. \n" + userId + "님의 임시 비밀번호는 [ " + tempPassword
				+ " ] 입니다. \n\n임시 비밀번호로 로그인 후 반드시 비밀번호를 변경해주세요.");
		return mailDto;
	}

	public void sendMail(MailDto mailDto) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(mailDto.getAddress());
		message.setSubject(mailDto.getTitle());
		message.setText(mailDto.getMessage());
		message.setFrom(mailAuthor);
		System.out.println("보낸 메일 정보: " + message);
		mailSender.send(message);
	}
}
