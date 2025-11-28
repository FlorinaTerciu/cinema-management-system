package com.cinemasystem.service;

import com.cinemasystem.dto.RoomDTO;
import com.cinemasystem.entity.Room;
import com.cinemasystem.exception.NotFoundException;
import com.cinemasystem.mapper.RoomMapper;
import com.cinemasystem.repository.RoomRepository;
import com.cinemasystem.validator.RoomValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;
    private final RoomValidator roomValidator;

    public RoomDTO create(RoomDTO dto) {
        roomValidator.validateForCreate(dto);

        Room saved = roomRepository.save(roomMapper.toEntity(dto));
        return roomMapper.toDto(saved);
    }

    public RoomDTO update(Long id, RoomDTO dto) {
        roomValidator.validateForUpdate(id, dto);

        Room existing = roomRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Room not found with id " + id));

        Room updated = roomMapper.toEntity(dto)
                .toBuilder()
                .id(existing.getId())
                .build();

        Room saved = roomRepository.save(updated);
        return roomMapper.toDto(saved);
    }

    public void delete(Long id) {
        if (!roomRepository.existsById(id)) {
            throw new NotFoundException("Room not found with id " + id);
        }
        roomRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public RoomDTO getById(Long id) {
        return roomRepository.findById(id)
                .map(roomMapper::toDto)
                .orElseThrow(() -> new NotFoundException("Room not found with id " + id));
    }

    @Transactional(readOnly = true)
    public List<RoomDTO> getAll() {
        return roomRepository.findAll()
                .stream()
                .map(roomMapper::toDto)
                .toList();
    }
}
