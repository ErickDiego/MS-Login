package com.example.demo

import com.example.demo.controller.UsuarioController
import com.example.demo.entity.PhoneEntity
import com.example.demo.entity.UsuarioEntity
import com.example.demo.repository.UsuarioRepository
import org.springframework.http.ResponseEntity
import spock.lang.Specification
import spock.lang.Subject
import spock.mock.DetachedMockFactory

class TestUsuarioController extends Specification{
    def "helloWorld should return 'Hola Mundo desde el proyecto Login-MS'"() {
        given:
        def usuarioController = new UsuarioController()

        when:
        def result = usuarioController.helloWorld()

        then:
        result == "Hola Mundo desde el proyecto Login-MS"
    }

    /***
     * Test para la validacion de contraseña con formato y condidiciones correctas**/
    def "Test ValidacionPasswordOk"(){
        given:
        def usuarioController = new UsuarioController();

        when:
        def passwordTest= "a2asfGfdfdf4"
        def result = usuarioController.validacionPassword(passwordTest)

        then:
        result == true;
    }

    /**Test para validar que las contraseñas incorrectas**/
    def "Test ValidacionPasswordError"(){
        given:
        def usuarioController = new UsuarioController();

        when:
        def passwordTest= "a2asfGfdfdf4111"
        def result = usuarioController.validacionPassword(passwordTest)

        then:
        result == false;
    }

    def "Crear usuario de manera correcta"() {
        given:
        def usuarioController = new UsuarioController()
        def usuarioEntity = new UsuarioEntity(name: "Jorge", email: "jorge@example.com", password: "Abcd1234")

        // Mock del repositorio de usuarios
        usuarioController.usuarioRepository = Mock(UsuarioRepository)

        when:
        def responseEntity = usuarioController.createUser(usuarioEntity)

        then:
        responseEntity.statusCode == HttpStatus.OK
    }

    def "Encriptacion de contrasenia"(){
        given:
        def inputPassword = "a2asfGfdfdf4"
        def passwordEncript = "l6WZ6vd2p7y+SneKOAtZFfL6EewOX7Zo9MlQtGoDQB8="

        UsuarioController usuarioController = new UsuarioController();
        when:
        def outputPassword = usuarioController.encriptacionPassword(inputPassword)

        then:
        outputPassword == passwordEncript

    }
}

