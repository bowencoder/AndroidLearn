package com.example.androidlearn.feature.intermediate.detail.stage5

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

private val detail = TopicDetail(
    title = "蓝牙与 Wi-Fi 连接",
    description = "BLE · GATT · Wi-Fi Direct · NFC",
    overview = "Android 支持经典蓝牙（Bluetooth Classic）和低功耗蓝牙（BLE）两种蓝牙协议，以及 Wi-Fi Direct（P2P）和 NFC 近场通信，广泛用于 IoT、智能硬件和数据传输场景。",
    keyPoints = listOf(
        "BLE 扫描：BluetoothLeScanner.startScan()，过滤设备名/服务 UUID",
        "GATT 连接：connectGatt() → onConnectionStateChange → discoverServices",
        "GATT 读写：readCharacteristic()、writeCharacteristic()、setCharacteristicNotification()",
        "Wi-Fi Direct：WifiP2pManager，无需路由器直接设备间传输大文件",
        "NFC：NfcAdapter.enableReaderMode()，读取 NFC Tag，NDEF 格式数据",
        "权限：Android 12+ 需 BLUETOOTH_SCAN / BLUETOOTH_CONNECT，旧版需 BLUETOOTH / ACCESS_FINE_LOCATION"
    ),
    codeSnippet = """
// BLE 扫描
val scanner = bluetoothAdapter.bluetoothLeScanner
val scanSettings = ScanSettings.Builder()
    .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
    .build()
scanner.startScan(null, scanSettings, object : ScanCallback() {
    override fun onScanResult(callbackType: Int, result: ScanResult) {
        val device = result.device
        // 找到目标设备后连接
    }
})

// GATT 连接
val gatt = device.connectGatt(context, false, object : BluetoothGattCallback() {
    override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
        if (newState == BluetoothProfile.STATE_CONNECTED) gatt.discoverServices()
    }
    override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
        val characteristic = gatt.getService(SERVICE_UUID)?.getCharacteristic(CHAR_UUID)
        gatt.readCharacteristic(characteristic)
    }
})
    """.trimIndent(),
    tips = listOf(
        "Android 12+ 蓝牙权限体系重构，BLUETOOTH_SCAN 需要 neverForLocation=true 才不强制定位权限",
        "BLE 操作必须在回调中串行执行，不能并发发送多个 GATT 请求",
        "扫描会大量消耗电量，找到设备后及时停止 stopScan()"
    )
)

@Composable
fun BluetoothWifiScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFF00BCD4),
        stageTitle = "多媒体与系统能力",
        onBack = onBack
    )
}
