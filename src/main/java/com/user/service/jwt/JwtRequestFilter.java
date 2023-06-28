package com.user.service.jwt;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.user.service.jwt.util.JwtTokenUtil;
import com.user.service.payloads.SharedData;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	private JwtTokenUtil tokenUtil;

	@Autowired
	private JwtAuthenticationEntryPoint entryPoint;

	@Autowired
	private JwtUserDetailService userDetailService;

	private String extractJwtFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7, bearerToken.length());
		}
		return null;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		try {

			String jwtToken = extractJwtFromRequest(request);
			String username = null;
	

			if (StringUtils.hasText(jwtToken)) {
				try {
					username = this.tokenUtil.extractUsername(jwtToken);
				} catch (ExpiredJwtException e) {
					entryPoint.commence(request, response, e);
					return;
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
					UserDetails userDetails = this.userDetailService.loadUserByUsername(username);

					if (this.tokenUtil.validateToken(jwtToken, userDetails)) {
						UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails,
								null, userDetails.getAuthorities());
						token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
						SecurityContextHolder.getContext().setAuthentication(token);
						SharedData.setData("jwtToken", "Bearer "+jwtToken);
					} else {
						log.info("JwtRequestFilter :: Invalid Token");
					}

				} else {
					log.info("Context in not null");
				}

			} else {
				log.info("Invalid JWT TOKEN : {}", jwtToken);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			filterChain.doFilter(request, response);
		} catch (Exception e) {
			// do nothing
		}
	}
}
