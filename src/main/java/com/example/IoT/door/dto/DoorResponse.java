package com.example.IoT.door.dto;

import com.example.IoT.door.domain.Door;

public record DoorResponse(
        Long id,
        String name,
        String location,
        boolean active
) {
    public static DoorResponse from(Door d) {
        return new DoorResponse(d.getId(), d.getName(), d.getLocation(), d.isActive());
    }
}
