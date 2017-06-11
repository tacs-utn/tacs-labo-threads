package tacs.threadsconcurrency.service;

import tacs.threadsconcurrency.domain.Cuenta;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Resource
public class CuentaService {
    public static final int CANTIDAD_CUENTAS = 100;
    private static final Map<Integer, Cuenta> CUENTAS = new HashMap<>(CANTIDAD_CUENTAS);

    public CuentaService() {
        init();
    }

    private void init() {
        Random random = new Random(System.currentTimeMillis());
        for (int i = 1; i < CANTIDAD_CUENTAS + 1; i++) {
            int randomInt = random.nextInt(9000);
            BigDecimal saldo = BigDecimal.valueOf(randomInt + 1000L);
            Cuenta cuenta = new Cuenta(i, saldo);
            CUENTAS.put(i, cuenta);
        }
    }

    public Cuenta getCuenta(Integer i) {

        Cuenta cuenta = CUENTAS.get(i);
        if (cuenta == null) {
            throw new IllegalArgumentException(String.format("La cuenta %s no existe.", i));
        }
        return cuenta;
    }

}
