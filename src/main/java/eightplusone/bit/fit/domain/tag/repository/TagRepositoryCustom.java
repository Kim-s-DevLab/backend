package eightplusone.bit.fit.domain.tag.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import eightplusone.bit.fit.domain.tag.dto.TagResponseDto;

public interface TagRepositoryCustom {
	Page<Object[]> tagFilterAndSearch(Pageable pageable, TagResponseDto dto);
}
