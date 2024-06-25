package com.linky.data.usecase.tag

import com.linky.data.repository.tag.TagRepository
import javax.inject.Inject

class UnDoDeleteTagsUseCase @Inject constructor(
    private val tagRepository: TagRepository
) {
    suspend operator fun invoke() = tagRepository.insertTags(tagRepository.deletedTagsCache)
}