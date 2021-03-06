package br.com.ermig.patrimonio.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.ermig.patrimonio.dto.UsuarioDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@EqualsAndHashCode(of = { "id" })
@JsonIgnoreProperties(allowSetters = true, value = { "senha" })
@Table(name = "usuarios", uniqueConstraints = @UniqueConstraint(name = "usuario_email_uk", columnNames = { "email" }))
public class Usuario implements UserDetails {

	private static final long serialVersionUID = 5366900798390254099L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotNull
	@NotBlank
	private String nome;

	@NotNull
	@NotBlank
	private String email;

	@NotNull
	@NotBlank
	private String senha;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private final List<Perfil> perfis = new ArrayList<>();

	public void setSenha(final String senha) {
		final PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		this.senha = encoder.encode(senha);
	}

	@Override
	@JsonIgnore
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.perfis;
	}

	@Override
	@JsonIgnore
	public String getUsername() {
		return this.email;
	}

	@Override
	@JsonIgnore
	public String getPassword() {
		return this.senha;
	}

	@Override
	@JsonIgnore
	public boolean isEnabled() {
		return true;
	}

	@Override
	@JsonIgnore
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	@JsonIgnore
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	@JsonIgnore
	public boolean isCredentialsNonExpired() {
		return true;
	}

	public Usuario(final String nome, final String email, final String senha) {
		this.nome = nome;
		this.email = email;
		this.setSenha(senha);
	}

	public Usuario(final UsuarioDTO usuarioDTO) {
		if (usuarioDTO != null) {
			this.setId(usuarioDTO.getId());
			this.setNome(usuarioDTO.getNome());
			this.setEmail(usuarioDTO.getEmail());
			this.setSenha(usuarioDTO.getSenha());
		}
	}

	public Usuario(String nome) {
		this.nome = nome;
	}

	@Override
	public String toString() {
		return String.format("Usuario [id=%s, nome=%s, email=%s, perfis=%s]", id, nome, email, toStringPerfis());
	}

	private List<String> toStringPerfis() {
		if (perfis != null) {
			return perfis.stream().limit(5).map(Perfil::getNome).collect(Collectors.toList());
		}
		return null;
	}
	
}
