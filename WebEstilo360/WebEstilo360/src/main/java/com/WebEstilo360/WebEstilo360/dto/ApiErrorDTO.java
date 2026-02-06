package com.WebEstilo360.WebEstilo360.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiErrorDTO {
    private String mensaje;
    private int status;
}