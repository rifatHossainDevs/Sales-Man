package com.wevx.dealershipmanagement.presentation.receipt

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.wevx.dealershipmanagement.R
import com.wevx.dealershipmanagement.utils.SharedData
import com.wevx.dealershipmanagement.core.common.BaseFragment
import com.wevx.dealershipmanagement.databinding.FragmentReceiptBinding
import com.wevx.dealershipmanagement.domain.models.CartItem
import com.wevx.dealershipmanagement.presentation.auth.profile.GetProfileViewModel
import com.wevx.dealershipmanagement.presentation.storeOwnerDetails.GetStoreByIdViewModel
import com.wevx.dealershipmanagement.utils.TokenManager
import com.wevx.dealershipmanagement.utils.collectInLifecycle
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import kotlin.getValue
@AndroidEntryPoint
class ReceiptFragment : BaseFragment<FragmentReceiptBinding>(FragmentReceiptBinding::inflate) {

    private val REQUEST_CODE_PERMISSIONS = 1001
    private val storeOwnerViewModel: GetStoreByIdViewModel by viewModels()
    private val getProfileViewModel: GetProfileViewModel by viewModels()
    private var productList: List<CartItem> = SharedData.selectedProductList
    private lateinit var salesmanName: String
    private lateinit var storeName: String
    private lateinit var customerId: String
    private lateinit var customerAddress: String
    private val args: ReceiptFragmentArgs by navArgs()


    override fun setAllClickListener() {
        customerId = args.id
        val tokenManager = TokenManager(requireContext())
        val token = "Bearer ${tokenManager.getAccessToken()}"
        storeOwnerViewModel.getStoreById(customerId)
        getProfileViewModel.getProfile(token)
        binding.btnPrintReceipt.setOnClickListener {
            if (areBluetoothPermissionsGranted()) {
                startPrintingProcess()
            } else {
                requestBluetoothPermissions()
            }
        }
    }

    override fun allObserver() {

        storeOwnerObserver()
        getProfileObserver()
    }

    private fun getProfileObserver() {
        getProfileViewModel.profileState.collectInLifecycle(viewLifecycleOwner) { profileState ->
            if (profileState.loading) return@collectInLifecycle
            profileState.error?.let {

            }
            profileState.data?.let {
                salesmanName = it.name
                receipt()
            }
        }
    }

    private fun storeOwnerObserver() {
        storeOwnerViewModel.getStoreByIdState.collectInLifecycle(viewLifecycleOwner) { storeByIdState ->
            if (storeByIdState.loading) {
                loading.show()
                return@collectInLifecycle
            }
            storeByIdState.error?.let {
                loading.dismiss()

            }
            storeByIdState.data?.let {
                loading.dismiss()
                storeName = it.storeName
                customerAddress = it.address

                receipt()
            }
        }
    }

    private fun receipt() {
        // Only build receipt if both storeName and salesmanName are initialized
        if (!::storeName.isInitialized || !::salesmanName.isInitialized) return

        val receiptString = buildReceiptText(storeName, customerAddress, salesmanName)
        binding.txtReceiptPreview.text = receiptString
        binding.txtReceiptPreview.typeface = Typeface.MONOSPACE
    }

    private fun areBluetoothPermissionsGranted(): Boolean {
        val permissions = mutableListOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissions.add(Manifest.permission.BLUETOOTH_CONNECT)
            permissions.add(Manifest.permission.BLUETOOTH_SCAN)
        }
        return permissions.all {
            ContextCompat.checkSelfPermission(
                requireContext(),
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestBluetoothPermissions() {
        val permissions = mutableListOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissions.add(Manifest.permission.BLUETOOTH_CONNECT)
            permissions.add(Manifest.permission.BLUETOOTH_SCAN)
        }

        ActivityCompat.requestPermissions(
            requireActivity(),
            permissions.toTypedArray(),
            REQUEST_CODE_PERMISSIONS
        )
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                startPrintingProcess()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Bluetooth permissions are required to print",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun startPrintingProcess() {
        val printer = getPairedPrinter()
        if (printer == null) {
            Toast.makeText(requireContext(), "No paired printer found", Toast.LENGTH_SHORT).show()
            return
        }
        printReceipt(printer)
    }

    private fun getPairedPrinter(): BluetoothDevice? {
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter() ?: return null

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return null
            }
        }

