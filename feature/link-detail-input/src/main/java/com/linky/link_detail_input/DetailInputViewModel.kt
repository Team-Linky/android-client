package com.linky.link_detail_input

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.kedia.ogparser.OpenGraphCacheProvider
import com.kedia.ogparser.OpenGraphCallback
import com.kedia.ogparser.OpenGraphParser
import com.kedia.ogparser.OpenGraphResult
import com.linky.data.usecase.link.LinkInsertUseCase
import com.linky.data.usecase.tag.GetTagsUseCase
import com.linky.data.usecase.tag.TagDeleteUseCase
import com.linky.data.usecase.tag.TagInsertUseCase
import com.linky.data.usecase.tag.UpdateLinkIdsUseCase
import com.linky.link_detail_input.model.toOpenGraphData
import com.linky.model.Link
import com.linky.model.Tag
import com.linky.model.open_graph.OpenGraphData
import com.sun5066.common.safe_coroutine.builder.safeLaunch
import com.sun5066.common.safe_coroutine.dispatchers.Dispatcher
import com.sun5066.common.safe_coroutine.dispatchers.LinkyDispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.plus
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class DetailInputViewModel @Inject constructor(
    application: Application,
    savedStateHandle: SavedStateHandle,
    getTagsUseCase: GetTagsUseCase,
    private val tagInsertUseCase: TagInsertUseCase,
    private val tagDeleteUseCase: TagDeleteUseCase,
    private val linkInsertUseCase: LinkInsertUseCase,
    private val updateLinkIdsUseCase: UpdateLinkIdsUseCase,
    @Dispatcher(LinkyDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) : AndroidViewModel(application), ContainerHost<State, SideEffect> {

    override val container: Container<State, SideEffect> = container(State.Loading)

    val tagsState = getTagsUseCase.invoke().stateIn(
        scope = viewModelScope.plus(ioDispatcher),
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    init {
        savedStateHandle.get<String>("url")?.also { parse(application.applicationContext, it) }
    }

    private fun parse(context: Context, url: String) {
        OpenGraphParser(
            listener = object : OpenGraphCallback {
                override fun onError(error: String) {
                    intent {
                        reduce { State.Fail(error) }
                    }
                }

                override fun onPostResponse(openGraphResult: OpenGraphResult) {
                    intent {
                        reduce { State.Success(openGraphResult.toOpenGraphData()) }
                    }
                }
            },
            showNullOnEmpty = true,
            cacheProvider = OpenGraphCacheProvider(context),
        ).apply { parse(url) }
    }

    fun addTag(name: String) {
        viewModelScope.safeLaunch(ioDispatcher) {
            tagInsertUseCase.invoke(Tag(name = name))
        }
    }

    fun deleteTag(id: Long) {
        viewModelScope.safeLaunch(ioDispatcher) {
            tagDeleteUseCase.invoke(id)
        }
    }

    fun addLink(link: Link) {
        viewModelScope.safeLaunch(
            ioDispatcher + CoroutineExceptionHandler { _, throwable ->
                intent { postSideEffect(SideEffect.LinkInsertFail) }
            }
        ) {
            intent {
                val id = linkInsertUseCase.invoke(link)
                updateLinkIdsUseCase.invoke(link.tags, id)
                postSideEffect(SideEffect.LinkInsertSuccess)
            }
        }
    }

}

sealed interface State {
    object Loading : State
    data class Success(val openGraphData: OpenGraphData) : State
    data class Fail(val message: String) : State
}

sealed interface SideEffect {
    object LinkInsertSuccess : SideEffect
    object LinkInsertFail : SideEffect
}