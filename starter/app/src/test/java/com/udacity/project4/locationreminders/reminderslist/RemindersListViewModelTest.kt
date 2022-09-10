package com.udacity.project4.locationreminders.reminderslist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.rule.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class RemindersListViewModelTest {

    private lateinit var fakeReminderDataSource: FakeDataSource
    private lateinit var remindersViewModel: RemindersListViewModel

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()


    @Before
    fun setupViewModel() {
        fakeReminderDataSource = FakeDataSource()
        remindersViewModel = RemindersListViewModel(
            ApplicationProvider.getApplicationContext(),
            fakeReminderDataSource)
    }

    @Test
    fun testShouldReturnError () = runBlockingTest  {
        fakeReminderDataSource.setShouldReturnError(true)
        saveReminderFakeData()
        remindersViewModel.loadReminders()

        MatcherAssert.assertThat(
            remindersViewModel.showSnackBar.value, CoreMatchers.`is`("Reminders not found")
        )
    }

    @Test
    fun testLoading() = runBlockingTest {

        mainCoroutineRule.pauseDispatcher()
        saveReminderFakeData()
        remindersViewModel.loadReminders()

        MatcherAssert.assertThat(remindersViewModel.showLoading.value, CoreMatchers.`is`(true))

        mainCoroutineRule.resumeDispatcher()
        MatcherAssert.assertThat(remindersViewModel.showLoading.value, CoreMatchers.`is`(false))
    }

    private suspend fun saveReminderFakeData() {
        fakeReminderDataSource.saveReminder(
            ReminderDTO(
                "Test Title",
                "Test Description",
                "Test Location",
                -19.917299,
                -43.934559)
        )
    }

}