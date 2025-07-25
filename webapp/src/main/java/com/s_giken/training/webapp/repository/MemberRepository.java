package com.s_giken.training.webapp.repository;

import java.util.List;
import java.util.Optional;
import com.s_giken.training.webapp.model.entity.Member;

public interface MemberRepository {
    public List<Member> findAll();

    public Optional<Member> findById(Long id);

    public List<Member> findByMailLike(String mail);
}
