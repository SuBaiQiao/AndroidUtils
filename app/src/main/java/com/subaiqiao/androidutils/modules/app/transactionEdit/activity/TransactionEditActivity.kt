package com.subaiqiao.androidutils.modules.app.transactionEdit.activity

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import com.subaiqiao.androidutils.R
import com.subaiqiao.androidutils.modules.app.transactionEdit.data.AppDatabase
import com.subaiqiao.androidutils.modules.app.transactionEdit.entity.Transaction
import com.subaiqiao.androidutils.modules.app.transactionEdit.repository.TransactionRepository
import com.subaiqiao.androidutils.modules.app.transactionEdit.ui.viewModel.TransactionViewModel
import com.subaiqiao.androidutils.utils.common.CommonUtils

class TransactionEditActivity : ComponentActivity() {
    companion object {
        private const val TAG = "TransactionEditActivity"
    }

    private lateinit var viewModel: TransactionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_edit)

        val dao = AppDatabase.getInstance(this).transactionDao()
        val repository = TransactionRepository(dao)

        viewModel = TransactionViewModel(repository);

        // 示例保存逻辑
        findViewById<Button>(R.id.btn_save).setOnClickListener {
            val transaction = Transaction(
                id = CommonUtils.generateSnowflakeId(),
                date = "2025-04-05",
                amount = 100.0,
                type = "income",
                description = "工资收入",
                category_id = null,
                reference_transaction_id = null,
                income_platform_id = 1,
                income_account_id = 1,
                expense_platform_id = null,
                expense_account_id = null,
                upload_status = 0,
                download_status = 0
            )
            viewModel.insert(transaction)
        }
        val transactions = viewModel.getTransactionsByDate("2025-04-05").observe(this, { list ->
            // 更新 UI
            Log.d(TAG, "onCreate: ")
            for (transaction in list) {
                Log.d(TAG, transaction.toString())
            }
        })
    }
}