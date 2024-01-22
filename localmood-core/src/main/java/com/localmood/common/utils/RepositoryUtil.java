package com.localmood.common.utils;

import org.springframework.data.jpa.repository.JpaRepository;

import com.localmood.common.exception.ErrorCode;
import com.localmood.common.exception.LocalmoodException;

public class RepositoryUtil {
	public static <T> T findByIdOrThrow(JpaRepository<T, Long> repository, Long id, ErrorCode errorCode) {
		return repository.findById(id)
			.orElseThrow(() -> new LocalmoodException(errorCode));
	}
}
