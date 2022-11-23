package ru.itmo.highload.storroom.orders.services;

import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itmo.highload.storroom.orders.clients.LockClient;
import ru.itmo.highload.storroom.orders.dtos.LockDTO;
import ru.itmo.highload.storroom.orders.dtos.ManufacturerDTO;
import ru.itmo.highload.storroom.orders.exceptions.ResourceNotFoundException;
import ru.itmo.highload.storroom.orders.exceptions.UnavailableException;

import java.util.UUID;

@Service
public class LockService {
    @Autowired
    private LockClient lockClient;

    public LockDTO getLock(UUID id) {
        LockDTO lock;
        try {

            lock = lockClient.getLock(id);
        } catch (FeignException e) {
            if(e.status() == 409) {
                throw new ResourceNotFoundException("lock", id.toString());
            } else {
                throw new UnavailableException();
            }
        }
        return lock;
    }

    public LockDTO getLockAlways(UUID id) {
        LockDTO lock;
        try {
            lock = lockClient.getLock(id);
        } catch (FeignException e) {
            lock = new LockDTO();
            lock.setId(id);
            lock.manufacturer = new ManufacturerDTO();
        }
        return lock;
    }
}
