package com.linky.data.usecase.tag

import com.linky.data.repository.tag.TagRepository
import com.linky.model.Tag
import javax.inject.Inject

class DeleteTagsByIdsUseCase @Inject constructor(
    private val tagRepository: TagRepository
) {
    suspend operator fun invoke(tags: List<Tag>) = tagRepository.deleteTagsByIds(tags)
}