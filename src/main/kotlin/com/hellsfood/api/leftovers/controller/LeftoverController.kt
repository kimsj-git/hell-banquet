package com.hellsfood.api.leftovers.controller

import com.hellsfood.api.leftovers.data.Ranking
import com.hellsfood.api.leftovers.dto.LeftoverRegisterRequestDto
import com.hellsfood.api.leftovers.service.LeftoverService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import lombok.RequiredArgsConstructor
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@Api(tags = ["잔반 정보 API"])
@RequiredArgsConstructor
@RestController
@RequestMapping("/leftovers")
class LeftoverController(
    private val leftoverService: LeftoverService,
) {
    @PostMapping("/register")
    @ApiOperation(value = "식사 기록", notes = "입력받은 음식양을 DB에 저장한다.")
    fun registerLeftover(
        @RequestBody @ApiParam(value = "회원가입 정보", required = true) requestDto: LeftoverRegisterRequestDto
    ): ResponseEntity<*> {
        val result: Boolean = leftoverService.registerLeftover(requestDto)
        if (result) {
            return ResponseEntity.ok("${requestDto.userId}님의 ${LocalDate.now()}일 식사정보가 기록되었습니다.")
        } else {
            return ResponseEntity.badRequest().body("유효하지 않은 저장 요청 정보입니다.");
        }
    }

    @GetMapping("/force_update")
    @ApiOperation(value = "디버깅용 랭킹 테이블 강제 업데이트 요청 함수")
    fun forceUpdateRankingList(){
        leftoverService.refreshRankingTable()
    }

    @GetMapping("/ranking")
    @ApiOperation(value = "일일 랭킹", notes = "")
    fun getDailyRanking(@ApiParam(value = "내 랭킹 정보를 조회하고 싶은 사용자 ID. 없으면 검색 안함.", required = false) userId: String): ResponseEntity<*> {
        val dailyRanking: List<Ranking> = leftoverService.getRankingList(userId)
        return ResponseEntity.ok(dailyRanking)
    }
}