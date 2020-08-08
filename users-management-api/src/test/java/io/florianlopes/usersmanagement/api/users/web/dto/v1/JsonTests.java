package io.florianlopes.usersmanagement.api.users.web.dto.v1;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.json.JacksonTester;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonTests {

    private JacksonTester jacksonTester;

    @BeforeEach
    void setUp() {
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @Test
    void testWriteJson() throws IOException {
        final CreateUserRequest createUserRequest =
                new CreateUserRequest("John", "Doe", "john.doe@email.com", "test");
        assertThat(jacksonTester.write(createUserRequest).getJson())
                .isEqualTo(
                        "{\"firstName\":\"John\",\"lastName\":\"Doe\",\"email\":\"john.doe@email.com\",\"password\":\"test\"}");
    }
}
