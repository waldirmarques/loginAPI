package com.br.waldir.dto;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

public class PasswordDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	
	@NotEmpty(message = "Preenchimento obrigatório")
	private String senha;

	public PasswordDTO() {
		
	}

	public PasswordDTO(Integer id, String senha) {
		this.id = id;
		this.senha = senha;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
}