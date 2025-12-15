package com.cinemasystem.mapper;

import com.cinemasystem.dto.TicketDTO;
import com.cinemasystem.entity.Ticket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TicketMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "showtime.id", target = "showtimeId")
    @Mapping(source = "seat.id", target = "seatId")
    TicketDTO toDto(Ticket entity);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "showtime", ignore = true)
    @Mapping(target = "seat", ignore = true)
    @Mapping(target = "purchaseTime", ignore = true)
    Ticket toEntity(TicketDTO dto);
}