        val pairedDevices = bluetoothAdapter.bondedDevices ?: return null
        return pairedDevices.firstOrNull { it.name.contains("printer", ignoreCase = true) }
    }

    private fun printReceipt(printer: BluetoothDevice) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(
                    requireContext(),
                    "Bluetooth Connect permission is required",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }
        }

        Thread {
            try {
                val uuid = printer.uuids?.firstOrNull()?.uuid
                    ?: UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
                val socket = printer.createRfcommSocketToServiceRecord(uuid)
                socket.connect()

                val outputStream = socket.outputStream
                val receiptText = buildReceiptText(storeName, customerAddress, salesmanName)

                outputStream.write(receiptText.toByteArray(Charsets.UTF_8))
                outputStream.flush()
                socket.close()

                requireActivity().runOnUiThread {
                    Toast.makeText(
                        requireContext(),
                        "Receipt printed successfully!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                requireActivity().runOnUiThread {
                    Toast.makeText(
                        requireContext(),
                        "Printing failed: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }.start()
    }

    // build the receipt using CartItem from share data
    @SuppressLint("DefaultLocale")
    private fun buildReceiptText(
        storeName: String,
        customerAddress: String,
        salesmanName: String
    ): String {
        val date = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())

        val lineWidth = 42 // thermal printer width
        val sb = StringBuilder()
        val dashLine = "-".repeat(lineWidth)

        var total = 0.0

        // Header
        sb.appendLine(centerText("DEALERSHIP MANAGEMENT", lineWidth))
        sb.appendLine(centerText("Sales Receipt", lineWidth))
        sb.appendLine()
        val displayAddress = if (customerAddress.length > 26){
            customerAddress.take(25) + "..."
        }
        else{
            customerAddress
        }

        val displayStoreName = if (storeName.length > 26){
            storeName.take(25) + "..."
        }
        else{
            storeName
        }

        val displaySalesmanName = if (salesmanName.length > 26){
            salesmanName.take(25) + "..."
        }
        else{
            salesmanName
        }
        val invoiceNumber = args.invoice

        // Customer & Sales Info
        sb.appendLine("Invoice : $invoiceNumber")
        sb.appendLine("Store   : $displayStoreName")
        sb.appendLine("Address : $displayAddress")
        sb.appendLine("Salesman: $displaySalesmanName")
        sb.appendLine("Date    : $date")
        sb.appendLine()

        // column name
        sb.appendLine(String.format("%-20s %-10s %10s", "Item", "Qty", "Amount"))
        sb.appendLine(dashLine)

        // Items
        for (item in productList) {
            val subtotal = item.subtotal
            total += subtotal

            val name = if (item.product.productName.length > 20)
                item.product.productName.take(17) + "..."
            else
                item.product.productName

            val qtyWithUnit = "${item.purchaseQuantity.toInt()} ${item.product.unit}"

            sb.appendLine(
                String.format(
                    "%-20s %-10s %10s",
                    name,
                    qtyWithUnit,
                    "৳${"%.2f".format(subtotal)}"
                )
            )
        }

        // Footer with Total
        sb.appendLine(dashLine)
        sb.appendLine(String.format("%-30s %10s", "TOTAL:", "৳${"%.2f".format(total)}"))
        sb.appendLine()
        sb.appendLine(centerText("Thank you for your purchase!", lineWidth))

        return sb.toString()
    }

    // take text into center
    private fun centerText(text: String, width: Int): String {
        if (text.length >= width) return text
        val padding = (width - text.length) / 2
        return " ".repeat(padding) + text
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {

            findNavController().navigate(R.id.action_receiptFragment_to_homeFragment)
        }
    }
}
