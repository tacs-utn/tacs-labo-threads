package tacs.threadsconcurrency.domain;

import java.math.BigDecimal;

public class Cuenta {
    private Integer id;
    private BigDecimal saldo;

    public Cuenta(Integer id, BigDecimal saldo) {
        this.id = id;
        this.saldo = saldo;
    }

    public Integer getId() {
        return id;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void incrementarSaldo(BigDecimal incremento){
        this.saldo = this.saldo.add(incremento);
    }

    public void decrementarSaldo(BigDecimal decremento){
        this.saldo = this.saldo.subtract(decremento);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cuenta cuenta = (Cuenta) o;

        return id.equals(cuenta.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Cuenta{" +
                "id=" + id +
                ", saldo=" + saldo +
                '}';
    }
}
