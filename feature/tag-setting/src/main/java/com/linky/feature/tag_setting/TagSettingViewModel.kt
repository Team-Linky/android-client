package com.linky.feature.tag_setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.linky.data.usecase.tag.DeleteTagsByIdsUseCase
import com.linky.data.usecase.tag.GetTagCountUseCase
import com.linky.data.usecase.tag.SelectTagsWithLinkCountUseCase
import com.linky.data.usecase.tag.UnDoDeleteTagsUseCase
import com.linky.feature.tag_setting.state.SnackBarAction
import com.linky.feature.tag_setting.state.TagSettingSideEffect
import com.linky.feature.tag_setting.state.TagSettingState
import com.linky.feature.tag_setting.state.TagSettingState.Companion.Init
import com.linky.model.Tag
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class TagSettingViewModel @Inject constructor(
    private val getTagCountUseCase: GetTagCountUseCase,
    private val selectTagsWithLinkCountUseCase: SelectTagsWithLinkCountUseCase,
    private val deleteTagsByIdsUseCase: DeleteTagsByIdsUseCase,
    private val unDoDeleteTagsUseCase: UnDoDeleteTagsUseCase,
) : ContainerHost<TagSettingState, TagSettingSideEffect>, ViewModel() {

    override val container = container<TagSettingState, TagSettingSideEffect>(Init)

    fun doAction(action: TagSettingAction) {
        when (action) {
            is TagSettingAction.TagDelete -> deleteTagsByIds(action.tags)
            is TagSettingAction.UnDoDelete -> undoDeleteTags()
        }
    }

    private fun getTagCount() {
        intent {
            getTagCountUseCase.invoke().collectLatest { count ->
                reduce { state.copy(tagCount = count) }
            }
        }
    }

    private fun getTagsWithLinkCounts() {
        intent {
            val tagWithLinkCounts = selectTagsWithLinkCountUseCase.invoke().cachedIn(viewModelScope)

            reduce { state.copy(tagWithLinkCounts = tagWithLinkCounts) }
        }
    }

    private fun deleteTagsByIds(tags: List<Tag>) {
        intent {
            deleteTagsByIdsUseCase.invoke(tags)

            val sideEffect = TagSettingSideEffect.SnackBar(
                message = "태그를 삭제했습니다.",
                snackBarAction = SnackBarAction.UnDoDelete
            )

            postSideEffect(sideEffect)
        }
    }

    private fun undoDeleteTags() {
        intent {
            val ids = unDoDeleteTagsUseCase.invoke()

            if (ids.isNotEmpty()) {
                val sideEffect = TagSettingSideEffect.SnackBar(message = "삭제된 태그를 복구했습니다.")

                postSideEffect(sideEffect)
            }
        }
    }

    init {
        getTagCount()
        getTagsWithLinkCounts()
    }

}

sealed interface TagSettingAction {
    data class TagDelete(val tags: List<Tag>) : TagSettingAction
    data object UnDoDelete : TagSettingAction
}