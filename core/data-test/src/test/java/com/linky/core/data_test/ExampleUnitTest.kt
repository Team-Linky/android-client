package com.linky.core.data_test

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import androidx.lifecycle.SavedStateHandle
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.linky.data.repository.link.LinkRepository
import com.linky.data.repository.link.LinkRepositoryImpl
import com.linky.data.usecase.link.GetLinksUseCase
import com.linky.data.usecase.link.IncrementLinkReadCountUseCase
import com.linky.data.usecase.link.LinkSetIsRemoveUseCase
import com.linky.data.usecase.link.SelectLinkByTagNameUseCase
import com.linky.core.data_base.LinkyDataBase
import com.linky.core.data_base.backup.LinkTagCrossRefBackupDao
import com.linky.core.data_base.link.dao.LinkDao
import com.linky.core.data_base.link.dao.LinkTagCrossRefDao
import com.linky.core.data_base.link.data_source.LinkDataSource
import com.linky.core.data_base.link.data_source.LinkDataSourceImpl
import com.linky.core.data_base.tag.dao.TagDao
import com.linky.timeline.TimeLineViewModel
import com.linky.timeline.state.TimeLineState
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.orbitmvi.orbit.test.test

class ExampleInstrumentedTest {

    private lateinit var database: LinkyDataBase

    private lateinit var linkDataSource: LinkDataSource

    private lateinit var linkRepository: LinkRepository

    /** UseCase */
    private lateinit var getLinksUseCase: GetLinksUseCase
    private lateinit var incrementLinkReadCountUseCase: IncrementLinkReadCountUseCase
    private lateinit var linkSetIsRemoveUseCase: LinkSetIsRemoveUseCase
    private lateinit var selectLinkByTagNameUseCase: SelectLinkByTagNameUseCase

    private val savedStateHandle = SavedStateHandle()

    private lateinit var tagDao: TagDao
    private lateinit var linkDao: LinkDao
    private lateinit var linkTagCrossRefDao: LinkTagCrossRefDao
    private lateinit var linkTagCrossRefBackupDao: LinkTagCrossRefBackupDao

    private lateinit var timeLineViewModel: TimeLineViewModel

    @BeforeEach
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            LinkyDataBase::class.java
        ).build()

        tagDao = database.getTagDao()
        linkDao = database.getLinkDao()
        linkTagCrossRefDao = database.getLinkTagCrossRefDao()
        linkTagCrossRefBackupDao = database.getLinkTagCrossRefBackupDao()

        linkDataSource = LinkDataSourceImpl(
            linkDao = linkDao,
            linkTagCrossRefDao = linkTagCrossRefDao,
            linkTagCrossRefBackupDao = linkTagCrossRefBackupDao
        )

        linkRepository = LinkRepositoryImpl(linkDataSource)

        getLinksUseCase = GetLinksUseCase(linkRepository)
        incrementLinkReadCountUseCase = IncrementLinkReadCountUseCase(linkRepository)
        linkSetIsRemoveUseCase = LinkSetIsRemoveUseCase(linkRepository)
        selectLinkByTagNameUseCase = SelectLinkByTagNameUseCase(linkRepository)

        timeLineViewModel = TimeLineViewModel(
            getLinksUseCase = getLinksUseCase,
            incrementLinkReadCountUseCase = incrementLinkReadCountUseCase,
            linkSetIsRemoveUseCase = linkSetIsRemoveUseCase,
            selectLinkByTagNameUseCase = selectLinkByTagNameUseCase,
            savedStateHandle = savedStateHandle,
        )
    }

    @Test
    fun test_getLinksUseCase() = runTest {
        timeLineViewModel.test(this, TimeLineState.Init) {
            runOnCreate()
            expectInitialState()
        }
    }
}

class InstantTaskExecutorExtension : BeforeEachCallback, AfterEachCallback {
    override fun beforeEach(context: ExtensionContext?) {
        ArchTaskExecutor.getInstance().setDelegate(object : TaskExecutor() {
            override fun executeOnDiskIO(runnable: Runnable) {
                runnable.run()
            }

            override fun postToMainThread(runnable: Runnable) {
                runnable.run()
            }

            override fun isMainThread(): Boolean {
                return true
            }
        })
    }

    override fun afterEach(context: ExtensionContext?) {
        ArchTaskExecutor.getInstance().setDelegate(null)
    }
}