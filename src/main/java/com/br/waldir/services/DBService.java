package com.br.waldir.services;

import java.text.ParseException;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.br.waldir.domain.User;
import com.br.waldir.domain.enums.Perfil;
import com.br.waldir.repositories.UserRepository;

@Service
public class DBService {
	
	@Autowired
	private BCryptPasswordEncoder pe;
	
	@Autowired
	private UserRepository userRepository;

	public void instantiateTestDatabase() throws ParseException{
		
		User user = new User(null,"MainAdmin","admin@gmail.com",pe.encode("admin"));
		user.addPerfil(Perfil.ADMIN);
		
		userRepository.saveAll(Arrays.asList(user)); 
	}
}

