package co.becuz.repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import co.becuz.domain.Device;
import co.becuz.domain.User;

public interface DeviceRepository extends JpaRepository<Device, String> {
    Collection<Device> findAllByUser(User user);

    Device findByUserAndToken(User user, String token);

}
