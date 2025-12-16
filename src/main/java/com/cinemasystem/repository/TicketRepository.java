package com.cinemasystem.repository;

import com.cinemasystem.entity.Ticket;
import com.cinemasystem.entity.enums.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByUserUsernameOrderByPurchaseTimeDesc(String username);

    boolean existsByShowtimeIdAndSeatIdAndStatusIn(
            Long showtimeId,
            Long seatId,
            List<TicketStatus> statuses
    );

    List<Ticket> findByShowtimeIdAndStatusIn(
            Long showtimeId,
            List<TicketStatus> statuses
    );

    @Query("""
        select t
        from Ticket t
        where t.status = :status
          and t.showtime.startTime <= :cutoff
    """)
    List<Ticket> findExpiredReservations(
            @Param("status") TicketStatus status,
            @Param("cutoff") LocalDateTime cutoff
    );
}
