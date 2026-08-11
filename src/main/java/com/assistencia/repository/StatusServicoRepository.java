package com.assistencia.repository;

import com.assistencia.entity.StatusServico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusServicoRepository extends JpaRepository<StatusServico, Integer> {
}