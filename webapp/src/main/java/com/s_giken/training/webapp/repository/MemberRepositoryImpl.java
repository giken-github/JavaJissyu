package com.s_giken.training.webapp.repository;

import java.sql.Types;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import com.s_giken.training.webapp.model.entity.Member;

@Repository
public class MemberRepositoryImpl implements MemberRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Member> rowMapper;

    public MemberRepositoryImpl(JdbcTemplate jdbcTemplate, RowMapper<Member> rowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = rowMapper;
    }

    @Override
    public List<Member> findAll() {
        String sql = "SELECT * FROM T_MEMBER";
        List<Member> result = jdbcTemplate.query(sql, rowMapper);
        return result;
    }

    @Override
    public Optional<Member> findById(Long id) {
        String sql = "SELECT * FROM T_MEMBER WHERE member_id = ?";
        Object[] args = { id };
        int[] argTypes = { Types.BIGINT };
        Member member = jdbcTemplate.queryForObject(sql, args, argTypes, rowMapper);
        return Optional.ofNullable(member);
    }

    @Override
    public List<Member> findByMailLike(String mail) {
        String sql = "SELECT * FROM T_MEMBER WHERE mail like ?";
        Object[] args = { mail };
        int[] argTypes = { Types.VARCHAR };
        List<Member> result = jdbcTemplate.query(sql, args, argTypes, rowMapper);
        return result;
    }

    @Override
    public int add(Member member) {
        Long memberId = member.getMemberId();
        if (memberId == null) {
            memberId = jdbcTemplate.queryForObject("SELECT NEXT VALUE FOR t_member_seq", Long.class);
            member.setMemberId(memberId);
        }

        String sql = """
                        INSERT INTO T_MEMBER (member_id, mail, name, address, start_date, end_date, payment_method, created_at, modified_at)
                        VALUES (?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
                """;
        int processed_count = jdbcTemplate.update(
                sql,
                memberId,
                member.getMail(),
                member.getName(),
                member.getAddress(),
                member.getStartDate(),
                member.getEndDate(),
                member.getPaymentMethod().getCode());

        return processed_count;
    }

    @Override
    public int update(Member member) {
        String sql = """
                    UPDATE T_MEMBER
                    SET
                        mail = ?,
                        name = ?,
                        address = ?,
                        start_date = ?,
                        end_date = ?,
                        payment_method = ?,
                        modified_at = CURRENT_TIMESTAMP
                    WHERE member_id = ?
                """;
        int processed_count = jdbcTemplate.update(
                sql,
                member.getMail(),
                member.getName(),
                member.getAddress(),
                member.getStartDate(),
                member.getEndDate(),
                member.getPaymentMethod().getCode(),
                member.getMemberId());

        return processed_count;
    }

    @Override
    public int deleteById(Long id) {
        String sql = "DELETE FROM T_MEMBER WHERE member_id = ?";

        int processed_count = jdbcTemplate.update(sql, id);

        return processed_count;
    }
}
