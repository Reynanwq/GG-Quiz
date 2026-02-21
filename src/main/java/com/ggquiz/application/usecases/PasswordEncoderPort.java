package com.ggquiz.application.usecases;

public interface PasswordEncoderPort {
    String encode(String rawPassword);
    boolean matches(String rawPassword, String encoded);
}