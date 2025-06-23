package moe.kanata.room;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import moe.kanata.global.domain.BaseTimeEntity;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "characters")
public class Character extends BaseTimeEntity {

    @Id
    @Column(name = "character_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Builder
    public Character(final String name) {
        this.name = name;
    }
}
