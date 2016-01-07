package ch.wisv.areafiftylan.service.repository;

import ch.wisv.areafiftylan.model.ExpiredOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;


@Repository
public interface ExpiredOrderRepository extends JpaRepository<ExpiredOrder, Long> {
    Collection<ExpiredOrder> findAllBycreatedBy(String username);

}
