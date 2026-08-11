package com.assistencia.repository;

import com.assistencia.entity.PecaComDefeito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PecaComDefeitoRepository extends JpaRepository<PecaComDefeito, Integer> {
}
