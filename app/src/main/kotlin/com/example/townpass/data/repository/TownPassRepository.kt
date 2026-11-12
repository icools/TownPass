package com.example.townpass.data.repository

import android.content.Context
import com.example.townpass.data.model.AccountData
import com.example.townpass.data.model.AccountResponse
import com.example.townpass.data.model.ActivityItem
import com.example.townpass.data.model.ActivityResponse
import com.example.townpass.data.model.BannerItem
import com.example.townpass.data.model.CityServiceItem
import com.example.townpass.data.model.HomeBannerResponse
import com.example.townpass.data.model.MessageItem
import com.example.townpass.data.model.MessageResponse
import com.example.townpass.data.model.PopularBillItem
import com.example.townpass.data.model.SubscriptionItem
import com.example.townpass.data.model.SubscriptionResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.json.Json

interface TownPassRepository {
    val accountFlow: Flow<AccountData>
    val bannersFlow: Flow<List<BannerItem>>
    val activitiesFlow: Flow<List<ActivityItem>>
    val messagesFlow: Flow<List<MessageItem>>
    val subscriptionsFlow: Flow<List<SubscriptionItem>>
    val servicesFlow: Flow<List<CityServiceItem>>
    val bills: List<PopularBillItem>

    fun updateAccount(
        realName: String? = null,
        idNo: String? = null,
        birthday: String? = null,
        email: String? = null,
        phoneNo: String? = null,
        residentAddress: String? = null
    )

    fun markMessageAsRead(id: String)
    fun togglePinService(serviceId: String)
    fun getActivityById(id: String): ActivityItem?
    fun getMessageById(id: String): MessageItem?
}

