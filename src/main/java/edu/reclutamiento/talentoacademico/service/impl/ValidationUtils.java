package edu.reclutamiento.talentoacademico.service.impl;

final class ValidationUtils {
    // Acepta letras con tildes, numeros y separadores comunes para nombres de personas, areas u ofertas.
    private static final String NAME_PATTERN = "^[\\p{L}\\p{M}0-9 .,'-]+$";
    // Validacion simple de email: suficiente para formularios internos sin implementar reglas completas de RFC.
    private static final String EMAIL_PATTERN = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$";

    private ValidationUtils() {
    }

    static void validarNombre(String valor, String campo) {
        String texto = normalizar(valor);
        if (texto.isBlank()) {
            throw new IllegalArgumentException(campo + " es obligatorio.");
        }
        if (texto.length() < 2 || texto.length() > 120) {
            throw new IllegalArgumentException(campo + " debe tener entre 2 y 120 caracteres.");
        }
        if (texto.matches("\\d+")) {
            throw new IllegalArgumentException(campo + " no puede contener solo numeros.");
        }
        if (!texto.matches(NAME_PATTERN)) {
            throw new IllegalArgumentException(campo + " contiene caracteres no permitidos.");
        }
    }

    static void validarEmail(String email) {
        String texto = normalizar(email);
        if (texto.isBlank()) {
            throw new IllegalArgumentException("El email es obligatorio.");
        }
        if (texto.length() > 120 || !texto.matches(EMAIL_PATTERN)) {
            throw new IllegalArgumentException("El formato del email no es valido.");
        }
    }

    static void validarTextoObligatorio(String valor, String campo, int maximo) {
        String texto = normalizar(valor);
        if (texto.isBlank()) {
            throw new IllegalArgumentException(campo + " es obligatorio.");
        }
        if (texto.length() > maximo) {
            throw new IllegalArgumentException(campo + " no debe superar " + maximo + " caracteres.");
        }
    }

    static void validarTextoOpcional(String valor, String campo, int maximo) {
        String texto = normalizar(valor);
        // Si el campo opcional viene vacio, solo se valida longitud cuando realmente hay contenido.
        if (!texto.isBlank() && texto.length() > maximo) {
            throw new IllegalArgumentException(campo + " no debe superar " + maximo + " caracteres.");
        }
    }

    static void validarEnteroNoNegativo(Integer valor, String campo) {
        if (valor != null && valor < 0) {
            throw new IllegalArgumentException(campo + " no puede ser negativo.");
        }
    }

    static String normalizar(String valor) {
        return valor == null ? "" : valor.trim();
    }
}
