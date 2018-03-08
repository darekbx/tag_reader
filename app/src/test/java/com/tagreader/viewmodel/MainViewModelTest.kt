package com.tagreader.viewmodel

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.tagreader.repository.storage.entities.Item
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class MainViewModelTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    val viewModel by lazy { MainViewModel(RuntimeEnvironment.application) }

    @Test
    fun test() {
        // Given
        val observerState = mock<Observer<List<Item>>> { }
        viewModel.itemList.observeForever(observerState)

        // When
        viewModel.addtag("name")

        Thread.sleep(1000)

        // Then
        verify(observerState).onChanged(any())
    }
}