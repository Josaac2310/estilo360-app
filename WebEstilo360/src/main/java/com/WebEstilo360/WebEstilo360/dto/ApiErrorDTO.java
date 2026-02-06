package com.WebEstilo360.WebEstilo360.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiErrorDTO {
    private String mensaje; // Mensaje amigable del error
    private int status;     // Código HTTP

    // Opcional: agregar timestamp para depuración
    private long timestamp = System.currentTimeMillis();

}