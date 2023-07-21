package com.linky.data.usecase.tag

import com.linky.data.repository.tag.TagRepository
import com.linky.model.Tag
import javax.inject.Inject

class TagInsertUseCase @Inject constructor(
    private val tagRepository: TagRepository
) {

    suspend operator fun invoke(tag: Tag) = tagRepository.insert(tag)

}