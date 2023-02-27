package com.hellsfood.api.users.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hellsfood.api.users.service.UserService;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;

@Api(tags = "회원 관리 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/useri")
public class UserController {
	private final UserService userService;


}
