package ru.unpos.hw.service;

import io.smallrye.config.ConfigMapping;
import org.eclipse.microprofile.config.inject.ConfigProperties;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.config.spi.ConfigSource;

import javax.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

//@ConfigMapping(prefix = "app")
//@ConfigProperties(prefix = "app")
@ApplicationScoped
public class InMemoryConfigSource {

    @ConfigProperty(name = "binPath")
    String binPath;

    public String getBinPath() {
        return binPath;
    }
}
