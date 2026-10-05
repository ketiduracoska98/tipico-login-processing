package org.example.trackloginservice.repository;

import org.example.trackloginservice.entity.LoginEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface LoginEventRepository extends JpaRepository<LoginEventEntity, UUID> {

}
