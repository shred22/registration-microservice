package com.registration.controller;


import com.registration.handler.error.SecurityFailedException;
import com.registration.jwt.util.JwtUtil;
import org.openapitools.api.AuthenticateApiDelegate;
import org.openapitools.model.AuthenticationRequest;
import org.openapitools.model.AuthenticationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;


@Component
public class AuthenticationApiDelegateImpl implements AuthenticateApiDelegate {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtUtil jwtUtil;

    public ResponseEntity<org.openapitools.model.AuthenticationResponse> authenticatePost(AuthenticationRequest authenticationRequest) {
        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        } catch (AuthenticationException e) {
            throw new SecurityFailedException(e);
        }

        if(authentication != null && authentication.isAuthenticated()) {
            AuthenticationResponse authenticationResponse = new AuthenticationResponse();
            authenticationResponse.setJwt(jwtUtil.generateToken(authenticationRequest.getUsername()));
            return ResponseEntity.ok(authenticationResponse);
        }
        throw new SecurityFailedException("Bad Credentials Presented");
    }

}
