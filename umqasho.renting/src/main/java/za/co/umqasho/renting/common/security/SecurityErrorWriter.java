package za.co.umqasho.renting.common.security;

import jakarta.servlet.http.HttpServletResponse;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

import static java.nio.file.Files.write;


public final class SecurityErrorWriter {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private SecurityErrorWriter(){}

    public static void writeUnauthorized(
            HttpServletResponse response,
            String code,
            String message
    ) throws IOException {

        write(
                response,
                HttpServletResponse.SC_UNAUTHORIZED,
                code,
                message
        );

    }

    public static void writeForbidden(
            HttpServletResponse response,
            String code,
            String message
    )throws IOException{
        write(
                response,
                HttpServletResponse.SC_FORBIDDEN,
                code,
                message
        );
    }

    private static void write(
            HttpServletResponse response,
            int status,
            String code,
            String message
    ) throws IOException {

        response.setStatus(status);
        response.setContentType("application/json");

        Map<String, Object> body = Map.of(
                "status", status,
                "code", code,
                "message", message
        );

        response.getWriter()
                .write(
                        OBJECT_MAPPER.writeValueAsString(body)
                );

    }

}
