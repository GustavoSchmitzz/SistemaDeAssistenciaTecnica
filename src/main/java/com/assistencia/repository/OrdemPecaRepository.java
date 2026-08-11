package com.assistencia.repository;

import com.assistencia.entity.OrdemPeca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdemPecaRepository extends JpaRepository<OrdemPeca, Integer> {
}
