package com.github.Ignacio.my_Library_In_Spring.Security.Jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Autowired
    private JwtUtils utils;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        try{
            String token = parseJwt(request);
            logger.debug("Token extracted from the header: {}", token);
            if(token != null && utils.isValid(token)){
                String username = utils.getUsernameFromToken(token);
                logger.debug("Token valid. Username extracted: {}", username);

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                logger.debug("UserDetails loaded: {}", userDetails.getUsername());

                UsernamePasswordAuthenticationToken auth = new
                        UsernamePasswordAuthenticationToken(userDetails
                        ,null
                        ,userDetails.getAuthorities());

                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(auth);
                logger.info("Authentication established for user: {}", username);

            }else {
                logger.warn("Token is null or invalid: {}",token);

            }
        }catch (Exception e ){
            logger.error("Error en JwtFilter: {}", e.getMessage(), e);
        }
        filterChain.doFilter(request,response);
    }
    private String parseJwt(HttpServletRequest request){
        String token = utils.getJwtFromHeader(request);
        logger.debug("-2AuthTokenFilter.java; {}",token);
        return token;
    }
}
