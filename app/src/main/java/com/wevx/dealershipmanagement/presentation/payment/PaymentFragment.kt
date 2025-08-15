package com.wevx.dealershipmanagement.presentation.payment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.widget.CheckBox
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.wevx.dealershipmanagement.utils.SharedData
import com.wevx.dealershipmanagement.core.common.BaseFragment
import com.wevx.dealershipmanagement.data.dto.createOrderDto.RequestCreateOrderDTO
import com.wevx.dealershipmanagement.data.dto.paymentDto.RequestPaymentDTO
import com.wevx.dealershipmanagement.data.dto.shipmentDto.RequestShipmentDTO
import com.wevx.dealershipmanagement.databinding.FragmentPaymentBinding
import com.wevx.dealershipmanagement.domain.models.CartItem
import com.wevx.dealershipmanagement.presentation.adapter.ProductCartAdapter
import com.wevx.dealershipmanagement.presentation.auth.profile.GetProfileViewModel
import com.wevx.dealershipmanagement.presentation.order.createOrder.CreateOrderViewModel
import com.wevx.dealershipmanagement.presentation.order.createPayment.CreatePaymentViewModel
import com.wevx.dealershipmanagement.presentation.order.createShipment.CreateShipmentViewModel
import com.wevx.dealershipmanagement.presentation.product.ProductsFragmentArgs
import com.wevx.dealershipmanagement.utils.TokenManager
import com.wevx.dealershipmanagement.utils.collectInLifecycle
import com.wevx.dealershipmanagement.utils.extract
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.getValue
import kotlin.properties.Delegates

@AndroidEntryPoint
class PaymentFragment : BaseFragment<FragmentPaymentBinding>(FragmentPaymentBinding::inflate) {
    private lateinit var adapter: ProductCartAdapter
    lateinit var selectedItems: List<CartItem>
    private lateinit var selectedDate: String
    private val createOrderViewModel: CreateOrderViewModel by viewModels()
    private val currentUserViewModel: GetProfileViewModel by viewModels()
    private val createPaymentViewModel: CreatePaymentViewModel by viewModels()
    private val createShipmentViewModel: CreateShipmentViewModel by viewModels()
    private val args: ProductsFragmentArgs by navArgs()
    private lateinit var customerId: String
    private lateinit var salesManId: String
    lateinit var token: String
    private var total by Delegates.notNull<Double>()
    private lateinit var expectedShipDateIso: String
    private lateinit var orderId: String
    private var isOrderCreated = false
    private var isPaymentCreated = false
    private var isShipmentCreated = false
    lateinit var invoiceNumber: String

    override fun setAllClickListener() {
        customerId = args.id
        val tokenManager = TokenManager(requireContext())
        token = tokenManager.getAccessToken().toString()
        currentUserViewModel.getProfile(token)
        allSelectedProducts()
        buttonClickListener()

    }

    override fun allObserver() {
        createOrderObserver()
        currentUserObserver()
        createPaymentObserver()
        createShipmentObserver()
    }

    private fun createShipmentObserver() {
        createShipmentViewModel.createShipmentState.collectInLifecycle(viewLifecycleOwner) { createShipmentState ->
            if (createShipmentState.loading) return@collectInLifecycle

            createShipmentState.error?.let {
                Toast.makeText(requireContext(), "Shipment Error: $it", Toast.LENGTH_SHORT).show()
                return@collectInLifecycle
            }
            createShipmentState.data?.let {
                isShipmentCreated = true
                checkAllOperationsSuccess()

            }
        }
    }

    private fun createPaymentObserver() {
        createPaymentViewModel.createPaymentState.collectInLifecycle(viewLifecycleOwner) { createPaymentState ->
            if (createPaymentState.loading) return@collectInLifecycle
            createPaymentState.error?.let {
                Toast.makeText(requireContext(), "Payment Error: $it", Toast.LENGTH_SHORT).show()
                return@collectInLifecycle
            }
            createPaymentState.data?.let {
                isPaymentCreated = true
                checkAllOperationsSuccess()

            }
        }
    }

    private fun currentUserObserver() {
        currentUserViewModel.profileState.collectInLifecycle(viewLifecycleOwner) { currentUserState ->
            if (currentUserState.loading) return@collectInLifecycle
            currentUserState.error?.let {
                Toast.makeText(requireContext(), "Error: $it", Toast.LENGTH_SHORT).show()
            }

            currentUserState.data?.let {
                salesManId = it.id
            }
        }
    }

