package com.example.vrt.domain.room.repository;

import com.example.vrt.domain.room.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    // Room의 ID는 roomId (String) 기준으로 식별
    // roomRepository.findById(roomId) : 사용자 방 입장
    // room.addParticipant(userId) + roomRepository.save(room) : 입장후 참여자 수 증가
    // 예외발생(IllegalStateException) : 방 꽉찼을 때 예외처리
    // RoomJoinResponseDTO : 응답

    @Query("""
      SELECT r
      FROM Room r
      LEFT JOIN FETCH r.participantIDs
      WHERE r.gallery.id = :galleryId
    """)
    List<Room> findAllByGallery_Id(@Param("galleryId") Long galleryId);
}