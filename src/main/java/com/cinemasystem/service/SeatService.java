package com.cinemasystem.service;

import com.cinemasystem.dto.SeatDTO;
import com.cinemasystem.entity.Room;
import com.cinemasystem.entity.Seat;
import com.cinemasystem.exception.NotFoundException;
import com.cinemasystem.mapper.SeatMapper;
import com.cinemasystem.repository.RoomRepository;
import com.cinemasystem.repository.SeatRepository;
import com.cinemasystem.validator.SeatValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SeatService {

    private final SeatRepository seatRepository;
    private final RoomRepository roomRepository;
    private final SeatMapper seatMapper;
    private final SeatValidator seatValidator;

    public SeatDTO create(SeatDTO dto) {
        seatValidator.validateForCreate(dto);

        Room room = roomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new NotFoundException("Room not found with id " + dto.getRoomId()));

        Seat entity = seatMapper.toEntity(dto)
                .toBuilder()
                .room(room)
                .build();

        Seat saved = seatRepository.save(entity);
        return seatMapper.toDto(saved);
    }

    public SeatDTO update(Long id, SeatDTO dto) {
        seatValidator.validateForUpdate(id, dto);

        Seat existing = seatRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Seat not found with id " + id));

        Room room = roomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new NotFoundException("Room not found with id " + dto.getRoomId()));

        Seat updated = seatMapper.toEntity(dto)
                .toBuilder()
                .id(existing.getId())
                .room(room)
                .build();

        Seat saved = seatRepository.save(updated);
        return seatMapper.toDto(saved);
    }

    public void delete(Long id) {
        if (!seatRepository.existsById(id)) {
            throw new NotFoundException("Seat not found with id " + id);
        }
        seatRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public SeatDTO getById(Long id) {
        return seatRepository.findById(id)
                .map(seatMapper::toDto)
                .orElseThrow(() -> new NotFoundException("Seat not found with id " + id));
    }

    @Transactional(readOnly = true)
    public List<SeatDTO> getAll() {
        return seatRepository.findAll()
                .stream()
                .map(seatMapper::toDto)
                .toList();
    }
}
