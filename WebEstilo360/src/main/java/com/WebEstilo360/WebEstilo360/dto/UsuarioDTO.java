package com.WebEstilo360.WebEstilo360.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
    private Long id_usuario;
    private String nombre_completo;
    private String correo;
    private String movil;
    private String rol;
    private String contrasena; // Solo para crear/actualizar, nunca se mostrará
}