package com.example.townpass.ui.bill

import androidx.lifecycle.ViewModel
import com.example.townpass.data.model.PopularBillItem
import com.example.townpass.data.repository.TownPassRepository

class BillViewModel(
    repository: TownPassRepository
) : ViewModel() {
    val bills: List<PopularBillItem> = repository.bills
}
