package com.security.web.rest.vm;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AccountVM(String fullName, String avatarUrl, @JsonProperty("tokens") TokenVM tokenVM) {}
