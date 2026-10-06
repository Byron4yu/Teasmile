<template>
  <view class="order-list">
    <!-- 状态 tabs -->
    <view class="tabs">
      <view
        v-for="tab in tabs"
        :key="tab.value"
        class="tab"
        :class="{ active: currentTab === tab.value }"
        @click="switchTab(tab.value)"
      >
        {{ tab.label }}
      </view>
    </view>

    <view v-if="!isLogin()" class="empty">
      <text>🔒 登录后查看订单</text>
      <text class="go-login" @click="goLogin">去登录</text>
    </view>

    <view v-else-if="orders.length === 0" class="empty">
      <text>📦 暂无订单</text>
      <text class="go-buy" @click="goHome">去点单</text>
    </view>

    <view v-else class="orders">
      <view class="order-card" v-for="order in orders" :key="order.id" @click="goDetail(order.id)">
        <view class="order-header">
          <text class="order-no">订单号 {{ order.number }}</text>
          <text class="status" :class="statusClass(order.status)">{{ statusText(order.status) }}</text>
        </view>

        <view class="order-body">
          <text class="drinks">{{ order.orderDrinks || '饮品订单' }}</text>
          <view class="order-meta">
            <text class="time">{{ formatTime(order.orderTime) }}</text>
            <text class="amount">实付 ¥{{ order.amount }}</text>
          </view>
        </view>

        <view class="order-actions" @click.stop>
          <button
            v-if="order.status === 1"
            class="btn danger"
            @click="cancel(order.id)"
          >取消订单</button>
          <button
            v-if="order.status === 2 || order.status === 3"
            class="btn warn"
            @click="reminder(order.id)"
          >催单</button>
          <button
            v-if="order.status === 5"
            class="btn primary"
            @click="repetition(order.id)"
          >再来一单</button>
          <button class="btn plain" @click="goDetail(order.id)">订单详情</button>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import { getHistoryOrders, cancelOrder, reminderOrder, repetitionOrder } from '@/api/index.js'
import { isLogin } from '@/utils/request.js'

const tabs = [
  { label: '全部', value: null },
  { label: '待付款', value: 1 },
  { label: '待接单', value: 2 },
  { label: '已完成', value: 5 }
]
const currentTab = ref(null)
const orders = ref([])

const statusMap = {
  1: '待付款', 2: '待接单', 3: '已接单', 4: '派送中',
  5: '已完成', 6: '已取消', 7: '已退款'
}
const statusText = (s) => statusMap[s] || '未知'
const statusClass = (s) => {
  if (s === 5 || s === 3) return 'done'
  if (s === 6 || s === 7) return 'cancel'
  return 'pending'
}
const formatTime = (t) => t ? String(t).replace('T', ' ').slice(0, 16) : ''

const loadOrders = async () => {
  if (!isLogin()) {
    orders.value = []
    return
  }
  try {
    const params = { page: 1, pageSize: 50 }
    if (currentTab.value) params.status = currentTab.value
    const res = await getHistoryOrders(params)
    orders.value = (res && res.records) ? res.records : []
  } catch (e) {
    orders.value = []
  }
}

const switchTab = (val) => {
  currentTab.value = val
  loadOrders()
}

const cancel = (id) => {
  uni.showModal({
    title: '提示', content: '确定取消该订单吗？',
    success: async (r) => {
      if (r.confirm) {
        try { await cancelOrder(id); uni.showToast({ title: '已取消' }); loadOrders() } catch (e) {}
      }
    }
  })
}

const reminder = async (id) => {
  try { await reminderOrder(id); uni.showToast({ title: '已提醒商家' }) } catch (e) {}
}

const repetition = async (id) => {
  try {
    await repetitionOrder(id)
    uni.showToast({ title: '已加入购物车' })
    setTimeout(() => uni.switchTab({ url: '/pages/cart/cart' }), 800)
  } catch (e) {}
}

const goDetail = (id) => uni.navigateTo({ url: `/pages/order-detail/detail?id=${id}` })
const goHome = () => uni.switchTab({ url: '/pages/index/index' })
const goLogin = () => uni.navigateTo({ url: '/pages/login/login' })

onShow(() => loadOrders())
</script>

<style scoped>
.order-list { min-height: 100vh; background: #f5f5f5; padding-bottom: 40rpx; }
.tabs {
  display: flex;
  background: #fff;
  padding: 20rpx;
  gap: 40rpx;
  border-bottom: 1rpx solid #eee;
  overflow-x: auto;
}
.tab { font-size: 28rpx; color: #666; padding: 12rpx 0; white-space: nowrap; }
.tab.active { color: #6B4226; border-bottom: 4rpx solid #6B4226; font-weight: bold; }
.empty { text-align: center; padding: 200rpx 0; color: #999; }
.go-buy, .go-login {
  display: inline-block; margin-top: 30rpx;
  background: #6B4226; color: #fff;
  padding: 16rpx 48rpx; border-radius: 48rpx;
}
.order-card {
  background: #fff;
  margin: 20rpx;
  border-radius: 20rpx;
  overflow: hidden;
}
.order-header {
  display: flex;
  justify-content: space-between;
  padding: 20rpx 30rpx;
  background: #fafafa;
  border-bottom: 1rpx solid #eee;
  font-size: 26rpx;
}
.order-no { color: #333; }
.status { font-weight: bold; }
.status.pending { color: #ff9444; }
.status.done { color: #4cd964; }
.status.cancel { color: #999; }
.order-body { padding: 20rpx 30rpx; }
.drinks { font-size: 28rpx; display: block; margin-bottom: 12rpx; }
.order-meta {
  display: flex; justify-content: space-between;
  font-size: 24rpx; color: #999;
}
.amount { color: #ff6b6b; font-weight: bold; font-size: 28rpx; }
.order-actions {
  display: flex; gap: 16rpx;
  padding: 16rpx 30rpx;
  border-top: 1rpx solid #eee;
  flex-wrap: wrap;
}
.btn {
  font-size: 26rpx; padding: 10rpx 24rpx;
  border-radius: 30rpx; line-height: 1.5;
  margin: 0;
}
.btn.primary { background: #6B4226; color: #fff; }
.btn.warn { background: #fff3e0; color: #ff9444; }
.btn.danger { background: #fff; color: #ff6b6b; border: 1rpx solid #ff6b6b; }
.btn.plain { background: #f0f0f0; color: #666; }
</style>
