package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.UsuarioInputDTO;
import org.example.service.UsuarioService;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UsuarioServiceTest {
    private int index = 1;
    private final URL API_URL = new URL("http://localhost:4567/usuarios");

    public UsuarioServiceTest() throws MalformedURLException {
    }

    @Test
    public void testInsercaoUsuario() {
        UsuarioService usuarioService = new UsuarioService();

        UsuarioInputDTO usuarioInputDTO = new UsuarioInputDTO();
        usuarioInputDTO.setId(1);
        usuarioInputDTO.setNome("Gabriel Borel");
        usuarioInputDTO.setSenha("123456");

        usuarioService.inserir(usuarioInputDTO);
        assertEquals(1, usuarioService.listar().size());
    }

    @Test
    public void testListaUsuarios() throws IOException {
        HttpURLConnection connection = (HttpURLConnection) API_URL.openConnection();
        connection.setRequestMethod("GET");

        assertEquals(200, connection.getResponseCode());
    }

    @Test
    public void testInsercaoUsuariosAPIExterna() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        URL randomUserApiUrl = new URL("https://randomuser.me/api");
        HttpURLConnection connection = (HttpURLConnection) randomUserApiUrl.openConnection();

        connection.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        JsonNode usuarioResposta = objectMapper.readTree(response.toString()).path("results").get(0);
        UsuarioInputDTO usuarioInputDTO = new UsuarioInputDTO();
        usuarioInputDTO.setId(index++);
        usuarioInputDTO.setNome(usuarioResposta.path("name").path("first").asText());
        usuarioInputDTO.setSenha(usuarioResposta.path("login").path("password").asText());

        HttpURLConnection apiURLConnection = (HttpURLConnection) API_URL.openConnection();
        apiURLConnection.setRequestMethod("POST");
        apiURLConnection.setRequestProperty("Content-Type", "application/json");
        apiURLConnection.setDoOutput(true);

        String jsonRequestBody = objectMapper.writeValueAsString(usuarioInputDTO);
        try (OutputStream outputStream = apiURLConnection.getOutputStream()) {
            byte[] input = jsonRequestBody.getBytes(StandardCharsets.UTF_8);
            outputStream.write(input, 0, input.length);
        }

        assertEquals(201, apiURLConnection.getResponseCode());
    }
}
