package com.marat.controlworkbymarat.service;

import com.marat.controlworkbymarat.dto.Message;
import com.marat.controlworkbymarat.dto.OrderDTO;
import com.marat.controlworkbymarat.dto.OrderResponse;
import com.marat.controlworkbymarat.entity.Order;
import com.marat.controlworkbymarat.entity.User;
import com.marat.controlworkbymarat.entity.enums.ERole;
import com.marat.controlworkbymarat.entity.enums.EStatus;
import com.marat.controlworkbymarat.facade.OrderFacade;
import com.marat.controlworkbymarat.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderFacade orderFacade;
    private final PrincipalService principalService;

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderFacade orderFacade, PrincipalService principalService) {
        this.orderRepository = orderRepository;
        this.orderFacade = orderFacade;
        this.principalService = principalService;
    }

    public OrderDTO createOrder(OrderDTO orderDTO, Principal principal){
        Order order=orderFacade.orderDTOToOrder(orderDTO);
        order.setUser(principalService.getUserByPrincipal(principal));
        order.setOrderDate(LocalDate.now());
        order.setDeliveryDate(LocalDate.now().plusDays(5));
        order.setEStatus(EStatus.NEW);
        order.setCode(100 + (int) (Math.random()*899));
        Order savedOrder=orderRepository.save(order);
        return orderFacade.orderToOrderDTO(savedOrder);
    }

    public OrderResponse getOrderById(Long id){
        return orderFacade.orderToOrderResponse(orderRepository.findById(id).orElse(null));
    }

    public List<OrderResponse> getAllOrders(Principal principal){
        User user=principalService.getUserByPrincipal(principal);
        if(user.getRoles().contains(ERole.ROLE_ADMIN)){
            return orderFacade.orderListToOrderResponseList(orderRepository.findAll());
        }
            return orderFacade.orderListToOrderResponseList(orderRepository.findAllByUser(user));


    }


}
