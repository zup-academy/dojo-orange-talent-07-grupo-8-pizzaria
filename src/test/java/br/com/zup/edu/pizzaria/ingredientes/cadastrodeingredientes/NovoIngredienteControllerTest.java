package br.com.zup.edu.pizzaria.ingredientes.cadastrodeingredientes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class NovoIngredienteControllerTest {


    @Autowired
    private MockMvc mvc;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void deveCadastrarNovoIngrediente() throws Exception {

        NovoIngredienteRequest body = new NovoIngredienteRequest("Queijo muçarela", new BigDecimal("2.0"), 200);
        MockHttpServletRequestBuilder request = post("/api/ingredientes")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(new ObjectMapper().writeValueAsString(body));

        mvc.perform(request)
           .andExpect(status().isCreated())
           .andExpect(header().exists("Location"))
                .andExpect(redirectedUrlPattern("/api/ingredientes/*"));

    }

    @Test
    void naoDeveCadastrarIngredienteComPrecoVazio() throws Exception{
        NovoIngredienteRequest body = new NovoIngredienteRequest("", new BigDecimal("2.0"), 200);
        MockHttpServletRequestBuilder request = post("/api/ingredientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(body));

        mvc.perform(request).andExpect(status().is4xxClientError());
    }
    @Test
    void naoDeveCadastrarIngredienteComNomeNulo() throws Exception{
        NovoIngredienteRequest body = new NovoIngredienteRequest(null, new BigDecimal("2.0"), 200);
        MockHttpServletRequestBuilder request = post("/api/ingredientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(body));

        mvc.perform(request).andExpect(status().is4xxClientError());
    }
    @Test
    void naoDeveCadastrarIngredienteComPrecoNegativo() throws Exception{
        NovoIngredienteRequest body = new NovoIngredienteRequest(null, new BigDecimal("-2.0"), 200);
        MockHttpServletRequestBuilder request = post("/api/ingredientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(body));

        mvc.perform(request).andExpect(status().is4xxClientError());
    }
    @Test
    void naoDeveCadastrarIngredienteComPrecoNulo() throws Exception{
        NovoIngredienteRequest body = new NovoIngredienteRequest("Queijo muçarela", null, 200);
        MockHttpServletRequestBuilder request = post("/api/ingredientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(body));

        mvc.perform(request).andExpect(status().is4xxClientError());
    }
    @Test
    void naoDeveCadastrarIngredienteComQuantidadeNegativa() throws Exception{
        NovoIngredienteRequest body = new NovoIngredienteRequest("Muçarela", new BigDecimal("2.0"), 200);
        MockHttpServletRequestBuilder request = post("/api/ingredientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(body));

        mvc.perform(request).andExpect(status().is4xxClientError());
    }
    @Test
    void naoDeveCadastrarIngredienteComNomeVazio() throws Exception{
        NovoIngredienteRequest body = new NovoIngredienteRequest("", new BigDecimal("2.0"), 200);
        MockHttpServletRequestBuilder request = post("/api/ingredientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(body));

        mvc.perform(request).andExpect(status().is4xxClientError());
    }
    @Test
    @Transactional
    void naoDeveCadastrarIngredienteComNomeJaCadastrado() throws Exception{
        NovoIngredienteRequest ingredienteCadastrado = new NovoIngredienteRequest("Muçarela", new BigDecimal("2.0"), 200);

        entityManager.persist(ingredienteCadastrado.paraIngrediente());
        NovoIngredienteRequest body = new NovoIngredienteRequest("Muçarela", new BigDecimal("2.0"), 200);
        MockHttpServletRequestBuilder request = post("/api/ingredientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(body));

        mvc.perform(request).andExpect(status().is4xxClientError());
    }
}