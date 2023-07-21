package com.linky.data.usecase.tag

import com.linky.data.repository.tag.TagRepository
import javax.inject.Inject

class GetTagByIdsUseCase @Inject constructor(
    private val tagRepository: TagRepository
) {

    suspend operator fun invoke(ids: List<Long>) = tagRepository.selectByIds(ids)

}