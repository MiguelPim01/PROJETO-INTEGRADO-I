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

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CursoControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    private Long cursoId;
    private Long disciplinaIdA;
    private Long disciplinaIdB;

    @Test
    @Order(1)
    public void testUnknownCursoId() throws Exception {
        mockMvc.perform(get("/curso/123123123123").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.error").value("Not Found"))
        .andExpect(jsonPath("$.message").value("Curso não encontrado com id 123123123123"));
    }

    @Test
    @Order(1)
    public void testPostCurso() throws Exception {
        String cursoContent = "{\"nome\":\"Curso Teste\", \"qtdPeriodos\":10}";

        ResultActions result = mockMvc.perform(post("/curso").contentType(MediaType.APPLICATION_JSON)
        .content(cursoContent))
        .andExpect(status().isCreated());

        String responseString = result.andReturn().getResponse().getContentAsString();

        cursoId = Long.valueOf(responseString);
    }

    @Test
    @Order(2)
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
    @Order(2)
    public void testUnknownDisciplinaId() throws Exception {
        String url = "/curso/" + cursoId + "/disciplina/123123123123";

        mockMvc.perform(get(url).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.error").value("Not Found"))
        .andExpect(jsonPath("$.message").value("Disciplina não encontrada com id 123123123123"));
    }

    @Test
    @Order(2)
    public void testPostDisciplina() throws Exception {
        String disciplinaContentA = "{\"nome\":\"Disciplina Teste\", \"codigo\":\"COD00001\", \"cargaHoraria\": 60, \"periodo\": 1}";
        String disciplinaContentB = "{\"nome\":\"Disciplina PreRequisito\", \"codigo\":\"COD00002\", \"cargaHoraria\": 75, \"periodo\": 2}";

        String url = "/curso/" + cursoId + "/disciplina";

        // Faz o POST da primeira disciplina
        ResultActions result = mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(disciplinaContentA))
        .andExpect(status().isCreated());

        String responseString = result.andReturn().getResponse().getContentAsString();

        disciplinaIdA = Long.valueOf(responseString);

        // Faz o POST da segunda disciplina
        result = mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(disciplinaContentB))
        .andExpect(status().isCreated());

        responseString = result.andReturn().getResponse().getContentAsString();

        disciplinaIdB = Long.valueOf(responseString);
    }

    @Test
    @Order(3)
    public void testPutDisciplina() throws Exception {
        String disciplinaContent = "{\"nome\":\"Disciplina Modificada\", \"codigo\":\"COD00003\", \"cargaHoraria\": 90, \"periodo\": 5}";

        String url = "/curso/" + cursoId + "/disciplina/" + disciplinaIdA;

        mockMvc.perform(put(url).contentType(MediaType.APPLICATION_JSON).content(disciplinaContent))
        .andExpect(status().isOk());

        mockMvc.perform(get(url).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.nome").value("Disciplina Modificada"))
        .andExpect(jsonPath("$.codigo").value("COD00003"))
        .andExpect(jsonPath("$.cargaHoraria").value(90))
        .andExpect(jsonPath("$.periodo").value(5));
    }

    @Test
    @Order(4)
    public void testPostDependencia() throws Exception {
        String dependenciaContent = "{\"disciplinaA\":\"COD00002\", \"disciplinaB\":\"COD00003\"}";

        String url = "/curso/" + cursoId + "/preRequisito";

        mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(dependenciaContent))
        .andExpect(status().isCreated());
    }

    @Test
    @Order(5)
    public void testPreRequisitoExistence() throws Exception {
        String url1 = "/curso/" + cursoId + "/path/" + disciplinaIdA;
        String url2 = "/curso/" + cursoId + "/path/" + disciplinaIdB;

        mockMvc.perform(get(url1).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].a.codigo").value("COD00002"))
        .andExpect(jsonPath("$[0].b.codigo").value("COD00003"));

        mockMvc.perform(get(url2).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].a.codigo").value("COD00002"))
        .andExpect(jsonPath("$[0].b.codigo").value("COD00003"));
    }

    @Test
    @Order(6)
    public void testDeleteDisciplina() throws Exception {
        String url = "/curso/" + cursoId + "/disciplina/" + disciplinaIdA;

        mockMvc.perform(delete(url).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

        mockMvc.perform(get(url).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
    }

    @Test
    @Order(7)
    public void testDeleteCurso() throws Exception {
        String url = "/curso/" + cursoId;

        mockMvc.perform(delete(url).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

        mockMvc.perform(get(url).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
    }
}
