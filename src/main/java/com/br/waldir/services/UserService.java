package com.br.waldir.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.br.waldir.domain.User;
import com.br.waldir.domain.enums.Perfil;
import com.br.waldir.dto.NomeDTO;
import com.br.waldir.dto.PasswordDTO;
import com.br.waldir.dto.UserDTO;
import com.br.waldir.repositories.UserRepository;
import com.br.waldir.security.UserSS;
import com.br.waldir.services.exceptions.AuthorizationException;
import com.br.waldir.services.exceptions.DataIntegrityException;
import com.br.waldir.services.exceptions.ObjectNotFoundException;

@Service
public class UserService {
	
	@Autowired 
	private BCryptPasswordEncoder pe; // usado para criptrografar a senha
	
	@Autowired //Essa anotação faz com que o objeto seja estanciado assim como está.
	private UserRepository repo;
	
	public User find(Integer id) throws ObjectNotFoundException {		
		UserSS user = UserServiceService.authenticated();
		if (user==null || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId())) {
			throw new AuthorizationException("Acesso negado");
		}
		
		Optional<User> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + User.class.getName()));
	}

	@Transactional
	public User insert(User obj) {
		obj.setId(null);
		return repo.save(obj);
	}

	public User update(User obj){
		User newObj = find(obj.getId());
		updateData(newObj,obj);
		return repo.save(obj);
	}
	
	public User update(PasswordDTO obj) throws ObjectNotFoundException {		
		User newObj = find(obj.getId());
		updatePassword(newObj,obj);
		return repo.save(newObj);
	}
	
	public User update(NomeDTO obj) throws ObjectNotFoundException {		
		User newObj = find(obj.getId());
		updateNome(newObj,obj);
		return repo.save(newObj);
	}
	
	public NomeDTO nomeDTO(NomeDTO nomeDto) {
		return new NomeDTO(nomeDto.getId(), nomeDto.getNome());
	}	

	public PasswordDTO passwordDTO(PasswordDTO objDto) {		
		return new PasswordDTO(objDto.getId(), pe.encode(objDto.getSenha()));
	}

	public void delete(Integer id){
		find(id);
		try {
			repo.deleteById(id);
		}
		catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possivevel excluir uma User que possui profutos");
		}
	}

	public List<User> findAll() {
		return repo.findAll();
	}
	
	public Page<User> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		PageRequest pageRequest = PageRequest.of(page, linesPerPage , Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
	}
	
	public User fromDTO(UserDTO objDto) {
		return new User(objDto.getId(),objDto.getNome(), objDto.getEmail(), pe.encode(objDto.getSenha()));
	}
	
	private void updateData(User newObj,User obj) {
		newObj.setNome(obj.getNome());
	}
	
	private void updateNome(User newObj, NomeDTO obj) {
		newObj.setId(obj.getId());
		newObj.setNome(obj.getNome());
	}
	
	private void updatePassword(User newObj,PasswordDTO obj) {
		newObj.setId(obj.getId());
		newObj.setSenha(obj.getSenha());
	}

	public User findByEmail(String email){
		
		UserSS user = UserServiceService.authenticated();
		if (user == null || !user.hasRole(Perfil.ADMIN) && !email.equals(user.getUsername())) {
			throw new AuthorizationException("Acesso negado");
		}
	
		User obj = repo.findByEmail(email);
		if (obj == null) {
			throw new ObjectNotFoundException(
					"Objeto não encontrado! Id: " + user.getId() + ", Tipo: " + User.class.getName());
		}
		return obj;
	}

	
}
