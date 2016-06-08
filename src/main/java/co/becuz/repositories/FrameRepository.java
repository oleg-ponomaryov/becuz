package co.becuz.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import co.becuz.domain.Frame;

public interface FrameRepository extends JpaRepository<Frame, String> {
}