class TownPassRepositoryImpl(
    private val context: Context? = null
) : TownPassRepository {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        coerceInputValues = true
    }

    private val _account = MutableStateFlow(loadInitialAccount())
    override val accountFlow: Flow<AccountData> = _account.asStateFlow()

    private val _banners = MutableStateFlow(loadInitialBanners())
    override val bannersFlow: Flow<List<BannerItem>> = _banners.asStateFlow()

    private val _activities = MutableStateFlow(loadInitialActivities())
    override val activitiesFlow: Flow<List<ActivityItem>> = _activities.asStateFlow()

    private val _messages = MutableStateFlow(loadInitialMessages())
    override val messagesFlow: Flow<List<MessageItem>> = _messages.asStateFlow()

    private val _subscriptions = MutableStateFlow(loadInitialSubscriptions())
    override val subscriptionsFlow: Flow<List<SubscriptionItem>> = _subscriptions.asStateFlow()

    private val _services = MutableStateFlow(loadInitialServices())
    override val servicesFlow: Flow<List<CityServiceItem>> = _services.asStateFlow()

    override val bills: List<PopularBillItem> = listOf(
        PopularBillItem("fee-car", "停車費", "https://taipei-pass-service.vercel.app/fee-payment/detail/fee-5/"),
        PopularBillItem("fee-water", "水費", "https://taipei-pass-service.vercel.app/fee-payment/detail/fee-6/"),
        PopularBillItem("fee-tax", "地方稅", "https://taipei-pass-service.vercel.app/fee-payment/detail/tax-1/"),
        PopularBillItem("fee-school", "學雜費", "https://taipei-pass-service.vercel.app/fee-payment/detail/fee-7/"),
        PopularBillItem("fee-housing", "國/社宅租金維管費", "https://taipei-pass-service.vercel.app/fee-payment/detail/fee-8/"),
        PopularBillItem("fee-hospital", "聯合醫院醫療費", "https://taipei-pass-service.vercel.app/fee-payment/detail/fee-9/"),
        PopularBillItem("fee-maintenance", "平價住宅維護費", "https://taipei-pass-service.vercel.app/fee-payment/detail/fee-10/"),
        PopularBillItem("fee-market", "公有市場攤鋪位使用費", "https://taipei-pass-service.vercel.app/fee-payment/detail/fee-11/")
    )

    private fun loadAssetString(fileName: String): String? {
        return try {
            context?.assets?.open(fileName)?.bufferedReader()?.use { it.readText() }
        } catch (_: Exception) {
            null
        }
    }

    private fun loadInitialAccount(): AccountData {
        val jsonStr = loadAssetString("mock_data/account.json")
        if (jsonStr != null) {
            try {
                val parsed = json.decodeFromString<AccountResponse>(jsonStr)
                return parsed.data
            } catch (_: Exception) {}
        }
        return AccountData(
            id = "mock-account-1",
            account = "wz7786",
            username = "Wesley",
            realName = "金大森",
            idNo = "A123456789",
            email = "ist83903@bcaoo.com",
            phoneNo = "0932166777",
            birthday = "1988/12/12",
            residentAddress = "臺北市中山區吉林路 69 號 4 樓"
        )
    }

    private fun loadInitialBanners(): List<BannerItem> {
        val jsonStr = loadAssetString("mock_data/home_banner.json")
        if (jsonStr != null) {
            try {
                val parsed = json.decodeFromString<HomeBannerResponse>(jsonStr)
                return parsed.data
            } catch (_: Exception) {}
        }
        return listOf(
            BannerItem(
                imageUrl = "https://usport-file.usport.tw/images/cms/cms20240417114242_661f4532678e0_1920_1200.jpg",
                webUrl = "https://usport.gov.taipei",
                title = "台北樂運動"
            ),
            BannerItem(
                imageUrl = "https://img.youtube.com/vi/dLq9uv0X0Nw/maxresdefault.jpg",
                webUrl = "https://co2-passbook.com",
                title = "我的減碳存摺"
            )
        )
    }

    private fun loadInitialActivities(): List<ActivityItem> {
        val jsonStr = loadAssetString("mock_data/activity.json")
        if (jsonStr != null) {
            try {
                val parsed = json.decodeFromString<ActivityResponse>(jsonStr)
                return parsed.data.mapIndexed { index, item ->
                    item.copy(id = if (item.id.isBlank()) "act_$index" else item.id)
                }
            } catch (_: Exception) {}
        }
        return emptyList()
    }

    private fun loadInitialMessages(): List<MessageItem> {
        val jsonStr = loadAssetString("mock_data/message.json")
        if (jsonStr != null) {
            try {
                val parsed = json.decodeFromString<MessageResponse>(jsonStr)
                return parsed.data.mapIndexed { index, item ->
                    item.copy(id = if (item.id.isBlank()) "msg_$index" else item.id)
                }
            } catch (_: Exception) {}
        }
        return emptyList()
    }

    private fun loadInitialSubscriptions(): List<SubscriptionItem> {
        val jsonStr = loadAssetString("mock_data/subscription.json")
        if (jsonStr != null) {
            try {
                val parsed = json.decodeFromString<SubscriptionResponse>(jsonStr)
                return parsed.data
            } catch (_: Exception) {}
        }
        return emptyList()
    }

    private fun loadInitialServices(): List<CityServiceItem> {
        return listOf(
            CityServiceItem("1999", "1999網路電話", "撥打網路語音通話", "市政服務", "https://taipei-pass-service.vercel.app/1999", isPinned = true),
            CityServiceItem("district", "申辦服務", "線上申辦市政府服務各項目", "市政服務", "https://taipei-pass-service.vercel.app/", isPinned = true),
            CityServiceItem("talk", "有話要說", "陳情系統與市民建言", "市政服務", "https://taipei-pass-service.vercel.app/citizen-report/", isPinned = true),
            CityServiceItem("call_num", "臨櫃叫號", "線上抽取號碼牌及進度查詢", "市政服務", "https://taipei-pass-service.vercel.app/booking", isPinned = true),
            CityServiceItem("voting", "i-Voting", "公共政策網路參與投票", "生活休閒", "https://ivoting.taipei", isPinned = true),
            CityServiceItem("zoo", "台北市立動物園", "門票預約與園區導覽", "生活休閒", "https://www.zoo.gov.taipei", isPinned = true),
            CityServiceItem("library", "電子借閱證", "台北市立圖書館數位借閱證", "生活休閒", "https://tpml.gov.taipei", isPinned = true),
            CityServiceItem("water", "水費查繳", "台北自來水事業處水費查詢", "生活休閒", "https://water.gov.taipei", isPinned = true),
            CityServiceItem("police", "警政服務", "報案與交通違規檢舉", "市政服務", "https://police.gov.taipei", isPinned = false),
            CityServiceItem("radio", "台北廣播電台", "線上隨選收聽市政廣播", "生活休閒", "https://radio.gov.taipei", isPinned = false)
        )
    }

    override fun updateAccount(
        realName: String?,
        idNo: String?,
        birthday: String?,
        email: String?,
        phoneNo: String?,
        residentAddress: String?
    ) {
        _account.update { current ->
            current.copy(
                realName = realName ?: current.realName,
                idNo = idNo ?: current.idNo,
                birthday = birthday ?: current.birthday,
                email = email ?: current.email,
                phoneNo = phoneNo ?: current.phoneNo,
                residentAddress = residentAddress ?: current.residentAddress
            )
        }
    }

    override fun markMessageAsRead(id: String) {
        _messages.update { current ->
            current.map { if (it.id == id) it.copy(hasRead = true) else it }
        }
    }

    override fun togglePinService(serviceId: String) {
        _services.update { current ->
            current.map {
                if (it.id == serviceId) it.copy(isPinned = !it.isPinned) else it
            }
        }
    }

    override fun getActivityById(id: String): ActivityItem? {
        val list = _activities.value
        return list.firstOrNull { it.id == id } ?: list.firstOrNull()
    }

    override fun getMessageById(id: String): MessageItem? {
        val list = _messages.value
        return list.firstOrNull { it.id == id } ?: list.firstOrNull()
    }
}
