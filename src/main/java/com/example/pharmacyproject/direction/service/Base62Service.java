package com.example.pharmacyproject.direction.service;

import io.seruco.encoding.base62.Base62;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class Base62Service {

    private static final Base62 base62Instance = Base62.createInstance();

    public String encodeDirectionId(Long directionId) {
        return new String(base62Instance.encode(String.valueOf(directionId).getBytes()));
    }

    public Long decodeDirectionId(String encodeDirectionId) {
        String resultDirectionId = new String(base62Instance.decode(encodeDirectionId.getBytes()));
        return Long.parseLong(resultDirectionId);
    }
}
