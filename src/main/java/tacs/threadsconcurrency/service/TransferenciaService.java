package tacs.threadsconcurrency.service;

import tacs.threadsconcurrency.domain.Cuenta;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.inject.Named;
import java.math.BigDecimal;

@Resource
public class TransferenciaService {
    private boolean serialize = false;
    private static final int DELAY = 5000;

    public TransferenciaService(boolean serialize) {
        this.serialize = serialize;
    }

    public void transferir(Cuenta c1, Cuenta c2, BigDecimal monto) {
        if (monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto debe ser positivo");
        }
        if (c1.equals(c2)) {
            throw new IllegalArgumentException("No se puede transferir una cuenta a si misma");
        }
        if (serialize) {
            transferirSerializado(c1, c2, monto);
        } else {
            transferirPorCuenta(c1, c2, monto);
        }
    }

    private synchronized void transferirSerializado(Cuenta c1, Cuenta c2, BigDecimal monto) {
        if (c1.getSaldo().compareTo(monto) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente");
        }

        try {
            Thread.sleep(DELAY);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        c1.decrementarSaldo(monto);
        c2.incrementarSaldo(monto);
    }

    private void transferirPorCuenta(Cuenta c1, Cuenta c2, BigDecimal monto) {
        try {

        synchronized (c1) {
            Thread.sleep(DELAY /2);
            synchronized (c2) {
                if (c1.getSaldo().compareTo(monto) < 0) {
                    throw new IllegalArgumentException("Saldo insuficiente");
                }

                    Thread.sleep(DELAY /2);

                c1.decrementarSaldo(monto);
                c2.incrementarSaldo(monto);
            }
        }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isSerialize() {
        return serialize;
    }

    public void setSerialize(boolean serialize) {
        this.serialize = serialize;
    }
}
