package ifpb.edu.br.pj.ifpbichos.presentation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ifpb.edu.br.pj.ifpbichos.business.service.TokenService;
import ifpb.edu.br.pj.ifpbichos.model.entity.Donator;
import ifpb.edu.br.pj.ifpbichos.model.entity.User;
import ifpb.edu.br.pj.ifpbichos.model.repository.UserRepository;
import ifpb.edu.br.pj.ifpbichos.presentation.dto.AuthenticationDTO;
import ifpb.edu.br.pj.ifpbichos.presentation.dto.LoginResponseDTO;
import ifpb.edu.br.pj.ifpbichos.presentation.dto.UserRegistrationDTO;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private TokenService tokenService;
	
	
	
	
	@PostMapping("/login")
	public ResponseEntity login(@RequestBody AuthenticationDTO dto) {
		System.out.println("chegou aqui");
		var userNamePassword = new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword());
		var auth = this.authenticationManager.authenticate(userNamePassword);
		var token = tokenService.generateToken((User) auth.getPrincipal());
		
		return ResponseEntity.ok(new LoginResponseDTO(token));
	}
	
	//futura implementação
	
	@PostMapping("/userRegistration")
	public ResponseEntity userRegistration(@RequestBody UserRegistrationDTO dto) {
		if(this.userRepository.findByLogin(dto.getEmail())!= null) {
			return ResponseEntity.badRequest().build();
		}
		String encryptedPassword = new BCryptPasswordEncoder().encode(dto.getPassword());
		
		User newUser = new Donator(dto.getName(),dto.getPhoneNumber(),dto.getEmail(),dto.getLogin(),
				encryptedPassword,dto.getRegistration(),dto.getDonatorType());
		
		this.userRepository.save(newUser);
		
		return ResponseEntity.ok().build();		
	}
	
}
