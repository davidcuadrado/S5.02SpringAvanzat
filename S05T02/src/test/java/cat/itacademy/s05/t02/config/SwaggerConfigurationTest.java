package cat.itacademy.s05.t02.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springdoc.core.models.GroupedOpenApi;

class SwaggerConfigurationTest {

    @InjectMocks
    private SwaggerConfiguration swaggerConfiguration;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void api_returnsGroupedOpenApiBean() {
        GroupedOpenApi groupedOpenApi = swaggerConfiguration.api();

        assertNotNull(groupedOpenApi, "El bean GroupedOpenApi no debería ser nulo");

        assertEquals("pet-api", groupedOpenApi.getGroup(), "El grupo debería llamarse 'pet-api'");

        String[] pathsToMatch =  groupedOpenApi.getPathsToMatch();
        assertNotNull(pathsToMatch, "Los paths configurados no deberían ser nulos");
        assertEquals(5, pathsToMatch.length, "El número de rutas configuradas debería ser 5");
        assertEquals("/user/**", pathsToMatch[0], "La primera ruta debería ser '/user/**'");
        assertEquals("/pet/**", pathsToMatch[1], "La segunda ruta debería ser '/pet/**'");
        assertEquals("/home/**", pathsToMatch[2], "La tercera ruta debería ser '/home/**'");
        assertEquals("/admin/**", pathsToMatch[3], "La cuarta ruta debería ser '/admin/**'");
        assertEquals("/register/**", pathsToMatch[4], "La quinta ruta debería ser '/register/**'");
    }
}
