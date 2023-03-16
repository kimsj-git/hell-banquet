package com.hellsfood.api.users.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hellsfood.api.roles.data.RoleRepository;
import com.hellsfood.api.users.data.User;
import com.hellsfood.api.users.data.UserRepository;
import com.hellsfood.api.users.dto.ExcelizedUserRegisterResultDto;
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
	public List<ExcelizedUserRegisterResultDto> registerUsers(Workbook requestWorkbook) {
		Sheet requestSheet = requestWorkbook.getSheetAt(0);
		List<ExcelizedUserRegisterResultDto> resultList = new ArrayList<>();

		for (int i = 1; i < requestSheet.getPhysicalNumberOfRows(); i++) {
			Row requestRow = requestSheet.getRow(i);
			ExcelizedUserRegisterResultDto resultDto = new ExcelizedUserRegisterResultDto(i);

			StringBuilder errorInfo = new StringBuilder();

			resultDto.setEmail(requestRow.getCell(1).getStringCellValue());
			if (resultDto.getEmail() == null || !resultDto.getEmail()
				.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
				errorInfo.append("이메일 형식에 오류가 있습니다.");
				resultDto.setResult("실패");
			} else if (userRepository.existsByEmail(resultDto.getEmail())) {
				errorInfo.append("이미 가입된 이메일입니다.");
				resultDto.setResult("실패");
			} else if (resultDto.getEmail().length() > 100) {
				errorInfo.append("이메일 글자수 제한 초과입니다.");
				resultDto.setResult("실패");
			} else {
				try {
					resultDto.setUserId(requestRow.getCell(0).getStringCellValue());
				} catch (NullPointerException e) {
				}
				try {
					resultDto.setName(requestRow.getCell(2).getStringCellValue());
				} catch (NullPointerException e) {
				}
				String generatedValue = "guser" + (userRepository.getLastUID() + 1);
				if (resultDto.getUserId() != null && userRepository.existsByUserId(resultDto.getUserId())) {
					resultDto.setUserId(generatedValue);
					errorInfo.append("이미 가입된 아이디입니다. 아이디가 임의로 생성되었습니다.");
					resultDto.setResult("조건 변경");
				} else if (resultDto.getUserId() == null) {
					resultDto.setUserId(generatedValue);
					errorInfo.append("아이디가 임의로 생성되었습니다.");
					resultDto.setResult("조건 변경");
				} else if (resultDto.getUserId().length() > 16) {
					resultDto.setUserId(generatedValue);
					errorInfo.append("아이디의 길이 범위 오류입니다. 임의의 아이디로 가입되었습니다.");
					resultDto.setResult("조건 변경");
				}
				if (resultDto.getName() != null && userRepository.existsByName(resultDto.getName())) {
					resultDto.setName(generatedValue);
					if (errorInfo.length() > 0) {
						errorInfo.append(" | ");
					}
					errorInfo.append("중복된 닉네임입니다. 임의로 생성된 닉네임으로 설정되었습니다.");
					resultDto.setResult("조건 변경");
				} else if (resultDto.getName() == null) {
					resultDto.setName(generatedValue);
					if (errorInfo.length() > 0) {
						errorInfo.append(" | ");
					}
					errorInfo.append("임의로 생성된 닉네임으로 설정되었습니다.");
					resultDto.setResult("조건 변경");
				} else if (resultDto.getName().length() > 16) {
					resultDto.setName(generatedValue);
					errorInfo.append("닉네임의 길이 범위 오류입니다. 임의의 닉네임으로 가입되었습니다.");
					resultDto.setResult("조건 변경");
				}

				// TODO: 추후 대량 회원가입 요청한 매니저 아이디 넣어주는거도 고려해보기
				userRepository.save(User.builder()
					.userId(resultDto.getUserId())
					.password(passwordEncoder.encode(resultDto.getUserId()))
					.name(resultDto.getName())
					.email(resultDto.getEmail())
					.roles(Collections.singletonList(
						roleRepository.findByRoleName("user")
							.orElseThrow(() -> new RuntimeException("권한 설정 중 오류가 발생하였습니다."))))
					.build());
			}
			resultDto.setErrorInfo(errorInfo.toString());
			if (resultDto.getResult() == null) {
				resultDto.setResult("성공");
			}
			resultList.add(resultDto);
		}
		return resultList;
	}

	public boolean listToExcel(List<ExcelizedUserRegisterResultDto> list, HttpServletResponse response) {
		Workbook resultWorkbook = new SXSSFWorkbook();
		Sheet resultSheet = resultWorkbook.createSheet("가입 결과");
		resultSheet.setColumnWidth(1, 7500);
		resultSheet.setColumnWidth(2, 4400);
		resultSheet.setColumnWidth(3, 7500);
		resultSheet.setColumnWidth(5, 20000);
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
		headerCell.setCellValue("가입 결과");
		headerCell = header.createCell(5);
		headerCell.setCellValue("비고");

		for (int i = 0; i < list.size(); i++) {
			Row resultRow = resultSheet.createRow(i + 1);

			Cell cell = resultRow.createCell(0);
			cell.setCellValue(i + 1);
			cell = resultRow.createCell(1);
			cell.setCellValue(list.get(i).getEmail());
			cell = resultRow.createCell(2);
			cell.setCellValue(list.get(i).getUserId());
			cell = resultRow.createCell(3);
			cell.setCellValue(list.get(i).getName());
			cell = resultRow.createCell(4);
			cell.setCellValue(list.get(i).getResult());
			cell = resultRow.createCell(5);
			cell.setCellValue(list.get(i).getErrorInfo());
		}
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment;filename=result.xlsx");
		// file 손상 오류 https://developer-davii.tistory.com/49

		try {
			resultWorkbook.write(response.getOutputStream());
			resultWorkbook.close();
			response.getOutputStream().close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("결과 파일 작성 중 오류가 발생했습니다. (IOException)");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
