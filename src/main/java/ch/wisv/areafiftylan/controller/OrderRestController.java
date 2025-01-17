package ch.wisv.areafiftylan.controller;

import ch.wisv.areafiftylan.dto.TicketDTO;
import ch.wisv.areafiftylan.exception.ImmutableOrderException;
import ch.wisv.areafiftylan.exception.TicketUnavailableException;
import ch.wisv.areafiftylan.model.Order;
import ch.wisv.areafiftylan.model.User;
import ch.wisv.areafiftylan.model.view.View;
import ch.wisv.areafiftylan.service.OrderService;
import ch.wisv.areafiftylan.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;

import static ch.wisv.areafiftylan.util.ResponseEntityBuilder.createResponseEntity;

@RestController
public class OrderRestController {

    OrderService orderService;

    UserService userService;

    @Autowired
    public OrderRestController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    @JsonView(View.OrderOverview.class)
    public Collection<Order> getAllOrders() {
        return orderService.getAllOrders();

    }

    /**
     * When a User does a POST request to /orders, a new Order is created. The requestbody is a TicketDTO, so an order
     * always contains at least one ticket. Optional next tickets should be added to the order by POSTing to the
     * location provided.
     *
     * @param auth      The User that is currently logged in
     * @param ticketDTO Object containing information about the Ticket that is being ordered.
     *
     * @return A message informing about the result of the request
     */
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/orders", method = RequestMethod.POST)
    @JsonView(View.OrderOverview.class)
    public ResponseEntity<?> createOrder(Authentication auth, @RequestBody @Validated TicketDTO ticketDTO) {
        HttpHeaders headers = new HttpHeaders();
        User user = (User) auth.getPrincipal();

        Order order = orderService.create(user.getId(), ticketDTO);

        headers.setLocation(
                ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(order.getId()).toUri());

        return createResponseEntity(HttpStatus.CREATED, headers,
                "Ticket available and order successfully created at " + headers.getLocation(), order);
    }

    /**
     * This method handles GET requests on a specific Order.
     *
     * @param orderId Id of the Order that is requested
     *
     * @return The requested Order.
     */
    @PreAuthorize("@currentUserServiceImpl.canAccessOrder(principal, #orderId)")
    @RequestMapping(value = "/orders/{orderId}", method = RequestMethod.GET)
    @JsonView(View.OrderOverview.class)
    public Order getOrderById(@PathVariable Long orderId) {
        return orderService.getOrderById(orderId);
    }

    /**
     * Adds a ticket to an existing Order. It's possible to buy serveral ticket at once. After the order has been
     * created, tickets can be added by POSTing more TicketDTOs to this location.
     *
     * @param orderId   Id of the Order
     * @param ticketDTO TicketDTO of the Ticket to be added to the Order
     *
     * @return Message about the result of the request
     */
    @PreAuthorize("@currentUserServiceImpl.canAccessOrder(principal, #orderId)")
    @RequestMapping(value = "/orders/{orderId}", method = RequestMethod.POST)
    @JsonView(View.OrderOverview.class)
    public ResponseEntity<?> addToOrder(@PathVariable Long orderId, @RequestBody @Validated TicketDTO ticketDTO) {
        Order modifiedOrder = orderService.addTicketToOrder(orderId, ticketDTO);
        return createResponseEntity(HttpStatus.OK, "Ticket successfully added to your order", modifiedOrder);
    }

    /**
     * This method requests payment of the order, locks the order and needs to return information on how to proceed.
     * Depending on PaymentService.
     *
     * @param orderId The order to be paid
     *
     * @return Instructions on how to proceed.
     */
    @PreAuthorize("@currentUserServiceImpl.canAccessOrder(principal, #orderId)")
    @RequestMapping(value = "/orders/{orderId}/checkout", method = RequestMethod.GET)
    public ResponseEntity<?> payOrder(@PathVariable Long orderId) throws URISyntaxException {
        //TODO: Implement Paymentprovider calls here.
        String paymentUrl = orderService.requestPayment(orderId);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(new URI(paymentUrl));

        return createResponseEntity(HttpStatus.OK, headers, "Please go to" + paymentUrl + "to finish your payment");
    }


    /**
     * This method handles the webhook from the payment provider. It requests the status of the order with the given
     * reference
     *
     * @param orderReference Id of the order at the paymentprovider, stored in the reference field
     *
     * @return Statusmessage
     */
    @RequestMapping(value = "/orders/status", method = RequestMethod.POST)
    public ResponseEntity<?> updateOrderStatus(@RequestParam(name = "id") String orderReference) {
        //TODO: Figure out how Mollie sends this request
        orderService.updateOrderStatus(orderReference);
        return createResponseEntity(HttpStatus.OK, "Status is being updated");
    }


    @ExceptionHandler(TicketUnavailableException.class)
    public ResponseEntity<?> handleTicketUnavailableException(TicketUnavailableException e) {
        return createResponseEntity(HttpStatus.GONE, e.getMessage());
    }

    @ExceptionHandler(ImmutableOrderException.class)
    public ResponseEntity<?> handleWrongOrderStatusException(ImmutableOrderException e) {
        return createResponseEntity(HttpStatus.CONFLICT, e.getMessage());
    }

    //TODO: Move this to central ControllerAdvice class
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException ex) {
        return createResponseEntity(HttpStatus.FORBIDDEN, "Access denied");
    }
}
