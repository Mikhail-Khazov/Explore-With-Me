package ru.practicum.ewm.hit.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.dto.StatsResponseDto;
import ru.practicum.ewm.hit.model.EndpointsHit;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface EndpointsHitStorage extends JpaRepository<EndpointsHit, Long> {

    @Query("select new ru.practicum.dto.StatsResponseDto(r.app, r.uri, count (r.ip)) from EndpointsHit r " +
            "where r.created between :start and :end and r.uri in :uris group by r.app, r.uri order by count (r.ip) desc ")
    List<StatsResponseDto> getAllStats(@Param("start") LocalDateTime start,
                                       @Param("end") LocalDateTime end,
                                       @Param("uris") Collection<String> uris);

    @Query("select new ru.practicum.dto.StatsResponseDto(r.app, r.uri, count (distinct r.ip)) from EndpointsHit r " +
            "where r.created between :start and :end and r.uri in :uris group by r.app, r.uri order by count (r.ip) desc ")
    List<StatsResponseDto> getUniqIpStats(@Param("start") LocalDateTime start,
                                          @Param("end") LocalDateTime end,
                                          @Param("uris") Collection<String> uris);

    @Query("select new ru.practicum.dto.StatsResponseDto(r.app, r.uri, count (r.ip)) from EndpointsHit r " +
            "where r.created between :start and :end group by r.app, r.uri order by count (r.ip) desc ")
    List<StatsResponseDto> getAllStats(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("select new ru.practicum.dto.StatsResponseDto(r.app, r.uri, count (distinct r.ip)) from EndpointsHit r " +
            "where r.created between :start and :end group by r.app, r.uri order by count (r.ip) desc ")
    List<StatsResponseDto> getUniqIpStats(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
