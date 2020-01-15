package com.br.waldir.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.br.waldir.domain.User;
import com.br.waldir.repositories.UserRepository;
import com.br.waldir.security.UserSS;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{
	
	@Autowired
	private UserRepository repo;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = repo.findByEmail(email); //Procura no banco de dados o email
		
		if(user == null) { //Se imail não existir
			throw new UsernameNotFoundException("Email não existe: "+email);
		}
		
		return new UserSS(user.getId(),user.getEmail(),user.getSenha(), user.getPerfis());

	}
}
