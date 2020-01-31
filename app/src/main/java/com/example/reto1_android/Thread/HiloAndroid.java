package com.example.reto1_android.Thread;

import businessLogic.LogicCliente;
import businessLogic.LogicFactory;
import clases.User;
import exceptions.DAOException;
import exceptions.EsperaCompletaException;
import exceptions.LogicException;
import exceptions.LoginIDException;
import exceptions.PasswordException;
import exceptions.ServerException;

/**
 * Hilo que se conecta al servidor.
 * @author Ricardo
 */
public class HiloAndroid extends Thread {
    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger("HiloAndroid");
    private Object resultado = "DD";
    private Integer opc;
    private User usuarioEnvier;
    private LogicCliente logic = new LogicFactory().getLogicCliente();

    /**
     * Constructor para inicializar el hilo.
     * @param opc opciones, 1- registrar, 2- inicio de sesión.
     * @param user usuario para registrar o el que devuelve al iniciar sesión.
     */
    public HiloAndroid(int opc, User user) {
        this.opc = opc;
        this.usuarioEnvier = user;
    }

    /**
     * Analiza la opción y llama a su método logic.
     */
    public void run() {
        switch (opc) {
            case 1:
                try {
                    resultado = "Empezando";
                    resultado = logic.registro(usuarioEnvier);
                } catch (LogicException e) {
                    LOGGER.severe("Error -> " + e.getMessage());
                    resultado = 1;
                } catch (EsperaCompletaException e) {
                    LOGGER.severe("Error -> " + e.getMessage());
                    resultado = 2;
                } catch (ServerException e) {
                    LOGGER.severe("Error -> " + e.getMessage());
                    resultado = 3;
                } catch (DAOException e) {
                    LOGGER.severe("Error -> " + e.getMessage());
                    resultado = 4;
                } catch (LoginIDException e) {
                    LOGGER.severe("Error -> " + e.getMessage());
                    resultado = 7;
                }
                break;
            case 2:
                try {
                    resultado = "Empezando";
                    resultado = logic.login(usuarioEnvier.getLogin(), usuarioEnvier.getPassword());
                } catch (LogicException e) {
                    LOGGER.severe("Error -> " + e.getMessage());
                    resultado = 1;
                } catch (EsperaCompletaException e) {
                    LOGGER.severe("Error -> " + e.getMessage());
                    resultado = 2;
                } catch (ServerException e) {
                    LOGGER.severe("Error -> " + e.getMessage());
                    resultado = 3;
                } catch (DAOException e) {
                    LOGGER.severe("Error -> " + e.getMessage());
                    resultado = 4;
                } catch (LoginIDException e) {
                    LOGGER.severe("Error -> " + e.getMessage());
                    resultado = 5;
                } catch (PasswordException e) {
                    LOGGER.severe("Error -> " + e.getMessage());
                    resultado = 6;
                }
                break;
        }
    }

    /**
     * Método para obtener la respuesta del servidor desde la activity.
     * @return respuesta del servidor.
     */
    public Object getRespuesta() {
        return resultado;
    }
}
