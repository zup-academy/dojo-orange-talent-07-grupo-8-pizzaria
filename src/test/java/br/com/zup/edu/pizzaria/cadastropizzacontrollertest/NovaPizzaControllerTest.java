package br.com.zup.edu.pizzaria.cadastropizzacontrollertest;

import br.com.zup.edu.pizzaria.ingredientes.Ingrediente;
import br.com.zup.edu.pizzaria.ingredientes.IngredienteRepository;
import br.com.zup.edu.pizzaria.pizzas.cadastropizza.NovaPizzaRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class NovaPizzaControllerTest {
    @Autowired
    private MockMvc mvc;

    @PersistenceContext
    private EntityManager entityManager;


    @Test
    public void deveCadastrarPizza() throws Exception {

        Ingrediente ingrediente = new Ingrediente("Frango",100,new BigDecimal("10.0"));
        entityManager.persist(ingrediente);

        List<Long> listaIngred = new ArrayList<Long>();
        listaIngred.add(ingrediente.getId());

        NovaPizzaRequest body = new NovaPizzaRequest("Frango com calabresa", listaIngred);
        MockHttpServletRequestBuilder request = post("/api/pizzas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(body));

        mvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(redirectedUrlPattern("/api/pizzas/*"));
    }
}
