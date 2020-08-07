package io.florianlopes.usersmanagement.api.users.web;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v3/api-docs")
public class OpenAPIUsersController {

    @GetMapping("/users.openapi.yaml")
    public void getApiDocs(HttpServletResponse response) throws IOException {
        final ClassPathResource usersOpenApiResource =
                new ClassPathResource("openapi/users.openapi.yaml");
        if (!usersOpenApiResource.exists()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/yaml");

        StreamUtils.copy(usersOpenApiResource.getInputStream(), response.getOutputStream());
    }
}
