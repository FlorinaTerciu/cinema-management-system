package com.cinemasystem.mapper;

import com.cinemasystem.dto.TicketDTO;
import com.cinemasystem.entity.Ticket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TicketMapper {

    @Mapping(source = "showtime.id", target = "showtimeId")
    @Mapping(source = "seat.id", target = "seatId")
    TicketDTO toDto(Ticket entity);
}
