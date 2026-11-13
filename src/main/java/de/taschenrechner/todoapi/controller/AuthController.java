package de.taschenrechner.todoapi.controller;

import de.taschenrechner.todoapi.dto.LoginRequest;
import de.taschenrechner.todoapi.dto.LoginResponse;
import de.taschenrechner.todoapi.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private final AuthenticationManager authenticationManager;
	private final UserDetailsService userDetailsService;
	private final JwtService jwtService;

	public AuthController(AuthenticationManager authenticationManager,
			UserDetailsService userDetailsService, JwtService jwtService) {
		this.authenticationManager = authenticationManager;
		this.userDetailsService = userDetailsService;
		this.jwtService = jwtService;
	}

	@PostMapping("/login")
	public LoginResponse login(@Valid @RequestBody LoginRequest request) {
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.username(),
							request.password()));
		} catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
					"Ungültige Anmeldedaten");
		}

		UserDetails userDetails = userDetailsService
				.loadUserByUsername(request.username());
		String token = jwtService.generateToken(userDetails);
		return new LoginResponse(token);
	}
}