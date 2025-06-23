package moe.kanata.room;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import moe.kanata.member.domain.Member;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "member_message")
public class MemberMessage {

    @Id
    @Column(name = "message_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "translated_content")
    private String translatedContent;

    @ManyToOne
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member member;

    @Builder
    public MemberMessage(
        final String content,
        final String translatedContent,
        final Member member
    ) {
        this.content = content;
        this.translatedContent = translatedContent;
        this.member = member;
    }
}
