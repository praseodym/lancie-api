package ch.wisv.areafiftylan.model;

import ch.wisv.areafiftylan.model.util.OrderStatus;
import ch.wisv.areafiftylan.model.view.View;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity

public class Order {

    @Id
    @GeneratedValue
    @JsonView(View.OrderOverview.class)
    Long id;

    @OneToMany(cascade = CascadeType.MERGE, targetEntity = Ticket.class, fetch = FetchType.EAGER)
    @JsonView(View.OrderOverview.class)
    Set<Ticket> tickets;

    @JsonView(View.OrderOverview.class)
    OrderStatus status;

    @JsonView(View.OrderOverview.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime creationDateTime;

    @JsonView(View.OrderOverview.class)
    float amount;

    /**
     * This String can be used to store an external reference. Payment providers often have their own id.
     */
    @JsonView(View.OrderOverview.class)
    String reference;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JsonView(View.OrderOverview.class)
    User user;

    public Order(User user) {
        this.user = user;
        status = OrderStatus.CREATING;
        creationDateTime = LocalDateTime.now();
        this.tickets = new HashSet<>();
    }

    public Order() {
        //JPA only
    }

    public Long getId() {
        return id;
    }

    public Set<Ticket> getTickets() {
        return tickets;
    }

    public boolean addTicket(Ticket ticket) {
        amount += ticket.getPrice();
        return tickets.add(ticket);
    }

    public void clearTickets() {
        tickets.clear();
    }

    public OrderStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreationDateTime() {
        return creationDateTime;
    }

    public String getReference() {
        return reference;
    }

    public User getUser() {
        return user;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public float getAmount() {
        float price = 0F;
        for (Ticket ticket : this.tickets) {
            price += ticket.getPrice();
        }
        return price;
    }
}
