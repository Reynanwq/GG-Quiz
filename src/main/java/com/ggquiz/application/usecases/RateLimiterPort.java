package com.ggquiz.application.usecases;

public interface RateLimiterPort {
    void checkLimit(String userId);
}