package com.linky.data.usecase.tag

import com.linky.data.repository.tag.TagRepository
import javax.inject.Inject

class TagDeleteUseCase @Inject constructor(
    private val tagRepository: TagRepository
) {

    suspend operator fun invoke(id: Long) = tagRepository.delete(id)

}