package moe.kanata.room;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import moe.kanata.global.domain.BaseTimeEntity;
import moe.kanata.member.domain.Member;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "room")
public class Room extends BaseTimeEntity {

    @Id
    @Column(name = "room_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_uuid")
    private UUID uuid;

    @OneToOne
    @JoinColumn(name = "charcter_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Character character;

    @OneToOne
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member member;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Builder
    public Room(
        final UUID uuid,
        final Character character,
        final Member member,
        final LocalDateTime startTime,
        final LocalDateTime endTime
    ) {
        this.uuid = uuid;
        this.character = character;
        this.member = member;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
