package com.example.PathToGrade.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CursoControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    private Long cursoId;
    private Long disciplinaId;

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

        cursoId = Long.valueOf(responseString);
    }

    @Test
    @Order(3)
    public void testPutCurso() throws Exception {
        String cursoContent = "{\"nome\":\"Curso Mudado\", \"qtdPeriodos\":12}";
        String url = "/curso/" + cursoId;

        mockMvc.perform(put(url).contentType(MediaType.APPLICATION_JSON).content(cursoContent))
        .andExpect(status().isOk());

        mockMvc.perform(get(url).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.nome").value("Curso Mudado"))
        .andExpect(jsonPath("$.qtdPeriodos").value(12));
    }

    @Test
    @Order(3)
    public void testUnknownDisciplinaId() throws Exception {
        String url = "/curso/" + cursoId + "/disciplina/123123123123";

        mockMvc.perform(get(url).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.error").value("Not Found"))
        .andExpect(jsonPath("$.message").value("Disciplina não encontrada com id 123123123123"));
    }

    @Test
    @Order(4)
    public void testPostDisciplina() throws Exception {
        String disciplinaContent = "{\"nome\":\"Disciplina Teste\", \"codigo\":\"COD00001\", \"cargaHoraria\": 60, \"periodo\": 1}";

        String url = "/curso/" + cursoId + "/disciplina";

        ResultActions result = mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(disciplinaContent))
        .andExpect(status().isCreated());

        String responseString = result.andReturn().getResponse().getContentAsString();

        disciplinaId = Long.valueOf(responseString);
    }

    @Test
    @Order(5)
    public void testDeleteCurso() throws Exception {
        String url = "/curso/" + cursoId;

        mockMvc.perform(delete(url).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

        mockMvc.perform(get(url).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
    }
}
