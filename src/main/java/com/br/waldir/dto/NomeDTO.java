package com.br.waldir.dto;

import javax.validation.constraints.NotEmpty;

public class NomeDTO {
	
	private Integer id;
	
	@NotEmpty(message = "Preenchimento obrigatório")
	private String nome;
	
	public NomeDTO() {
		
	}

	public NomeDTO(Integer id, String nome) {
		this.id = id;
		this.nome = nome;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}	
}
