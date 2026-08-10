package com.example.androidlearn.feature.intermediate.detail.stage5

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

/*
 * 蓝牙与 Wi-Fi 连接
 * 官方文档：https://developer.android.com/guide/topics/connectivity/bluetooth
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  1  BLE 低功耗蓝牙
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 1.1  BLE 扫描 ─────────────────────────────────────────────────────────────
 *
 *  · BluetoothLeScanner.startScan()，过滤设备名/服务 UUID
 *  · 扫描会大量消耗电量，找到设备后及时停止 stopScan()
 *
 *  val scanner = bluetoothAdapter.bluetoothLeScanner
 *  val scanSettings = ScanSettings.Builder()
 *      .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
 *      .build()
 *  scanner.startScan(null, scanSettings, object : ScanCallback() {
 *      override fun onScanResult(callbackType: Int, result: ScanResult) {
 *          val device = result.device
 *      }
 *  })
 *
 * ── 1.2  GATT 连接与读写 ──────────────────────────────────────────────────────
 *
 *  · connectGatt() → onConnectionStateChange → discoverServices
 *  · readCharacteristic()、writeCharacteristic()、setCharacteristicNotification()
 *  · BLE 操作必须在回调中串行执行，不能并发发送多个 GATT 请求
 *
 *  val gatt = device.connectGatt(context, false, object : BluetoothGattCallback() {
 *      override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
 *          if (newState == BluetoothProfile.STATE_CONNECTED) gatt.discoverServices()
 *      }
 *      override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
 *          val characteristic = gatt.getService(SERVICE_UUID)?.getCharacteristic(CHAR_UUID)
 *          gatt.readCharacteristic(characteristic)
 *      }
 *  })
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  2  Wi-Fi Direct（P2P）
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · WifiP2pManager：无需路由器直接设备间传输大文件
 *  · 适合：文件分享、本地多人游戏、打印等场景
 *  · 发现设备 → 连接 → 建立 Socket 传输数据
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  3  NFC 近场通信
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · NfcAdapter.enableReaderMode()：读取 NFC Tag
 *  · NDEF 格式：标准化数据格式，支持文本、URI、MIME 类型
 *  · 适合：门禁卡、支付、设备配对等场景
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  4  权限要求
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · Android 12+：BLUETOOTH_SCAN / BLUETOOTH_CONNECT（新权限体系）
 *  · Android 11 及以下：BLUETOOTH / ACCESS_FINE_LOCATION
 *  · BLUETOOTH_SCAN 需要 neverForLocation=true 才不强制定位权限
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  5  最佳实践
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · Android 12+ 蓝牙权限体系重构，注意版本兼容
 *  · BLE 操作必须在回调中串行执行，不能并发发送多个 GATT 请求
 *  · 扫描会大量消耗电量，找到设备后及时停止 stopScan()
 */

val bluetoothWifiData = NoteData(
    title = "蓝牙与 Wi-Fi 连接",
    subtitle = "多媒体与系统能力 · BLE · GATT · Wi-Fi Direct · NFC",
    color = Color.parseColor("#00BCD4"),
    chapters = listOf(
        ChapterItem("1",   "BLE 低功耗蓝牙"),
        ChapterItem("1.1", "BLE 扫描"),
        ChapterItem("1.2", "GATT 连接与读写"),
        ChapterItem("2",   "Wi-Fi Direct（P2P）"),
        ChapterItem("3",   "NFC 近场通信"),
        ChapterItem("4",   "权限要求"),
        ChapterItem("5",   "最佳实践"),
    )
)
