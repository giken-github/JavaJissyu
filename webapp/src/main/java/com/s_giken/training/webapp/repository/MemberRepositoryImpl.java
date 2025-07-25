package com.s_giken.training.webapp.repository;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.s_giken.training.webapp.model.entity.Member;

@Repository
public class MemberRepositoryImpl implements MemberRepository {

    private final JdbcTemplate jdbcTemplate;

    public MemberRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Member mapToMember(Map<String, Object> r) {
        Long member_id = Optional.ofNullable(r.get("member_id"))
                .filter(Long.class::isInstance)
                .map(Long.class::cast)
                .orElse(null);
        String mail = Optional.ofNullable(r.get("mail"))
                .filter(String.class::isInstance)
                .map(String.class::cast)
                .orElse(null);
        String name = Optional.ofNullable(r.get("name"))
                .filter(String.class::isInstance)
                .map(String.class::cast)
                .orElse(null);
        String address = Optional.ofNullable(r.get("address"))
                .filter(String.class::isInstance)
                .map(String.class::cast)
                .orElse(null);
        LocalDate start_date = Optional.ofNullable(r.get("start_date"))
                .filter(Date.class::isInstance)
                .map((d) -> ((Date) d).toLocalDate())
                .orElse(null);
        LocalDate end_date = Optional.ofNullable(r.get("end_date"))
                .filter(Date.class::isInstance)
                .map((d) -> ((Date) d).toLocalDate())
                .orElse(null);
        Byte payment_method = Optional.ofNullable(r.get("payment_method"))
                .filter(Byte.class::isInstance)
                .map(Byte.class::cast)
                .orElse(null);
        Timestamp created_at = Optional.ofNullable(r.get("created_at"))
                .filter(Timestamp.class::isInstance)
                .map(Timestamp.class::cast)
                .orElse(null);
        Timestamp modified_at = Optional.ofNullable(r.get("modified_at"))
                .filter(Timestamp.class::isInstance)
                .map(Timestamp.class::cast)
                .orElse(null);

        return new Member(
                member_id,
                mail,
                name,
                address,
                start_date,
                end_date,
                payment_method,
                created_at,
                modified_at);
    }

    @Override
    public List<Member> findAll() {
        ArrayList<Member> result = new ArrayList<>();
        String sql = "SELECT * FROM T_MEMBER";
        jdbcTemplate.queryForList(sql).stream().forEach((r) -> {
            result.add(mapToMember(r));
        });

        return result;
    }

    @Override
    public Optional<Member> findById(Long id) {
        String sql = "SELECT * FROM T_MEMBER WHERE member_id = ?";
        var result = jdbcTemplate.queryForMap(sql, id);
        Member member = (result != null) ? mapToMember(result) : null;
        return Optional.ofNullable(member);
    }

    @Override
    public List<Member> findByMailLike(String mail) {
        ArrayList<Member> result = new ArrayList<>();
        String sql = "SELECT * FROM T_MEMBER WHERE mail like ?";
        jdbcTemplate.queryForList(sql, mail).stream().forEach((r) -> {
            result.add(mapToMember(r));
        });

        return result;
    }

}
