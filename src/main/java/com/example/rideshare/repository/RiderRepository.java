package com.example.rideshare.repository;

import com.example.rideshare.CoreEntity.Rider;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class RiderRepository {

    private final Map<UUID, Rider> riders = new ConcurrentHashMap<>();


    public void save(Rider rider) {
        riders.put(rider.getUserId(), rider);
    }


    public Optional<Rider> findById(UUID riderId) {
        return Optional.ofNullable(riders.get(riderId));
    }
}
