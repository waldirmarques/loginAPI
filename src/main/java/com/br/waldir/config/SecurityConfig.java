package com.br.waldir.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.br.waldir.security.JWTAuthenticationFilter;
import com.br.waldir.security.JWTAuthorizationFilter;
import com.br.waldir.security.JWTUtil;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) //Permite autorizar apenas alguns perfis
public class SecurityConfig extends  WebSecurityConfigurerAdapter{
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private Environment env;
	
	@Autowired
	private JWTUtil jwtUtil;
	
	private static final String[] PUBLIC_MATCHERS = { //Arraay dos endpoint que vão ser permitidos os acessos
			"/h2-console/**"
	};
	
	private static final String[] PUBLIC_MATCHERS_GET = { //Arraay dos endpoint que um usuario sem tá logado pode apenas recuperar os dados
			
	};
	
	private static final String[] PUBLIC_MATCHERS_POST = { //Arraay dos endpoint que um usuario sem tá logado pode apenas adicionar dados		
			"/user/**",
			"/auth/forgot/**"
	};
	
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		
		if(Arrays.asList(env.getActiveProfiles()).contains("test")) { //Teste que faz a liberação para uso dos bancos para
																		//teste como o h2-console e demais bancos.
			http.headers().frameOptions().disable();
		}
		
		http.cors().and().csrf().disable();  //desativa função que protege contra ataque de armazenamento de em seção
		http.authorizeRequests()
			.antMatchers(HttpMethod.GET, PUBLIC_MATCHERS_POST).permitAll() //Permite post para endpoint de quem ta na variavel
			.antMatchers(HttpMethod.GET, PUBLIC_MATCHERS_GET).permitAll() //Só vai permitir o metodo GET para quem tiver nessa lista
			.antMatchers(PUBLIC_MATCHERS).permitAll() //Permite acesso aos entenpoid da variavel Public_Matchers
			.anyRequest().authenticated();  //Estou pedindo autenticação para o restante dos endpoint
		http.addFilter(new JWTAuthenticationFilter(authenticationManager(), jwtUtil));
		http.addFilter(new JWTAuthorizationFilter(authenticationManager(), jwtUtil, userDetailsService));
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
	
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception{
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
	}
	
	@Bean
	CorsConfigurationSource corsConfigurationSource() { //Permite acesso aos endpoint com as configurações basicas
	    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
	    return source;
	}
	
	@Bean
	public  BCryptPasswordEncoder  bCryptPasswordEncoder() { //Faz com que a senha quando salva no banco de dados senha incriptada
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public CorsFilter corsFilter() {
	    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    final CorsConfiguration config = new CorsConfiguration();
	    config.setAllowCredentials(true);
	    config.addAllowedOrigin("*");
	    config.addAllowedHeader("*");
	    config.addAllowedMethod("OPTIONS");
	    config.addAllowedMethod("HEAD");
	    config.addAllowedMethod("GET");
	    config.addAllowedMethod("PUT");
	    config.addAllowedMethod("POST");
	    config.addAllowedMethod("DELETE");
	    config.addAllowedMethod("PATCH");
	    source.registerCorsConfiguration("/**", config);
	    return new CorsFilter(source);
	}
}
