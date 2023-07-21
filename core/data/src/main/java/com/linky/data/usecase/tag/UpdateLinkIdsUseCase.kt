package com.linky.data.usecase.tag

import com.linky.data.repository.tag.TagRepository
import javax.inject.Inject

class UpdateLinkIdsUseCase @Inject constructor(
    private val tagRepository: TagRepository
) {

    suspend operator fun invoke(tagIds: List<Long>, linkId: Long) = tagRepository.updateUsedLink(tagIds, linkId)

}