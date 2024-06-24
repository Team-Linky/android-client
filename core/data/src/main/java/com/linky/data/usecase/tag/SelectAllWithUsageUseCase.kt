package com.linky.data.usecase.tag

import com.linky.data.repository.tag.TagRepository
import javax.inject.Inject

class SelectAllWithUsageUseCase @Inject constructor(
    private val tagRepository: TagRepository
) {
    operator fun invoke(linkId: Long) = tagRepository.selectAllWithUsage(linkId, 100)
}