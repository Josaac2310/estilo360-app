package com.WebEstilo360.WebEstilo360.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServicioDTO {
    private Long id_servicio;
    private String nombre;
    private String descripcion;
    private Double precio;
    private Integer duracion_minutos;
}