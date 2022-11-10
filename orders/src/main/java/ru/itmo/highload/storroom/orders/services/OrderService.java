package ru.itmo.highload.storroom.orders.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itmo.highload.storroom.orders.dtos.*;
import ru.itmo.highload.storroom.orders.exceptions.ResourceNotFoundException;
import ru.itmo.highload.storroom.orders.models.OrderEntity;
import ru.itmo.highload.storroom.orders.models.OrderStatus;
import ru.itmo.highload.storroom.orders.models.UnitEntity;
import ru.itmo.highload.storroom.orders.models.UnitStatus;
import ru.itmo.highload.storroom.orders.repositories.OrderRepository;
import ru.itmo.highload.storroom.orders.utils.Mapper;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    OrderRepository repo;
    @Autowired UnitService unitService;

    @Autowired private LockService lockService;
    @Autowired private LocationService locationService;
    @Autowired private UserService userService;

    public Page<OrderDTO> getAll(Pageable pageable) {
        Page<OrderEntity> res = repo.findAll(pageable);
        return res.map(Mapper::toOrderDTO);
    }

    public Page<OrderDTO> getAllByUserId(UUID userId, Pageable pageable) {
        Page<OrderEntity> res = repo.findAllByUserId(pageable, userId);
        return res.map(Mapper::toOrderDTO);
    }

    @Transactional
    public OrderFullDTO create(OrderDTO dto) {
        UnitEntity unitEntity = unitService.getById(dto.getUnitId());
        if(unitEntity.getStatus() != UnitStatus.available) {
            throw new IllegalStateException("unit is not available");
        }
        UserDTO user = userService.getUser(dto.getUserId());
        LockDTO lock = lockService.getLock(unitEntity.getLockId());
        LocationDTO location = locationService.getLocationAlways(unitEntity.getLocationId());

        OrderEntity orderEntity = Mapper.toOrderEntity(dto);
        orderEntity.setUnit(unitEntity);
        orderEntity = repo.save(orderEntity);
        unitService.updateStatus(unitEntity.getId(), UnitStatus.occupied);

        return Mapper.toOrderFullDTO(orderEntity, user,  lock, location);
    }

    public OrderFullDTO updateOrderInfo(UUID id, OrderDTO dto) {
        OrderEntity order = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("order", id.toString()));
        if(order.getStatus() != dto.getStatus()) {
            throw new IllegalArgumentException("status updates via info updates not supported");
        }
        UnitEntity unitEntity = unitService.getById(dto.getUnitId());
        UserDTO user = userService.getUser(dto.getUserId());
        LocationDTO location = locationService.getLocationAlways(unitEntity.getLocationId());

        order.setUserId(dto.getUserId());
        order.setUnit(unitEntity);
        order.setStartTime(dto.getStartTime());
        order.setEndTime(dto.getEndTime());
        order.setFinishedTime(dto.getFinishedTime());
        order = repo.save(order);

        LockDTO lock = lockService.getLockAlways(order.getUnit().getLockId());
        return Mapper.toOrderFullDTO(order, user,  lock, location);
    }

    @Transactional
    public OrderFullDTO finishOrder(UUID id) {
        OrderEntity order = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("order", id.toString()));

        if(order.getStatus() == OrderStatus.finished) {
            throw new IllegalStateException("order is already finished");
        }
        UserDTO user = userService.getUserAlways(order.getUserId());
        LockDTO lock = lockService.getLockAlways(order.getUnit().getLockId());
        LocationDTO location = locationService.getLocationAlways(order.getUnit().getLocationId());

        order.setFinishedTime(LocalDateTime.now());
        order.setStatus(OrderStatus.finished);
        order = repo.save(order);

        unitService.updateStatus(order.getUnit().getId(), UnitStatus.pending);

        return Mapper.toOrderFullDTO(order, user,  lock, location);
    }
}
