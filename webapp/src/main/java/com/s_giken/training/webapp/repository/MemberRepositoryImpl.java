package com.s_giken.training.webapp.repository;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.s_giken.training.webapp.model.entity.Member;

@Repository
public class MemberRepositoryImpl implements MemberRepository {

    private final JdbcTemplate jdbcTemplate;

    public MemberRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private <T> T getValue(Object v, Class<T> type) {
        return Optional.ofNullable(v)
                .map(type::cast)
                .orElse(null);
    }

    private <T> T getValue(Object v, Class<T> type, Function<Object, T> castfunc) {
        return Optional.ofNullable(v)
                .map(castfunc)
                .map(type::cast)
                .orElse(null);
    }

    private Member mapToMember(Map<String, Object> r) {
        return new Member(
                getValue(r.get("member_id"), Long.class),
                getValue(r.get("mail"), String.class),
                getValue(r.get("name"), String.class),
                getValue(r.get("address"), String.class),
                getValue(r.get("start_date"), LocalDate.class, v -> ((Date) v).toLocalDate()),
                getValue(r.get("end_date"), LocalDate.class, v -> ((Date) v).toLocalDate()),
                getValue(r.get("payment_method"), Byte.class, v -> ((Integer) v).byteValue()),
                getValue(r.get("created_at"), Timestamp.class),
                getValue(r.get("modified_at"), Timestamp.class));
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
