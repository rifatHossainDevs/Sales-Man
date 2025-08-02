package com.wevx.dealershipmanagement.presentation.receipt

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.os.Build
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.wevx.dealershipmanagement.utils.SharedData
import com.wevx.dealershipmanagement.core.common.BaseFragment
import com.wevx.dealershipmanagement.databinding.FragmentReceiptBinding
import com.wevx.dealershipmanagement.domain.models.CartItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class ReceiptFragment : BaseFragment<FragmentReceiptBinding>(FragmentReceiptBinding::inflate) {

    private val REQUEST_CODE_PERMISSIONS = 1001
    private var productList: List<CartItem> = SharedData.selectedProductList

    override fun setAllClickListener() {
        binding.btnPrintReceipt.setOnClickListener {
            if (areBluetoothPermissionsGranted()) {
                startPrintingProcess()
            } else {
                requestBluetoothPermissions()
            }
        }
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
            ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
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
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                startPrintingProcess()
            } else {
                Toast.makeText(requireContext(), "Bluetooth permissions are required to print", Toast.LENGTH_SHORT).show()
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
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                return null
            }
        }

        val pairedDevices = bluetoothAdapter.bondedDevices ?: return null
        return pairedDevices.firstOrNull { it.name.contains("printer", ignoreCase = true) }
    }

    private fun printReceipt(printer: BluetoothDevice) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(requireContext(), "Bluetooth Connect permission is required", Toast.LENGTH_SHORT).show()
                return
            }
        }

        Thread {
            try {
                val uuid = printer.uuids?.firstOrNull()?.uuid ?: UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
                val socket = printer.createRfcommSocketToServiceRecord(uuid)
                socket.connect()

                val outputStream = socket.outputStream
                val receiptText = buildReceiptText()

                outputStream.write(receiptText.toByteArray(Charsets.UTF_8))
                outputStream.flush()
                socket.close()

                requireActivity().runOnUiThread {
                    Toast.makeText(requireContext(), "Receipt printed successfully!", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                requireActivity().runOnUiThread {
                    Toast.makeText(requireContext(), "Printing failed: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }.start()
    }

    // Build receipt using CartItem
    @SuppressLint("DefaultLocale")
    private fun buildReceiptText(): String {
        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        val itemsText = StringBuilder()
        var total = 0.0

        for (item in productList) {
            val subtotal = item.subtotal
            total += subtotal
            val line = String.format(
                "%-14s %2.0f x %6s = %7.2f",
                item.product.productName,
                item.purchaseQuantity,
                item.product.unit,
                subtotal
            )
            itemsText.appendLine(line)
        }

        return """
            -------------------------------
                 DEALERSHIP MANAGEMENT
                     Sales Receipt
            -------------------------------
            Salesman: Rifat Hossain
            Date: $date

            Items:
            $itemsText-------------------------------
            Total:                      ${"%.2f".format(total)}
            Thank you for your purchase!
            -------------------------------
        """.trimIndent()
    }

    override fun allObserver() {
        val receiptString = buildReceiptText()
        binding.txtReceiptPreview.text = receiptString
        binding.txtReceiptPreview.typeface = Typeface.MONOSPACE
    }
}
