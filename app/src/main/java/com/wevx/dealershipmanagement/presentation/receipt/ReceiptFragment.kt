package com.wevx.dealershipmanagement.presentation.receipt

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.wevx.dealershipmanagement.R
import com.wevx.dealershipmanagement.core.common.BaseFragment
import com.wevx.dealershipmanagement.databinding.FragmentReceiptBinding
import com.wevx.dealershipmanagement.domain.models.CartItem
import com.wevx.dealershipmanagement.presentation.auth.profile.GetProfileViewModel
import com.wevx.dealershipmanagement.presentation.storeOwnerDetails.GetStoreByIdViewModel
import com.wevx.dealershipmanagement.utils.SharedData
import com.wevx.dealershipmanagement.utils.TokenManager
import com.wevx.dealershipmanagement.utils.collectInLifecycle
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
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
    private var productList: List<CartItem> = SharedData.selectedProductList.toList()
    private var salesmanName: String = "Sales Representative"
    private var storeName: String = "Retail Store"
    private var customerId: String = ""
    private var customerAddress: String = "Dhaka"
    private val args: ReceiptFragmentArgs by navArgs()

    override fun setAllClickListener() {
        customerId = args.id
        val tokenManager = TokenManager(requireContext())
        val token = "Bearer ${tokenManager.getAccessToken()}"
        receipt()
        storeOwnerViewModel.getStoreById(customerId)
        getProfileViewModel.getProfile(token)

        binding.btnSavePdf.setOnClickListener {
            saveReceiptAsPdf()
        }

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
                receipt()
            }
            profileState.data?.let {
                if (it.name.isNotBlank()) {
                    salesmanName = it.name
                }
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
                receipt()
            }
            storeByIdState.data?.let {
                loading.dismiss()
                if (it.storeName.isNotBlank()) {
                    storeName = it.storeName
                }
                if (it.address.isNotBlank()) {
                    customerAddress = it.address
                }
                receipt()
            }
        }
    }

    private fun receipt() {
        val receiptString = buildReceiptText(storeName, customerAddress, salesmanName)
        binding.txtReceiptPreview.text = receiptString
        binding.txtReceiptPreview.typeface = Typeface.MONOSPACE
    }

    private fun saveReceiptAsPdf() {
        val receiptText = buildReceiptText(storeName, customerAddress, salesmanName)
        val lines = receiptText.lines()
        val safeInvoice = args.invoice.replace(Regex("[^A-Za-z0-9_-]"), "_")
        val fileName = "Receipt_$safeInvoice.pdf"

        val pdfDocument = PdfDocument()
        try {
            val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                typeface = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL)
                textSize = 11f
                color = Color.BLACK
            }

            val lineHeight = 18f
            val marginHorizontal = 28f
            val marginVertical = 36f
            val pageWidth = 360
            val pageHeight = (marginVertical * 2 + (lines.size + 2) * lineHeight).toInt().coerceAtLeast(420)

            val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
            val page = pdfDocument.startPage(pageInfo)
            val canvas = page.canvas

            canvas.drawColor(Color.WHITE)

            var y = marginVertical
            for (line in lines) {
                canvas.drawText(line, marginHorizontal, y, paint)
                y += lineHeight
            }

            pdfDocument.finishPage(page)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val resolver = requireContext().contentResolver
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                    put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                }
                val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                    ?: throw IllegalStateException("Could not create PDF file in Downloads")
                resolver.openOutputStream(uri)?.use { outputStream ->
                    pdfDocument.writeTo(outputStream)
                }
            } else {
                val downloadsDir =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                if (!downloadsDir.exists()) {
                    downloadsDir.mkdirs()
                }
                val outFile = File(downloadsDir, fileName)
                FileOutputStream(outFile).use { outputStream ->
                    pdfDocument.writeTo(outputStream)
                }
            }

            Toast.makeText(
                requireContext(),
                "Saved to Downloads: $fileName",
                Toast.LENGTH_LONG
            ).show()
        } catch (e: Exception) {
            Toast.makeText(
                requireContext(),
                "Failed to save PDF: ${e.localizedMessage}",
                Toast.LENGTH_LONG
            ).show()
        } finally {
            pdfDocument.close()
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
        return pairedDevices.firstOrNull { (it.name ?: "").contains("printer", ignoreCase = true) }
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

        val receiptText = buildReceiptText(storeName, customerAddress, salesmanName)

        Thread {
            try {
                val uuid = printer.uuids?.firstOrNull()?.uuid
                    ?: UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
                val socket = printer.createRfcommSocketToServiceRecord(uuid)
                socket.connect()

                val outputStream = socket.outputStream
                outputStream.write(receiptText.toByteArray(Charsets.UTF_8))
                outputStream.flush()
                socket.close()

                activity?.runOnUiThread {
                    if (isAdded) {
                        Toast.makeText(
                            requireContext(),
                            "Receipt printed successfully!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                activity?.runOnUiThread {
                    if (isAdded) {
                        Toast.makeText(
                            requireContext(),
                            "Printing failed: ${e.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }.start()
    }

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
        val displayAddress = if (customerAddress.length > 26) {
            customerAddress.take(25) + "..."
        } else {
            customerAddress
        }

        val displayStoreName = if (storeName.length > 26) {
            storeName.take(25) + "..."
        } else {
            storeName
        }

        val displaySalesmanName = if (salesmanName.length > 26) {
            salesmanName.take(25) + "..."
        } else {
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

    private fun centerText(text: String, width: Int): String {
        if (text.length >= width) return text
        val padding = (width - text.length) / 2
        return " ".repeat(padding) + text
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            SharedData.selectedProductList = emptyList()
            findNavController().navigate(R.id.action_receiptFragment_to_homeFragment)
        }
    }
}
