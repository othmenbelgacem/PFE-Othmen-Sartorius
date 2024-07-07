package com.sartorius.tma.business.services;

import com.sartorius.tma.client.dtos.response.RefreshJwResponse;
import com.sartorius.tma.exceptions.UserForbiddenException;
import com.sartorius.tma.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;



@Service
@RequiredArgsConstructor
public class RefreshTokenService {

  private final JwtUtils jwtUtils;

  public RefreshJwResponse refreshUserToken(String refreshToken) {
    if (!StringUtils.hasText(refreshToken) || jwtUtils.isTokenExpired(refreshToken)) {
      throw new UserForbiddenException("Token is not valide");
    }
    return new RefreshJwResponse(jwtUtils.generateJwtTokenFromExpiredToken(refreshToken));
  }

}
