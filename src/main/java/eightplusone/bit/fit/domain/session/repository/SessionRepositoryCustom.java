package eightplusone.bit.fit.domain.session.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import eightplusone.bit.fit.domain.tag.dto.TagResponseDto;

public interface SessionRepositoryCustom {
	Page<Object[]> tagFilterAndSearch(Pageable pageable, TagResponseDto dto);
}
