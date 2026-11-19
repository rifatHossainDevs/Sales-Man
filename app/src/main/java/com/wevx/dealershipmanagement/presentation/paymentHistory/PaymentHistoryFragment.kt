package com.wevx.dealershipmanagement.presentation.paymentHistory

import android.annotation.SuppressLint
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import com.wevx.dealershipmanagement.core.common.BaseFragment
import com.wevx.dealershipmanagement.databinding.FragmentPaymentHistoryBinding
import com.wevx.dealershipmanagement.presentation.auth.profile.GetProfileViewModel
import com.wevx.dealershipmanagement.utils.TokenManager
import com.wevx.dealershipmanagement.utils.collectInLifecycle
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class PaymentHistoryFragment : BaseFragment<FragmentPaymentHistoryBinding>(
    FragmentPaymentHistoryBinding::inflate
) {
    private val paymentHistoryViewModel: PaymentHistoryViewModel by viewModels()
    private val profileViewModel: GetProfileViewModel by viewModels()

    private var sellerId: String = ""
    private val months = listOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )
    private val statuses = listOf("Paid", "Pending")

    private var selectedMonth: String = SimpleDateFormat("MMMM", Locale.US).format(Date())
    private var selectedStatus: String = "Paid"

    override fun setAllClickListener() {
        setupSpinners()
        val token = TokenManager(requireContext()).getAccessToken().orEmpty()
        profileViewModel.getProfile("Bearer $token")
    }

    override fun onResume() {
        super.onResume()
        if (sellerId.isNotBlank()) {
            paymentHistoryViewModel.loadMonthlyPayments(sellerId, selectedMonth, selectedStatus)
        }
    }

    private fun setupSpinners() {
        val monthAdapter = com.wevx.dealershipmanagement.presentation.adapter.CustomSpinnerAdapter(
            requireContext(),
            months
        )

        binding.spinnerMonth.adapter = monthAdapter
        val currentMonthIndex = months.indexOfFirst { it.equals(selectedMonth, ignoreCase = true) }
        if (currentMonthIndex >= 0) {
            binding.spinnerMonth.setSelection(currentMonthIndex)
        }

        val statusAdapter = com.wevx.dealershipmanagement.presentation.adapter.CustomSpinnerAdapter(
            requireContext(),
            statuses
        )

        binding.spinnerStatus.adapter = statusAdapter

        binding.spinnerMonth.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val nextMonth = months[position]
                if (nextMonth != selectedMonth) {
                    selectedMonth = nextMonth
                    fetchPaymentsIfReady()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.spinnerStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val nextStatus = statuses[position]
                if (nextStatus != selectedStatus) {
                    selectedStatus = nextStatus
                    fetchPaymentsIfReady()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun fetchPaymentsIfReady() {
        if (sellerId.isNotBlank()) {
            paymentHistoryViewModel.loadMonthlyPayments(sellerId, selectedMonth, selectedStatus)
        }
    }

    override fun allObserver() {
        profileObserver()
        paymentHistoryObserver()
    }

    private fun profileObserver() {
        profileViewModel.profileState.collectInLifecycle(viewLifecycleOwner) { profileState ->
            if (profileState.loading) return@collectInLifecycle
            profileState.data?.let {
                sellerId = it.id
                fetchPaymentsIfReady()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun paymentHistoryObserver() {
        paymentHistoryViewModel.paymentHistoryState.collectInLifecycle(viewLifecycleOwner) { state ->
            if (state.loading) {
                loading.show()
                return@collectInLifecycle
            }
            loading.dismiss()

            val group = state.data
            val list = group?.paymentsList.orEmpty().sortedByDescending { it.paidAt ?: it.createdAt }
            val totalAmount = group?.totalAmount ?: 0.0

            binding.tvSummaryLabel.text = "Total $selectedStatus ($selectedMonth)"
            binding.tvTotalAmount.text = "৳%.2f".format(totalAmount)
            binding.tvTransactionCount.text = list.size.toString()

            if (list.isNotEmpty() && state.error == null) {
                binding.rvPaymentHistory.visibility = View.VISIBLE
                binding.tvNoPaymentHistory.visibility = View.GONE
                binding.rvPaymentHistory.adapter = PaymentHistoryAdapter(list)
            } else {
                binding.rvPaymentHistory.visibility = View.GONE
                binding.tvNoPaymentHistory.visibility = View.VISIBLE
            }
        }
    }
}
