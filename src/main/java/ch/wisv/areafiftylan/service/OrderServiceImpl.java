package ch.wisv.areafiftylan.service;

import ch.wisv.areafiftylan.dto.TicketDTO;
import ch.wisv.areafiftylan.exception.TicketUnavailableException;
import ch.wisv.areafiftylan.exception.TokenNotFoundException;
import ch.wisv.areafiftylan.model.Order;
import ch.wisv.areafiftylan.model.Ticket;
import ch.wisv.areafiftylan.model.User;
import ch.wisv.areafiftylan.model.util.TicketType;
import ch.wisv.areafiftylan.service.repository.OrderRepository;
import ch.wisv.areafiftylan.service.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    OrderRepository orderRepository;
    TicketRepository ticketRepository;
    UserService userService;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, UserService userService,
                            TicketRepository ticketRepository) {
        this.orderRepository = orderRepository;
        this.ticketRepository = ticketRepository;
        this.userService = userService;
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findOne(id);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Collection<Order> findOrdersByUsername(String username) {
        return orderRepository.findAllByUserUsername(username);
    }

    @Override
    public Order create(Long userId, TicketDTO ticketDTO) {
        User user = userService.getUserById(userId);

        // Request a ticket to see if one is available. If a ticket is sold out, the method ends here due to the
        // exception thrown. Else, we'll get a new ticket to add to the order.
        Ticket ticket = this.requestTicketOfType(ticketDTO.getType(), user, ticketDTO.hasPickupService());

        Order order = new Order(user);

        order.addTicket(ticket);

        return orderRepository.save(order);
    }

    @Override
    public void addTicketToOrder(Long orderId, TicketDTO ticketDTO) {
        Order order = orderRepository.getOne(orderId);

        User user = order.getUser();

        // Request a ticket to see if one is available. If a ticket is sold out, the method ends here due to the
        // exception thrown. Else, we'll get a new ticket to add to the order.
        Ticket ticket = this.requestTicketOfType(ticketDTO.getType(), user, ticketDTO.hasPickupService());

        order.addTicket(ticket);

        orderRepository.save(order);
    }

    @Override
    public Ticket requestTicketOfType(TicketType type, User owner, boolean pickupService) {
        if (ticketRepository.countByType(type) >= type.getLimit()) {
            throw new TicketUnavailableException(type);
        } else {
            Ticket ticket = new Ticket(owner, type, pickupService);
            return ticketRepository.save(ticket);
        }
    }

    @Override
    public void transferTicket(User user, String ticketKey) {
        Ticket ticket = ticketRepository.findByKey(ticketKey).orElseThrow(() -> new TokenNotFoundException(ticketKey));

        if (ticket.isLockedForTransfer()) {
            ticket.setPreviousOwner(ticket.getOwner());

            ticket.setOwner(user);

            ticket.setLockedForTransfer(true);

            ticketRepository.save(ticket);
        } else {
            //TODO: Deal with invalid transfer attempt
        }
    }

    @Override
    public void requestPayment(Long orderId) {
        //TODO: Create a payment through the paymentservice
    }

    @Override
    public void updateOrderStatus(Long orderId) {
        //TODO: request an update of an order through the paymentservice
    }
}
