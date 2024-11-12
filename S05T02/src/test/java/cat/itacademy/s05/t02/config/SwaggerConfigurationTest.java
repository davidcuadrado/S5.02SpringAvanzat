package cat.itacademy.s05.t02.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springdoc.core.models.GroupedOpenApi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

class SwaggerConfigurationTest {

	@InjectMocks
	private SwaggerConfiguration swaggerConfiguration;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testCustomOpenAPI() {
		OpenAPI openAPI = swaggerConfiguration.customOpenAPI();

		assertNotNull(openAPI);
		assertNotNull(openAPI.getInfo());
		assertEquals("The Pet API", openAPI.getInfo().getTitle());
		assertEquals("API for managing users and pets.", openAPI.getInfo().getDescription().trim());
		assertEquals("v1.0", openAPI.getInfo().getVersion());

		assertEquals("David Cuadrado", openAPI.getInfo().getContact().getName());
		assertEquals("dav.cuadrado@gmail.com", openAPI.getInfo().getContact().getEmail());

		SecurityScheme securityScheme = openAPI.getComponents().getSecuritySchemes().get("bearer-key");
		assertNotNull(securityScheme);
		assertEquals(SecurityScheme.Type.HTTP, securityScheme.getType());
		assertEquals("bearer", securityScheme.getScheme().trim());
		assertEquals("JWT", securityScheme.getBearerFormat());

		boolean hasUserTag = openAPI.getTags().stream().anyMatch(tag -> tag.getName().equals("User Management"));
		boolean hasPetTag = openAPI.getTags().stream().anyMatch(tag -> tag.getName().equals("Pet Management"));
		assertEquals(true, hasUserTag);
		assertEquals(true, hasPetTag);
	}

	@Test
	void testPublicApiGroupedOpenApi() {
		GroupedOpenApi publicApi = swaggerConfiguration.publicApi();

		assertNotNull(publicApi);
		assertEquals("public-api", publicApi.getGroup());

		List<String> pathsToMatch = publicApi.getPathsToMatch();
		assertNotNull(pathsToMatch);
		assertEquals(3, pathsToMatch.size());
		assertTrue(pathsToMatch.contains("/pet/**"));
		assertTrue(pathsToMatch.contains("/home/**"));
		assertTrue(pathsToMatch.contains("/register/**"));
	}

	@Test
	void testAdminApiGroupedOpenApi() {
		GroupedOpenApi adminApi = swaggerConfiguration.adminApi();

		assertNotNull(adminApi);
		assertEquals("admin-api", adminApi.getGroup());

		List<String> pathsToMatch = adminApi.getPathsToMatch();
		assertNotNull(pathsToMatch);
		assertEquals(1, pathsToMatch.size());
		assertTrue(pathsToMatch.contains("/admin/**"));
	}

	@Test
	void testUserApiGroupedOpenApi() {
		GroupedOpenApi userApi = swaggerConfiguration.userApi();

		assertNotNull(userApi);
		assertEquals("user-api", userApi.getGroup());

		List<String> pathsToMatch = userApi.getPathsToMatch();
		assertNotNull(pathsToMatch);
		assertEquals(1, pathsToMatch.size());
		assertTrue(pathsToMatch.contains("/user/**"));
	}
}
