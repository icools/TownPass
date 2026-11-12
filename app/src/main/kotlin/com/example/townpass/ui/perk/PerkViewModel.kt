package com.example.townpass.ui.perk

import androidx.lifecycle.ViewModel

data class CouponItem(
    val id: String,
    val title: String,
    val storeName: String,
    val discount: String,
    val expireDate: String,
    val description: String
)

class PerkViewModel : ViewModel() {
    val coupons: List<CouponItem> = listOf(
        CouponItem(
            id = "c1",
            title = "台北市立動物園門票折抵券",
            storeName = "台北市立動物園",
            discount = "折抵 20 元",
            expireDate = "2026/12/31",
            description = "憑券入園購票享門票現折20元優惠"
        ),
        CouponItem(
            id = "c2",
            title = "北投溫泉博物館文創商品 9 折",
            storeName = "北投溫泉博物館",
            discount = "9 折",
            expireDate = "2026/12/31",
            description = "購買特定文創紀念品享九折優惠"
        ),
        CouponItem(
            id = "c3",
            title = "悠遊卡加值金回饋兌換",
            storeName = "台北通健康集點",
            discount = "兌換 50 元",
            expireDate = "2026/12/31",
            description = "參與市政健康步道活動累積點數折抵"
        ),
        CouponItem(
            id = "c4",
            title = "士林商圈特色店家優惠券",
            storeName = "士林商圈發展協會",
            discount = "滿額折 30 元",
            expireDate = "2026/12/31",
            description = "配合商圈特約店家消費滿額立折"
        )
    )
}