    private fun createOrderObserver() {
        createOrderViewModel.createOrderState.collectInLifecycle(viewLifecycleOwner) { createOrderState ->
            if (createOrderState.loading) {
                loading.show()
                return@collectInLifecycle
            }
            createOrderState.error?.let {
                loading.dismiss()
                Toast.makeText(requireContext(), "Order Error: $it", Toast.LENGTH_SHORT).show()
                return@collectInLifecycle
            }
            createOrderState.data?.let {
                //loading.dismiss()
                orderId = it.data?.id.toString()
                isOrderCreated = true

                val requestPaymentDTO =
                    RequestPaymentDTO(total, orderId, "Cash", "Pending", salesManId)

                val requestShipmentDTO = RequestShipmentDTO(expectedShipDateIso, orderId, "Pending")

                val bearerToken = "Bearer $token"

                createPaymentViewModel.createPayment(requestPaymentDTO, bearerToken)
                createShipmentViewModel.createShipment(requestShipmentDTO, bearerToken)
                //Toast.makeText(requireContext(), "Order Created", Toast.LENGTH_SHORT).show()
                invoiceNumber = it.data?.invoiceNumber.toString()

                checkAllOperationsSuccess()

            }
        }
    }

    private fun checkAllOperationsSuccess() {
        if (isPaymentCreated && isShipmentCreated && isOrderCreated) {
            loading.dismiss()
            Toast.makeText(
                requireContext(),
                "Order, Payment, and Shipment successfully created!",
                Toast.LENGTH_SHORT
            ).show()
            val action =
                PaymentFragmentDirections.actionPaymentFragmentToReceiptFragment(
                    id = customerId,
                    invoice = invoiceNumber
                )

            findNavController().navigate(action)
        }
    }

    @SuppressLint("SetTextI18n")
    fun allSelectedProducts() {
        selectedItems = SharedData.selectedProductList

        adapter = ProductCartAdapter(selectedItems)
        binding.recyclerProducts.adapter = adapter

        total = SharedData.selectedProductList.sumOf { it.subtotal }
        binding.tvTotal.text = "Total: ৳%.2f".format(total)
    }

    private fun buttonClickListener() {
        binding.btnPayment.setOnClickListener {

            val expectedShipmentDate = binding.etExpectedShipmentDate.extract()
            val shippingAddress = binding.etShippingAddress.extract()
            val cash = binding.rbCash
            val check = binding.checkboxConfirmation
            if (checkAllFieldValidity(expectedShipmentDate, shippingAddress, cash, check)) {
                val orderItems = SharedData.selectedProductList.map { cartItem ->
                    RequestCreateOrderDTO.OrderItem(
                        productId = cartItem.product.productId,
                        purchaseQuantity = cartItem.purchaseQuantity.toInt(),
                        priceAtPurchase = cartItem.product.price
                    )
                }

                fun generateInvoiceNumber(): String {
                    val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
                    val datePart = dateFormat.format(Date())
                    val timePartBase36 = System.currentTimeMillis().toString(36).uppercase()
                    val chars = ('A'..'Z') + ('0'..'9')
                    val randomPart = (1..2).map { chars.random() }.joinToString("")

                    return "INV-$datePart-$timePartBase36$randomPart"
                }

                val invoiceNumber = generateInvoiceNumber()
                val requestCreateOrderDTO = RequestCreateOrderDTO(
                    customerId = customerId,
                    salesmanId = salesManId,
                    invoiceNumber = invoiceNumber,
                    //Invoice Format : INV-20250814-KF4Z9AX7, INV-20250814-KF4Z9B2P
                    totalPrice = total,
                    amountPaid = 0.0,
                    paymentDue = total,
                    shippingAddress = shippingAddress,
                    orderItems = orderItems,
                    expectedShipDate = expectedShipDateIso
                )

                val bearerToken = "Bearer $token"
                createOrderViewModel.createOrder(requestCreateOrderDTO, bearerToken)
            }
        }
        binding.etExpectedShipmentDate.setOnClickListener {
            showDatePicker()
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            calendar.set(selectedYear, selectedMonth, selectedDay)

            val displayFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            selectedDate = displayFormat.format(calendar.time)
            binding.etExpectedShipmentDate.setText(selectedDate)

            val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            isoFormat.timeZone = TimeZone.getTimeZone("UTC")
            expectedShipDateIso = isoFormat.format(calendar.time)
        }, year, month, day).show()
    }

    private fun checkAllFieldValidity(
        expectedShipmentDate: String, shippingAddress: String, cash: RadioButton, check: CheckBox
    ): Boolean {

        if (expectedShipmentDate.isEmpty()) {
            binding.etShipmentDateLayout.error = "This field must be filled"
            return false
        }
        if (shippingAddress.isEmpty()) {
            binding.etShippingAddressLayout.error = "This field must be filled"
            return false
        }
        if (!cash.isChecked) {
            binding.rbCash.error = "This field must be filled"
            return false
        }
        if (!check.isChecked) {
            Toast.makeText(requireContext(), "Check Box must be checked", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }
}