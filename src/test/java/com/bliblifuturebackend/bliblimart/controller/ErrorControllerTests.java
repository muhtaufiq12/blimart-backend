package com.bliblifuturebackend.bliblimart.controller;

import com.blibli.oss.command.CommandExecutor;
import com.blibli.oss.command.exception.CommandValidationException;
import com.blibli.oss.common.error.Errors;
import com.blibli.oss.common.response.Response;
import org.assertj.core.util.Sets;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@WebFluxTest(controllers = ErrorController.class)
public class ErrorControllerTests {

    @MockBean
    private CommandExecutor commandExecutor;

    @Autowired
    private ErrorController errorController;

    @Before
    public void setup() {
    }

    @Test
    public void test_commandValidationException(){
        CommandValidationException exception = new CommandValidationException(Sets.newHashSet());

        Response<Object> expected = new Response<>();
        expected.setCode(HttpStatus.BAD_REQUEST.value());
        expected.setStatus(HttpStatus.BAD_REQUEST.name());
        expected.setErrors(Errors.from(exception.getConstraintViolations()));

        Response<Object> response = errorController.commandValidationException(exception);

        Assert.assertEquals(expected.getCode(), response.getCode());
        Assert.assertEquals(expected.getStatus(), response.getStatus());
        Assert.assertEquals(expected.getErrors(), response.getErrors());
    }

    @Test
    public void test_unauthorizedException(){
        SecurityException exception = new SecurityException("Unauthorized");

        Response<Object> expected = new Response<>();
        expected.setCode(HttpStatus.UNAUTHORIZED.value());
        expected.setStatus(HttpStatus.UNAUTHORIZED.name());

        Map<String, List<String>> errors = new HashMap<>();
        List<String> messages = new ArrayList<>();
        messages.add(exception.getMessage());

        errors.put("message", messages);
        expected.setErrors(errors);

        Response<Object> response = errorController.unauthorizedException(exception);

        Assert.assertEquals(expected.getCode(), response.getCode());
        Assert.assertEquals(expected.getStatus(), response.getStatus());
        Assert.assertEquals(expected.getErrors(), response.getErrors());
    }

    @Test
    public void test_notAcceptableRequest(){
        IllegalArgumentException exception = new IllegalArgumentException("Not acceptable");

        Response<Object> expected = new Response<>();
        expected.setCode(HttpStatus.NOT_ACCEPTABLE.value());
        expected.setStatus(HttpStatus.NOT_ACCEPTABLE.name());

        Map<String, List<String>> errors = new HashMap<>();
        List<String> messages = new ArrayList<>();
        messages.add(exception.getMessage());

        errors.put("message", messages);
        expected.setErrors(errors);

        Response<Object> response = errorController.notAcceptableRequest(exception);

        Assert.assertEquals(expected.getCode(), response.getCode());
        Assert.assertEquals(expected.getStatus(), response.getStatus());
        Assert.assertEquals(expected.getErrors(), response.getErrors());
    }

    @Test
    public void test_nullPointerException(){
        NullPointerException exception = new NullPointerException("Not found!");

        Response<Object> expected = new Response<>();
        expected.setCode(HttpStatus.NOT_FOUND.value());
        expected.setStatus(HttpStatus.NOT_FOUND.name());

        Map<String, List<String>> errors = new HashMap<>();
        List<String> messages = new ArrayList<>();
        messages.add(exception.getMessage());

        errors.put("message", messages);
        expected.setErrors(errors);

        Response<Object> response = errorController.nullPointerException(exception);

        Assert.assertEquals(expected.getCode(), response.getCode());
        Assert.assertEquals(expected.getStatus(), response.getStatus());
        Assert.assertEquals(expected.getErrors(), response.getErrors());
    }
}
