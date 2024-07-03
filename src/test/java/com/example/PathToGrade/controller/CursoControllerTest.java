package com.example.PathToGrade.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import org.junit.jupiter.api.Order;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
public class CursoControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    private Long id;

    @Test
    @Order(1)
    public void testUnknownCursoId() throws Exception {
        mockMvc.perform(get("/curso/123123123123").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.error").value("Not Found"))
        .andExpect(jsonPath("$.message").value("Curso não encontrado com id 123123123123"));
    }

    @Test
    @Order(2)
    public void testPostCurso() throws Exception {
        String cursoContent = "{\"nome\":\"Curso Teste\", \"qtdPeriodos\":10}";

        ResultActions result = mockMvc.perform(post("/curso").contentType(MediaType.APPLICATION_JSON)
        .content(cursoContent))
        .andExpect(status().isCreated());

        String responseString = result.andReturn().getResponse().getContentAsString();

        id = Long.valueOf(responseString);
    }

    @Test
    @Order(3)
    public void testUnknownDisciplinaId() throws Exception {
        String url = "/curso/" + id + "/disciplina/123123123123";

        mockMvc.perform(get(url).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.error").value("Not Found"))
        .andExpect(jsonPath("$.message").value("Disciplina não encontrada com id 123123123123"));
    }

    @Test
    @Order(4)
    public void testDeleteCurso() throws Exception {
        String urlDelete = "/curso/" + id;

        mockMvc.perform(delete(urlDelete).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

        String urlGet = "/curso/" + id;

        mockMvc.perform(get(urlGet).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
    }
}
