package moe.kanata.room;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberMessageRepository extends JpaRepository<MemberMessage, Long> {
}
