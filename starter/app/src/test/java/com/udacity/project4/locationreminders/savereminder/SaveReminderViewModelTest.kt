package com.udacity.project4.locationreminders.savereminder

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem
import com.udacity.project4.locationreminders.rule.MainCoroutineRule

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class SaveReminderViewModelTest {

    private lateinit var fakeReminderDataSource: FakeDataSource
    private lateinit var saveReminderViewModel: SaveReminderViewModel

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setupViewModel() {
        stopKoin()
        fakeReminderDataSource = FakeDataSource()
        saveReminderViewModel = SaveReminderViewModel(
            ApplicationProvider.getApplicationContext(),
            fakeReminderDataSource)
    }

    @Test
    fun shouldReturnError() = runBlockingTest {
        val result = saveReminderViewModel.validateEnteredData(saveIncompleteReminderDataItem())
        MatcherAssert.assertThat(result, CoreMatchers.`is`(false))
    }

    @Test
    fun showLoading() = runBlockingTest {

        mainCoroutineRule.pauseDispatcher()
        saveReminderViewModel.saveReminder(saveFakeReminderDataItem())

        MatcherAssert.assertThat(saveReminderViewModel.showLoading.value, CoreMatchers.`is`(true))

        mainCoroutineRule.resumeDispatcher()
        MatcherAssert.assertThat(saveReminderViewModel.showLoading.value, CoreMatchers.`is`(false))
    }

    private fun saveIncompleteReminderDataItem(): ReminderDataItem {
        return ReminderDataItem(
            "Test Title",
            "Test Description",
            "",
            -19.917299,
            -43.934559
        )
    }

    private fun saveFakeReminderDataItem(): ReminderDataItem {
        return ReminderDataItem(
            "Test Title",
            "Test Description",
            "Test Location",
            -19.917299,
            -43.9345590
        )
    }
}