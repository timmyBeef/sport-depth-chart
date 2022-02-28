package com.example.sportdepthchart.repository;

import com.example.sportdepthchart.model.Sport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SportRepository  extends JpaRepository<Sport, Long> {
}
