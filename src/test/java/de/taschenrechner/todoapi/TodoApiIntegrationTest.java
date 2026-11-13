package de.taschenrechner.todoapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.taschenrechner.todoapi.dto.LoginRequest;
import de.taschenrechner.todoapi.dto.LoginResponse;
import de.taschenrechner.todoapi.dto.TodoCreateDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TodoApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    // NEU: ObjectMapper direkt erstellen
    private final ObjectMapper objectMapper = new ObjectMapper();

    private String loginUndGetToken() throws Exception {
        LoginRequest login = new LoginRequest("user", "user");

        MvcResult result = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(login)))
            .andExpect(status().isOk())
            .andReturn();

        LoginResponse response = objectMapper.readValue(
            result.getResponse().getContentAsString(),
            LoginResponse.class
        );
        return response.token();
    }

    @Test
    void vollstaendigerCrudFlow() throws Exception {
        String token = loginUndGetToken();

        TodoCreateDTO dto = new TodoCreateDTO("Integration-Test", false);

        mockMvc.perform(post("/todos")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isCreated());

        mockMvc.perform(get("/todos")
                .header("Authorization", "Bearer " + token))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].titel").value("Integration-Test"));
    }

    @Test
    void loginMitFalschemPasswort_gibt401() throws Exception {
        LoginRequest login = new LoginRequest("user", "falsch");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(login)))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void adminEndpunktMitUserToken_gibt403() throws Exception {
        String token = loginUndGetToken();

        mockMvc.perform(get("/admin/todos")
                .header("Authorization", "Bearer " + token))
            .andExpect(status().isForbidden());
    }
}