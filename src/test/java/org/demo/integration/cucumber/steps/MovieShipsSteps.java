package org.demo.integration.cucumber.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.demo.controller.MovieSpaceShipsController;
import org.demo.dto.MovieSpaceShipsDto;
import org.demo.integration.cucumber.config.CucumberSpringConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.io.UnsupportedEncodingException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@ContextConfiguration(classes = {CucumberSpringConfiguration.class, MovieSpaceShipsController.class})
public class MovieShipsSteps {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MovieSpaceShipsController movieSpaceShipsController;

    private MovieSpaceShipsDto resultMovieSpaceShipsDto;

    @Given("existe una nave espacial con el ID {int}")
    public void existUserById(int userId) throws Exception {
        System.out.println("Ejecutado el GIVEN");

        var responseEntity = movieSpaceShipsController.findById(userId);
        var resultBody = responseEntity.getBody();

        assertThat(resultBody, is(notNullValue()));
    }

    @When("el usuario consulta los detalles de la na espacial con ID {int}")
    public void elUsuarioConsultaLosDetallesDelUsuarioConID(int userId) throws Exception {
        System.out.println("Ejecutado el WHEN");
        resultMovieSpaceShipsDto = movieSpaceShipsController.findById(userId).getBody();
    }

    @Then("se muestran los detalles de la nave espacial")
    public void seMuestranLosDetallesDelUsuario() throws UnsupportedEncodingException, JsonProcessingException {
        System.out.println("Ejecutado el THEN");
        assertThat(resultMovieSpaceShipsDto.getId(), is(notNullValue()));
        assertThat(resultMovieSpaceShipsDto.getName(), is("USS Enterprise"));
        assertThat(resultMovieSpaceShipsDto.getMovie(), is("Star Trek"));
    }
}