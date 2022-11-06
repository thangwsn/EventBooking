package com.eticket.domain.repo;

import com.eticket.domain.entity.event.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaImageRepository extends JpaRepository<Image, Integer> {
}
