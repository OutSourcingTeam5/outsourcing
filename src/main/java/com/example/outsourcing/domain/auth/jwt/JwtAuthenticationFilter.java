package com.example.outsourcing.domain.auth.jwt;

import java.io.IOException;

import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtProvider jwtProvider;

	public JwtAuthenticationFilter(JwtProvider jwtProvider) {
		this.jwtProvider = jwtProvider;
	}

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain
	) throws ServletException, IOException {

		String uri = request.getRequestURI();

		// 로그인 필수 경로
		boolean mustLogin = uri.startsWith("/api/stores")
			|| uri.startsWith("/api/orders")
			|| uri.startsWith("/api/menus")
			|| uri.startsWith("/api/users");

		String authHeader = request.getHeader("Authorization");

		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			String token = authHeader.substring(7);

			if (jwtProvider.validateToken(token)) {
				Long userId = jwtProvider.getUserId(token);
				request.setAttribute("userId", userId);
			} else if (mustLogin) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않은 토큰입니다.");
				return;
			}
		} else if (mustLogin) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "로그인이 필요합니다.");
			return;
		}

		filterChain.doFilter(request, response);
	}
}
