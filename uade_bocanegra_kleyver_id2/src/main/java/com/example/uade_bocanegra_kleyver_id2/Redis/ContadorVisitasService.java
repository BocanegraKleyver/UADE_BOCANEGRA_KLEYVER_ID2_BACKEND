package com.example.uade_bocanegra_kleyver_id2.Redis;

import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;

@Service
public class ContadorVisitasService {

    private static final String REDIS_HOST = "localhost";
    private static final int REDIS_PORT = 6379;

    private final Jedis jedis;

    public ContadorVisitasService() {
        this.jedis = new Jedis(REDIS_HOST, REDIS_PORT);
    }

    public long incrementarVisita(String key) {
        return jedis.incr(key);
    }

    public long obtenerVisitas() {
        String visitas = jedis.get("contador:visitas");
        return visitas != null ? Long.parseLong(visitas) : 0;
    }
}
