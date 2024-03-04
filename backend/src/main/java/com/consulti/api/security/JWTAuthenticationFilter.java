package com.consulti.api.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collections;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response
    )throws AuthenticationException{

        AuthCredentials authCredentials = new AuthCredentials();

        try {
            authCredentials = new ObjectMapper().readValue(request.getReader(), AuthCredentials.class);

        }catch(IOException error){
            System.out.println(error.getMessage());
        }

        UsernamePasswordAuthenticationToken userNamePat= new UsernamePasswordAuthenticationToken(
                authCredentials.getUserName(),
                authCredentials.getPassword(),
                Collections.emptyList()
        );
        return getAuthenticationManager().authenticate(userNamePat);
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult
    ) throws IOException, ServletException{
        UserDetailsImpl  userDetails= (UserDetailsImpl) authResult.getPrincipal();
        String token= TokenUtils.createToken(userDetails.getUserName(), userDetails.getUsername());
        System.out.println("User name: "+userDetails);
        JSONObject json = new JSONObject();
        json.put("token", token);
        System.out.println(token);
        response.getWriter().write(json.toString());
        response.setContentType("Application/json");
        response.getWriter().flush();

        super.successfulAuthentication(request, response, chain, authResult);
    }
}
