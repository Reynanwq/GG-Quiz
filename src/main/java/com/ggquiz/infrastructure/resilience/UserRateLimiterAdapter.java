// infrastructure/resilience/UserRateLimiterAdapter.java
package com.ggquiz.infrastructure.resilience;

import com.ggquiz.application.usecases.RateLimiterPort;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import org.springframework.stereotype.Component;

@Component
public class UserRateLimiterAdapter implements RateLimiterPort {

    private final RateLimiterRegistry registry;

    public UserRateLimiterAdapter(RateLimiterRegistry registry) {
        this.registry = registry;
    }

    @Override
    public void checkLimit(String userId) {
        RateLimiter rateLimiter = registry.rateLimiter("user-" + userId, "perUser");

        boolean permitted = rateLimiter.acquirePermission(1);

        if (!permitted) {
            throw RequestNotPermitted.createRequestNotPermitted(rateLimiter);
        }
    }
}