package cat.itacademy.s05.t02.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
        // Ejecución del método
        GroupedOpenApi groupedOpenApi = swaggerConfiguration.api();

        // Verificación de que el bean no es nulo
        assertNotNull(groupedOpenApi, "El bean GroupedOpenApi debería ser no nulo");

        // Verificación de que el grupo se llama "pet-api"
        assertEquals("pet-api", groupedOpenApi.getGroup(), "El grupo debería llamarse 'pet-api'");

        // Verificación de que los paths coinciden con los definidos
        assertEquals(3, groupedOpenApi.getPathsToMatch().length, "El número de rutas configuradas debería ser 3");
        assertEquals("/user/**", groupedOpenApi.getPathsToMatch()[0], "La primera ruta debería ser '/user/**'");
        assertEquals("/pet/**", groupedOpenApi.getPathsToMatch()[1], "La segunda ruta debería ser '/pet/**'");
        assertEquals("/app", groupedOpenApi.getPathsToMatch()[2], "La tercera ruta debería ser '/app'");
    }
}
