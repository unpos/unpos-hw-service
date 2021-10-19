package ru.unpos.hw.service;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "app")
public interface Cfg {

    String binPath();
}
