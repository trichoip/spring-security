package com.security;

import com.security.domain.Role;
import com.security.domain.User;
import com.security.domain.enumeration.RoleUser;
import com.security.domain.enumeration.UserStatus;
import com.security.repository.RoleRepository;
import com.security.repository.UserRepository;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@SecurityScheme(name = "token_auth", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT")
@OpenAPIDefinition(
  info = @Info(title = "Swagger for System", description = "This is list of endpoints and documentations of REST API for System", version = "1.0"),
  servers = {
    @Server(url = "http://localhost:8080", description = "Local development server"),
    @Server(url = "http://localhost:5000", description = "Local production server"),
  },
  tags = { @Tag(name = "authentication", description = "REST API endpoints for authentication") }
)
public class SecurityApplication {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoleRepository roleRepository;

  // @PostConstruct
  public void initUser() {
    List<Role> roleAdmin = new ArrayList<Role>() {
      {
        add(roleRepository.findByName(RoleUser.ADMIN).orElseGet(() -> Role.builder().name(RoleUser.ADMIN).build()));
      }
    };
    List<Role> roleUser = new ArrayList<Role>() {
      {
        add(roleRepository.findByName(RoleUser.USER).orElseGet(() -> Role.builder().name(RoleUser.USER).build()));
      }
    };

    List<User> users = Stream
      .of(
        new User(null, "trichoip", "1", roleAdmin, UserStatus.ACTIVE),
        new User(null, "test1", "1", roleUser, UserStatus.ACTIVE),
        new User(null, "test2", "1", roleAdmin, UserStatus.ACTIVE),
        new User(null, "test3", "1", roleUser, UserStatus.ACTIVE),
        new User(null, "test4", "1", roleUser, UserStatus.ACTIVE)
      )
      .collect(Collectors.toList());

    userRepository.saveAll(users);
  }

  public static void main(String[] args) {
    SpringApplication.run(SecurityApplication.class, args);
  }
}
