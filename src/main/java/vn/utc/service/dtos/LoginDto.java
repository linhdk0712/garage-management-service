package vn.utc.service.dtos;

import java.io.Serializable;

public record LoginDto(String username, String password) implements Serializable {}
