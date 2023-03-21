package ru.practicum.ewm.hit.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.hit.model.EndpointsHit;
import ru.practicum.ewm.hit.model.StatsResponse;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface EndpointsHitStorage extends JpaRepository<EndpointsHit, Long> {
    @Query("select new StatsResponse(r.app, r.uri, count (r.ip)) from EndpointsHit r " +
            "where r.timestamp between :start and :end and r.uri in :uris group by r.app, r.uri order by count (r.ip) desc ")
    List<StatsResponse> getAllStats(@Param("start") LocalDateTime start,
                                    @Param("end") LocalDateTime end,
                                    @Param("uris") Collection<String> uris);

    @Query("select new StatsResponse(r.app, r.uri, count (distinct r.ip)) from EndpointsHit r " +
            "where r.timestamp between :start and :end and r.uri in :uris group by r.app, r.uri order by count (r.ip) desc ")
    List<StatsResponse> getUniqIpStats(@Param("start") LocalDateTime start,
                                       @Param("end") LocalDateTime end,
                                       @Param("uris") Collection<String> uris);
}
