package ru.itmo.highload.storoom.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itmo.highload.storoom.consts.OrderStatus;
import ru.itmo.highload.storoom.consts.UnitStatus;
import ru.itmo.highload.storoom.exceptions.ResourceNotFoundException;
import ru.itmo.highload.storoom.models.DTOs.OrderDTO;
import ru.itmo.highload.storoom.models.OrderEntity;
import ru.itmo.highload.storoom.models.UnitEntity;
import ru.itmo.highload.storoom.repositories.OrderRepository;
import ru.itmo.highload.storoom.utils.Mapper;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired OrderRepository repo;
    @Autowired UnitService unitService;
    @Autowired UserService userService;

    public Page<OrderDTO> getAll(Pageable pageable) {
        Page<OrderEntity> res = repo.findAll(pageable);
        return res.map(Mapper::toOrderDTO);
    }

    public Page<OrderDTO> getAllByUsername(String username, Pageable pageable) {
        Page<OrderEntity> res = repo.findAllByUser(pageable, username);
        return res.map(Mapper::toOrderDTO);
    }

    @Transactional
    public OrderDTO create(OrderDTO dto) {
        UnitEntity unitEntity = unitService.getById(dto.getUnitId());
        if(unitEntity.getStatus() != UnitStatus.available) {
            throw new IllegalStateException("unit is not available");
        }

        OrderEntity orderEntity = Mapper.toOrderEntity(dto);
        orderEntity.setUser(userService.getRef(dto.getUserId()));
        orderEntity.setUnit(unitEntity);
        orderEntity = repo.save(orderEntity);
        unitService.updateStatus(unitEntity.getId(), UnitStatus.occupied);

        return Mapper.toOrderDTO(orderEntity);
    }

    public OrderDTO updateOrderInfo(UUID id, OrderDTO dto) {
        OrderEntity order = repo.findById(id).orElseThrow(ResourceNotFoundException::new);

        if(order.getStatus() != dto.getStatus()) {
            throw new IllegalArgumentException("status updates via info updates not supported");
        }

        order.setUser(userService.getRef(dto.getUserId()));
        order.setUnit(unitService.getRef(dto.getUnitId()));
        order.setStartTime(dto.getStartTime());
        order.setEndTime(dto.getEndTime());
        order.setFinishedTime(dto.getFinishedTime());
        order = repo.save(order);

        return Mapper.toOrderDTO(order);
    }

    @Transactional
    public OrderDTO finishOrder(UUID id) {
        OrderEntity order = repo.findById(id).orElseThrow(ResourceNotFoundException::new);

        if(order.getStatus() == OrderStatus.finished) {
            throw new IllegalStateException("order is already finished");
        }

        order.setFinishedTime(LocalDateTime.now());
        order.setStatus(OrderStatus.finished);
        order = repo.save(order);

        unitService.updateStatus(order.getUnit().getId(), UnitStatus.pending);

        return Mapper.toOrderDTO(order);
    }
}
