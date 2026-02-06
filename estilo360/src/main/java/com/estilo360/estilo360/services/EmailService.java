package com.estilo360.estilo360.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Async;

/**
 * Servicio de negocio para el envío de correos electrónicos.
 * Proporciona métodos para enviar emails de verificación de cuenta
 * y de recuperación de contraseña de forma asíncrona.
 * 
 * @version 1.0
 */
@Service
public class EmailService {

    /** Componente para enviar correos electrónicos */
    private final JavaMailSender mailSender;

    /** URL base de la aplicación, utilizada en los emails */
    @Value("${app.base.url}")
    private String baseUrl;

    /**
     * Constructor que inyecta el componente JavaMailSender.
     * 
     * @param mailSender Componente para enviar correos electrónicos
     */
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Envía un correo electrónico de verificación de cuenta de forma asíncrona.
     * Incluye un código que el usuario debe ingresar para activar su cuenta.
     * 
     * @param destinatario Correo electrónico del usuario destinatario
     * @param codigo Código de verificación a enviar
     */
    @Async
    public void enviarEmailVerificacion(String destinatario, String codigo) {
        try {
            SimpleMailMessage mensaje = new SimpleMailMessage();
            mensaje.setTo(destinatario);
            mensaje.setSubject("Código de verificación - Estilo360");
            mensaje.setText(
                "¡Bienvenido a Estilo360!\n\n" +
                "Tu código de verificación es:\n\n" +
                "      " + codigo + "\n\n" +
                "Ingresa este código en la página de registro para activar tu cuenta.\n\n" +
                "Este código es válido por 24 horas.\n\n" +
                "Si no te registraste en Estilo360, ignora este mensaje.\n\n" +
                "Saludos,\n"
            );

            mailSender.send(mensaje);
            System.out.println("Email enviado a: " + destinatario + " con código: " + codigo);

        } catch (Exception e) {
            System.err.println("Error al enviar email: " + e.getMessage());
        }
    }

    /**
     * Envía un correo electrónico para restablecer la contraseña de forma asíncrona.
     * Incluye un código que el usuario debe ingresar para establecer una nueva contraseña.
     * 
     * @param destinatario Correo electrónico del usuario destinatario
     * @param codigo Código de recuperación de contraseña a enviar
     */
    @Async
    public void enviarEmailResetPassword(String destinatario, String codigo) {
        try {
            SimpleMailMessage mensaje = new SimpleMailMessage();
            mensaje.setTo(destinatario);
            mensaje.setSubject("Recuperar contraseña - Estilo360");
            mensaje.setText(
                "Hola,\n\n" +
                "Recibimos una solicitud para restablecer tu contraseña en Estilo360.\n\n" +
                "Tu código de recuperación es:\n\n" +
                "      " + codigo + "\n\n" +
                "Ingresa este código en la página para establecer una nueva contraseña.\n\n" +
                "Este código es válido por 24 horas.\n\n" +
                "Si no solicitaste este cambio, ignora este mensaje y tu contraseña permanecerá sin cambios.\n\n" +
                "Saludos,\n" +
                "El equipo de Estilo360"
            );

            mailSender.send(mensaje);
            System.out.println("Email de reset enviado a: " + destinatario + " con código: " + codigo);

        } catch (Exception e) {
            System.err.println("Error al enviar email de reset: " + e.getMessage());
        }
    }
}