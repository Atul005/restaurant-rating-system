package com.the_review_company.restaurant_review_system.config;

import com.the_review_company.restaurant_review_system.domain.entities.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CustomJwtToUserConvertor implements Converter<Jwt, AbstractAuthenticationToken> {


    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        User user = userFromJwt(jwt);
        List<SimpleGrantedAuthority> grantedAuthorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                .toList();
        return new UsernamePasswordAuthenticationToken(
                user, null, grantedAuthorities
        );

    }

    private User userFromJwt(Jwt jwt) {
        User user = new User();
        user.setId(jwt.getSubject());
        user.setUsername(jwt.getClaimAsString("preferred_username"));
        user.setGivenName(jwt.getClaimAsString("given_name"));
        user.setFamilyName(jwt.getClaimAsString("family_name"));
        user.setEmail(jwt.getClaimAsString("email"));

        Map<String, Object> allClaims = new HashMap<>(jwt.getClaims());
        allClaims.remove("sub");
        allClaims.remove("preferred_username");
        allClaims.remove("given_name");
        allClaims.remove("family_name");
        allClaims.remove("email");

        List<String> roles = jwt.getClaimAsStringList("roles");

        user.setAdditionalProperties(allClaims);
        return user;
    }
}
