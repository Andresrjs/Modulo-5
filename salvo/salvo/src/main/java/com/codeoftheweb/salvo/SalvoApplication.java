package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

	@Bean
	public CommandLineRunner initData(PlayerRepository repo1,	GameRepository	gameRepository, GamePlayerRepository gameplayerRepository,  ShipRepository shipRepository, SalvoRepository salvoRepository, ScoreRepository scoreRepository) {
		return (args) -> {/*
			// save a couple of customers
			Player P1 = new Player("j.bauer@ctu.gov", passwordEncoder().encode("24"));
			Player P2 = new Player("c.obrian@ctu.gov", passwordEncoder().encode("24"));
			Player P3 = new Player("kim_bauer@gtmail.com", passwordEncoder().encode("24"));
			Player P4 = new Player("t.almeida@ctu.gov", passwordEncoder().encode("24"));

			repo1.save(P1);
			repo1.save(P2);
			repo1.save(P3);
			repo1.save(P4);


			Date date = new Date();
			Date date2 = Date.from(date.toInstant().plusSeconds(3600));
			Date date3 = Date.from(date2.toInstant().plusSeconds(3600));

			Game G1 = new Game(date);
			Game G2 = new Game(date2);
			Game G3 = new Game(date3);

			gameRepository.save(G1);
			gameRepository.save(G2);
			gameRepository.save(G3);

			GamePlayer GP1 = new GamePlayer(date,G1,P1);
			GamePlayer GP2 = new GamePlayer(date,G1,P2);
			GamePlayer GP3 = new GamePlayer(date,G2,P3);
			GamePlayer GP4 = new GamePlayer(date,G2,P4);
  			GamePlayer GP5 = new GamePlayer(date,G3,P4);


			gameplayerRepository.save(GP1);
			gameplayerRepository.save(GP2);
			gameplayerRepository.save(GP3);
			gameplayerRepository.save(GP4);
			gameplayerRepository.save(GP5);

            Set<String> shipLocationgp1 = new HashSet<>();
            Set<String> shipLocationgp2 =  new HashSet<>();
            Set<String> shipLocationgp3 = new HashSet<>();
            Set<String> shipLocationgp4 =  new HashSet<>();
            Set<String> shipLocationgp5 =  new HashSet<>();

            shipLocationgp1.add ("H2");
            shipLocationgp1.add ("H3");
            shipLocationgp1.add ("H4");
            shipLocationgp2.add ("E1");
            shipLocationgp2.add ("F1");
            shipLocationgp2.add ("G1");
            shipLocationgp3.add ("B4");
            shipLocationgp3.add ("B5");
            shipLocationgp4.add ("B5");
            shipLocationgp4.add ("C5");
            shipLocationgp4.add ("D5");
            shipLocationgp5.add ("F1");
            shipLocationgp5.add ("F2");
            shipLocationgp5.add ("F3");
// ShipTypes.
            String shipTypeYate = "Yate";
            String shipTypeVelero = "Velero";
            String shipTypeSubmarino = "Submarino";
            String shipTypeCanoa = "Canoa";

            Ship gp1Uno  = new Ship(GP1,shipTypeCanoa,shipLocationgp1);
            Ship gp1Dos  = new Ship(GP1,shipTypeVelero, shipLocationgp2);
            Ship gp1Tres  = new Ship(GP1,shipTypeSubmarino, shipLocationgp3);
            Ship gp2Uno  = new Ship(GP2,shipTypeYate, shipLocationgp4);
            Ship gp2Dos  = new Ship(GP2,shipTypeSubmarino, shipLocationgp5);
            Ship gp2Tres  = new Ship(GP2,shipTypeVelero, shipLocationgp1);

            shipRepository.save(gp1Uno);
            shipRepository.save(gp1Dos);
            shipRepository.save(gp1Tres);
            shipRepository.save(gp2Uno);
            shipRepository.save(gp2Dos);
            shipRepository.save(gp2Tres);

            Set<String> salvoLocationgp1 = new HashSet<>();
            Set<String> salvoLocationgp2 =  new HashSet<>();
            Set<String> salvoLocationgp3 = new HashSet<>();
            Set<String> salvoLocationgp4 =  new HashSet<>();
            Set<String> salvoLocationgp5 =  new HashSet<>();

            salvoLocationgp1.add ("H2");
            salvoLocationgp2.add ("G1");
            salvoLocationgp3.add ("G3");
            salvoLocationgp4.add ("C5");
            salvoLocationgp5.add ("F1");
            salvoLocationgp5.add ("F3");

            Salvo salvo1 = new Salvo(GP1,salvoLocationgp1,1);
            Salvo salvo2 = new Salvo(GP1, salvoLocationgp2,2);
            Salvo salvo3 = new Salvo(GP2,salvoLocationgp3,1);
            Salvo salvo4 = new Salvo(GP3,salvoLocationgp4,2);

            salvoRepository.save(salvo1);
            salvoRepository.save(salvo2);
            salvoRepository.save(salvo3);
            salvoRepository.save(salvo4);

            Score score1 = new Score(P1,G1,(float) 0.5,date);
            Score score2 = new Score(P2,G1,(float) 1,date);
            Score score3 = new Score(P3,G2,(float) 0.5,date);
            Score score4 = new Score(P4,G2,(float) 0,date);

            scoreRepository.saveAll(Arrays.asList(score1,score2,score3,score4));

		*/};
	}
}

@Configuration
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {

    @Autowired
    PlayerRepository playerRepo;

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(inputName-> {
            Player player = playerRepo.findByUserName(inputName);
            if (player != null) {
                return new User(player.getUserName(), player.getPassword(),
                        AuthorityUtils.createAuthorityList("USER"));
            } else {
                throw new UsernameNotFoundException("Unknown user: " + inputName);
            }
        });
    }
}

@Configuration
@EnableWebSecurity
class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/web/games.html").permitAll()
                .antMatchers("/web/**").permitAll()
                .antMatchers("/api/games").permitAll()
                .antMatchers("/api/players").permitAll()
                .antMatchers("/api/game_view/*").hasAuthority("USER")
                .antMatchers("/rest").permitAll()
                .anyRequest().permitAll();
        http.formLogin()
                .usernameParameter("name")
                .passwordParameter("pwd")
                .loginPage("/api/login");

        http.logout().logoutUrl("/api/logout");

        // turn off checking for CSRF tokens
        http.csrf().disable();

        // if user is not authenticated, just send an authentication failure response
        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if login is successful, just clear the flags asking for authentication
        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

        // if login fails, just send an authentication failure response
        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if logout is successful, just send a success response
        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
    }
    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }
}
