    package com.the_review_company.restaurant_review_system.config;

    import lombok.RequiredArgsConstructor;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.http.HttpMethod;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
    import org.springframework.security.config.http.SessionCreationPolicy;
    import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
    import org.springframework.security.web.SecurityFilterChain;

    @Configuration
    @RequiredArgsConstructor
    public class SecurityConfig {

        private final CustomJwtToUserConvertor customJwtToUserConvertor;

        @Bean
        public SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {

            httpSecurity
                    .authorizeHttpRequests(
                            auth ->
                                    auth
                                            .requestMatchers(HttpMethod.GET, "/api/photos/**").permitAll()
                                            .anyRequest().authenticated()
                    )

                    .oauth2ResourceServer(oauth2 ->
                            oauth2.jwt( jwt ->
                                    jwt.jwtAuthenticationConverter(customJwtToUserConvertor)

                            ))
                    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .csrf(AbstractHttpConfigurer::disable);


            return httpSecurity.build();
        }

//        @Bean
//        public JwtAuthenticationConverter jwtAuthenticationConverter(){
//            JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
//            jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(customJwtToUserConvertor);
//            return new JwtAuthenticationConverter();
//        }



    }
