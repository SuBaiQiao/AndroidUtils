package com.subaiqiao.androidutils.modules.app.transactionEdit.ui.viewModel

import androidx.lifecycle.*
import com.subaiqiao.androidutils.modules.app.transactionEdit.entity.Transaction
import com.subaiqiao.androidutils.modules.app.transactionEdit.repository.TransactionRepository
import kotlinx.coroutines.launch

class TransactionViewModel(private val repository: TransactionRepository) : ViewModel() {

    fun insert(transaction: Transaction) = viewModelScope.launch {
        repository.insert(transaction)
    }

    fun update(transaction: Transaction) = viewModelScope.launch {
        repository.update(transaction)
    }

    fun getTransactionsByDate(date: String): LiveData<List<Transaction>> {
        return repository.getTransactionsByDate(date).asLiveData()
    }


    fun syncToPC(ids: List<Long>) = viewModelScope.launch {
        repository.markAsUploaded(ids)
    }
}