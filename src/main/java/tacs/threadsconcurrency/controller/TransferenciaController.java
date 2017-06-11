package tacs.threadsconcurrency.controller;

import tacs.threadsconcurrency.domain.Cuenta;
import tacs.threadsconcurrency.service.CuentaService;
import tacs.threadsconcurrency.service.TransferenciaService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;


@Path("transferencia")
public class TransferenciaController {

    @Inject
    private TransferenciaService transferenciaService;

    @Inject
    private CuentaService cuentaService;

    @GET
    @Path("health-check")
    @Produces(MediaType.TEXT_PLAIN)
    public String helloWorld() {
        return "Hello, world!";
    }

    public TransferenciaService getTransferenciaService() {
        return transferenciaService;
    }

    public void setTransferenciaService(TransferenciaService transferenciaService) {
        this.transferenciaService = transferenciaService;
    }

    @GET
    @Path("/{cuenta1}/{cuenta2}")
    @Produces(MediaType.TEXT_PLAIN)
    public String transferir(@PathParam("cuenta1") int cuenta1, @PathParam("cuenta2") int cuenta2) {
        return transferir(cuenta1, cuenta2, "100");
    }

    @GET
    @Path("/{cuenta1}/{cuenta2}/{monto}")
    @Produces(MediaType.TEXT_PLAIN)
    public String transferir(@PathParam("cuenta1") int cuenta1, @PathParam("cuenta2") int cuenta2,@PathParam("monto") String monto) {
        return transferir(cuenta1, cuenta2, BigDecimal.valueOf(Integer.valueOf(monto))).toString();
    }

    public BigDecimal transferir(int cuenta1, int cuenta2, BigDecimal monto) {
        Cuenta c1 = cuentaService.getCuenta(cuenta1);
        Cuenta c2 = cuentaService.getCuenta(cuenta2);

        transferenciaService.transferir(c1, c2, monto);
        return c1.getSaldo();
    }

}
