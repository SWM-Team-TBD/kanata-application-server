package moe.kanata.room;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import moe.kanata.global.domain.BaseTimeEntity;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "character_message")
public class CharacterMessage extends BaseTimeEntity {

    @Id
    @Column(name = "message_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "translated_content")
    private String translatedContent;

    @ManyToOne
    @JoinColumn(name = "character_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Character character;

    @Builder
    public CharacterMessage(
        final String content,
        final String translatedContent,
        final Character character
    ) {
        this.content = content;
        this.translatedContent = translatedContent;
        this.character = character;
    }
}
