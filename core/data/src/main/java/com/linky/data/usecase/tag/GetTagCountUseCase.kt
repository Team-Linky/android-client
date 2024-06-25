package com.linky.data.usecase.tag

import com.linky.data.repository.tag.TagRepository
import javax.inject.Inject

class GetTagCountUseCase @Inject constructor(
    private val tagRepository: TagRepository
) {
    operator fun invoke() = tagRepository.getTagCount()
}