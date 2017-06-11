package tacs.threadsconcurrency.service;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import javax.inject.Singleton;

/**
 * Created by srosenbolt on 11/06/17.
 */
public class AppBinder extends AbstractBinder {
    private boolean serialize;

    public AppBinder(boolean serialize) {

        this.serialize = serialize;
    }

    @Override
    protected void configure() {
        bind(new TransferenciaService(serialize)).to(TransferenciaService.class);
        bind(CuentaService.class).to(CuentaService.class).in(Singleton.class);
    }
}
