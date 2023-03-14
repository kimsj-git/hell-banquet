package com.hellsfood.api.users.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.time.LocalDateTime;
import java.util.Collections;

import javax.transaction.Transactional;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hellsfood.api.roles.data.RoleRepository;
import com.hellsfood.api.users.data.User;
import com.hellsfood.api.users.data.UserRepository;
import com.hellsfood.api.users.dto.UserRegisterRequestDto;
import com.hellsfood.api.users.dto.UpdateRequestDto;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

	@Value("${jwt.secret}")
	private String uniqueKey;
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;

	private final PasswordEncoder passwordEncoder;

	@Transactional
	public Long registerUser(UserRegisterRequestDto requestDto) {
		if (requestDto.getUserId().startsWith("guser") || requestDto.getName().startsWith("guser")) {
			return -1L;
		}
		requestDto.setPassword(passwordEncoder.encode(requestDto.getPassword()));
		User tmpUser = requestDto.toEntity();
		tmpUser.setRoles(Collections.singletonList(
			roleRepository.findByRoleName("user").orElseThrow(() -> new RuntimeException("권한 설정 중 오류가 발생하였습니다."))));
		return userRepository.save(tmpUser).getId();
	}

	@Transactional
	public String updateUser(String userId, UpdateRequestDto requestDto, String accessToken) {
		User user = getActiveUserWithValidationCheck(userId, accessToken);
		if (user == null) {
			return null;
		}
		if (requestDto.getName() != null) {
			user.setName(requestDto.getName());
		}
		if (requestDto.getEmail() != null) {
			user.setEmail(requestDto.getEmail());
		}
		return userId;
	}

	@Transactional
	public String deleteUser(String id, String accessToken) {
		User user = getActiveUserWithValidationCheck(id, accessToken);
		if (user != null) {
			user.setDelFlag(LocalDateTime.now());
			return id;
		} else {
			return null;
		}
	}

	@Transactional
	public String updatePassword(String userId, String newPassword, boolean needValidation, String accessToken) {
		if (needValidation) {
			String extractedId = getUserIdFromAccessToken(accessToken);
			System.out.println("[updatePassword@UserService] AccessToken에서 추출한 userId: " + extractedId);
			if (!userId.equals(extractedId)) {
				return null;
			}
		}
		User user = getActiveUser(userId);
		String encodedPassword = passwordEncoder.encode(newPassword);
		user.setPassword(encodedPassword);
		System.out.println(userRepository.save(user));
		return user.getUserId();
	}

	@Transactional
	public String getTempPassword(String userId) {
		String tempPassword = makeTempPassword();
		return updatePassword(userId, tempPassword, false, null).equals(userId) ? tempPassword : null;
	}

	private User getActiveUserWithValidationCheck(String userId, String accessToken) {
		return getUserIdFromAccessToken(accessToken).equals(userId) ? getActiveUser(userId) : null;
	}

	public User getActiveUser(String userId) {
		User user = getUser(userId);
		if (user.getDelFlag() != null) {
			throw new IllegalArgumentException(userId + " 사용자는 탈퇴한 사용자입니다.");
		} else {
			return user;
		}
	}

	public String getUserIdFromAccessToken(String token) {
		String userId = null;
		try {
			userId = Jwts.parser().setSigningKey(uniqueKey.getBytes()).parseClaimsJws(token).getBody().getSubject();
		} catch (Exception e) {
			System.out.println("AccessToken에서 회원 ID 추출 중 오류가 발생했습니다.\n" + e.getMessage());
		}
		return userId;
	}

	public User getUser(String id) {
		User user = userRepository.findByUserId(id)
			.orElseThrow(() -> new IllegalArgumentException(id + " 사용자를 찾을 수 없습니다."));
		return user;
	}

	private String makeTempPassword() {
		char[] charSet = new char[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
			'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a',
			'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
			'w', 'x', 'y', 'z'};
		StringBuilder tempPassword = new StringBuilder();

		for (int i = 0; i < 8; i++) {
			int idx = (int)(charSet.length * Math.random());
			tempPassword.append(charSet[idx]);
		}
		return tempPassword.toString();
	}

	public String findActiveUserByEmail(String email) {
		return userRepository.findActiveUserIdByEmail(email).orElse(null);

	}

	@Transactional
	public Workbook registerUsers(Workbook requestWorkbook) {
		Sheet requestSheet = requestWorkbook.getSheetAt(0);

		Workbook resultWorkbook = new XSSFWorkbook();
		Sheet resultSheet = resultWorkbook.createSheet("가입 결과");
		resultSheet.setColumnWidth(1, 2200);
		resultSheet.setColumnWidth(2, 500);
		resultSheet.setColumnWidth(3, 500);
		resultSheet.setColumnWidth(4, 4000);
		Row header = resultSheet.createRow(0);
		Cell headerCell = header.createCell(0);
		headerCell.setCellValue("요청 번호");
		headerCell = header.createCell(1);
		headerCell.setCellValue("이메일");
		headerCell = header.createCell(2);
		headerCell.setCellValue("ID");
		headerCell = header.createCell(3);
		headerCell.setCellValue("사용자명");
		headerCell = header.createCell(4);
		headerCell.setCellValue("비고");

		for (int i = 1; i < requestSheet.getPhysicalNumberOfRows(); i++) {
			Row requestRow = requestSheet.getRow(i);
			Row resultRow = resultSheet.createRow(i);
			String userId;
			String userName;

			StringBuilder errorInfo = new StringBuilder();

			String email = requestRow.getCell(1).getStringCellValue();
			if (email == null || !email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
				errorInfo.append("이메일 형식 오류");
			} else if (userRepository.existsByEmail(email)) {
				errorInfo.append("이미 가입된 이메일입니다.");
			} else {
				try {
					userId = requestRow.getCell(0).getStringCellValue();
				} catch (NullPointerException e) {
					userId = null;
				}
				try {
					userName = requestRow.getCell(2).getStringCellValue();
				} catch (NullPointerException e) {
					userName = null;
				}
				String generatedValue = "guser" + (userRepository.getLastUID() + 1);
				if (userId != null && userRepository.existsByUserId(userId)) {
					userId = generatedValue;
					errorInfo.append("이미 가입된 아이디입니다. 아이디가 임의로 생성되었습니다. | ");
				} else if (userId == null) {
					userId = generatedValue;
					errorInfo.append("아이디가 임의로 생성되었습니다. | ");
				}
				if (userName != null && userRepository.existsByName(userName)) {
					userName = generatedValue;
					errorInfo.append("중복된 닉네임입니다. 임의로 생성된 닉네임으로 설정되었습니다.");
				} else if (userName == null) {
					userName = generatedValue;
					errorInfo.append("임의로 생성된 닉네임으로 설정되었습니다.");
				}

				userRepository.save(User.builder()
					.userId(userId)
					.password(passwordEncoder.encode(userId))
					.name(userName)
					.email(email)
					.roles(Collections.singletonList(
						roleRepository.findByRoleName("user")
							.orElseThrow(() -> new RuntimeException("권한 설정 중 오류가 발생하였습니다."))))
					.build());

				Cell currentCell = resultRow.createCell(0);
				currentCell.setCellValue(i);
				currentCell = resultRow.createCell(1);
				currentCell.setCellValue(email);
				currentCell = resultRow.createCell(2);
				currentCell.setCellValue(userId);
				currentCell = resultRow.createCell(3);
				currentCell.setCellValue(userName);
				currentCell = resultRow.createCell(4);
				currentCell.setCellValue(errorInfo.toString());
			}
		}
		File currDir = new File(".");
		String path = currDir.getAbsolutePath();
		String fileLocation = path.substring(0, path.length() - 1) + "result.xlsx";

		// file 손상 오류 https://developer-davii.tistory.com/49
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			resultWorkbook.write(outputStream);
			requestWorkbook.close();
			resultWorkbook.close();
			outputStream.close();
			return resultWorkbook;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("결과 파일 작성 중 오류가 발생했습니다. (IOException)");
		}
	}
}
