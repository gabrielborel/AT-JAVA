package org.example.service;

import org.example.dto.UsuarioInputDTO;
import org.example.dto.UsuarioOutputDTO;
import org.example.model.Usuario;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

public class UsuarioService {
    private final List<Usuario> listaUsuarios = new ArrayList<>();
    private final ModelMapper modelMapper = new ModelMapper();

    public List<UsuarioOutputDTO> listar() {
        List<UsuarioOutputDTO> listaUsuariosOutputDTO = new ArrayList<>();

        for (Usuario usuario : listaUsuarios) {
            UsuarioOutputDTO usuarioOutputDTO = modelMapper.map(usuario, UsuarioOutputDTO.class);
            listaUsuariosOutputDTO.add(usuarioOutputDTO);
        }

        return listaUsuariosOutputDTO;
    }

    public void inserir(UsuarioInputDTO usuarioInputDTO) {
        Usuario usuario = modelMapper.map(usuarioInputDTO, Usuario.class);
        listaUsuarios.add(usuario);
    }

    public void alterar(UsuarioInputDTO usuarioInputDTO) {
        Usuario usuario = modelMapper.map(usuarioInputDTO, Usuario.class);
        listaUsuarios.set(usuario.getId(), usuario);
    }

    public UsuarioOutputDTO buscar(int id) {
        Usuario usuario = listaUsuarios.get(id);
        return modelMapper.map(usuario, UsuarioOutputDTO.class);
    }

    public void excluir(int id) {
        listaUsuarios.remove(id);
    }
}
