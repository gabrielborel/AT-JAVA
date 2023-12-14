package org.example.controller;

import static spark.Spark.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.UsuarioInputDTO;
import org.example.dto.UsuarioOutputDTO;
import org.example.service.UsuarioService;

import java.util.List;

public class UsuarioController {
    private final UsuarioService usuarioService = new UsuarioService();
    private final ObjectMapper objectMapper = new ObjectMapper();


    public void respostaRequisicoes() {
        get("/usuarios", (req, res) -> {
            List<UsuarioOutputDTO> usuariosOutputDTO = usuarioService.listar();

            String respostaJson = objectMapper.writeValueAsString(usuariosOutputDTO);
            res.status(200);
            res.type("application/json");

            return respostaJson;
        });

        get("/usuarios/:id", (req, res) -> {
            int id = Integer.parseInt(req.params("id"));
            UsuarioOutputDTO usuarioOutputDTO = usuarioService.buscar(id);

            String respostaJson = objectMapper.writeValueAsString(usuarioOutputDTO);
            res.status(200);
            res.type("application/json");

            return respostaJson;
        });

        delete("/usuarios/:id", (req, res) -> {
            int id = Integer.parseInt(req.params("id"));
            usuarioService.excluir(id);

            res.status(204);

            return "";
        });

        post("/usuarios", (req, res) -> {
            UsuarioInputDTO usuarioInputDTO = objectMapper.readValue(req.body(), UsuarioInputDTO.class);
            usuarioService.inserir(usuarioInputDTO);

            res.status(201);

            return "";
        });

        put("/usuarios", (req, res) -> {
            UsuarioInputDTO usuarioInputDTO = objectMapper.readValue(req.body(), UsuarioInputDTO.class);
            usuarioService.alterar(usuarioInputDTO);

            res.status(204);

            return "";
        });
    }

}
