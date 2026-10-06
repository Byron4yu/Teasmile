<template>
  <view class="user-page">
    <!-- 头部 -->
    <view class="user-header" @click="handleHeader">
      <image class="avatar" :src="userInfo.avatar || '/static/milktealogo_1.png'" mode="aspectFill"></image>
      <view class="user-info">
        <text class="nickname">{{ isLogin() ? (userInfo.nickname || '微信用户') : '点击登录' }}</text>
        <text class="phone" v-if="isLogin()">ID: {{ userInfo.id }}</text>
      </view>
    </view>

    <!-- 订单入口 -->
    <view class="order-status" v-if="isLogin()">
      <view class="status-item" @click="goOrders(null)">
        <text class="status-num">{{ orderCounts.all }}</text>
        <text>全部订单</text>
      </view>
      <view class="status-item" @click="goOrders(1)">
        <text class="status-num">{{ orderCounts.pending }}</text>
        <text>待付款</text>
      </view>
      <view class="status-item" @click="goOrders(2)">
        <text class="status-num">{{ orderCounts.ready }}</text>
        <text>待接单</text>
      </view>
      <view class="status-item" @click="goOrders(5)">
        <text class="status-num">{{ orderCounts.done }}</text>
        <text>已完成</text>
      </view>
    </view>

    <!-- 未登录提示 -->
    <view class="login-prompt" v-if="!isLogin()">
      <view class="login-btn" @click="handleHeader">
        <text>🔑 微信一键登录</text>
      </view>
      <text class="login-desc">登录后查看订单与收货地址</text>
    </view>

    <!-- 功能菜单 -->
    <view class="menu-list" v-if="isLogin()">
      <view class="menu-item" @click="goAddress">
        <text>📍 收货地址</text>
        <text class="arrow">›</text>
      </view>
      <view class="menu-item" @click="callService">
        <text>📞 联系门店</text>
        <text class="arrow">›</text>
      </view>
      <view class="menu-item" @click="about">
        <text>ℹ️ 关于茶颜悦色</text>
        <text class="arrow">›</text>
      </view>
    </view>

    <button v-if="isLogin()" class="logout-btn" @click="handleLogout">退出登录</button>
  </view>
</template>

<script setup>
import { ref, computed } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import { getHistoryOrders, getMerchantInfo } from '@/api/index.js'
import { isLogin, USER_INFO_KEY, TOKEN_KEY } from '@/utils/request.js'

const userInfo = ref({})
const orderCounts = ref({ all: 0, pending: 0, ready: 0, done: 0 })

const loadUser = () => {
  userInfo.value = uni.getStorageSync(USER_INFO_KEY) || {}
}

const loadCounts = async () => {
  if (!isLogin()) { orderCounts.value = { all: 0, pending: 0, ready: 0, done: 0 }; return }
  try {
    const res = await getHistoryOrders({ page: 1, pageSize: 100 })
    const list = (res && res.records) ? res.records : []
    orderCounts.value = {
      all: list.length,
      pending: list.filter(o => o.status === 1).length,
      ready: list.filter(o => o.status === 2).length,
      done: list.filter(o => o.status === 5).length
    }
  } catch (e) {}
}

const handleHeader = () => {
  if (isLogin()) return
  uni.navigateTo({ url: '/pages/login/login' })
}

const handleLogout = () => {
  uni.showModal({
    title: '提示', content: '确定退出登录吗？',
    success: (r) => {
      if (r.confirm) {
        uni.removeStorageSync(TOKEN_KEY)
        uni.removeStorageSync(USER_INFO_KEY)
        userInfo.value = {}
        uni.showToast({ title: '已退出' })
      }
    }
  })
}

const goOrders = () => uni.switchTab({ url: '/pages/order-list/list' })
const goAddress = () => uni.navigateTo({ url: '/pages/address/list' })
const callService = async () => {
  try {
    const phone = await getMerchantInfo()
    if (phone) uni.makePhoneCall({ phoneNumber: String(phone) })
  } catch (e) {}
}
const about = () => uni.showModal({ title: '关于', content: '茶颜悦色点单小程序 v1.0\n毕业设计作品', showCancel: false })

onShow(() => { loadUser(); loadCounts() })
</script>

<style scoped>
.user-page { min-height: 100vh; background: #f5f5f5; padding-bottom: 40rpx; }
.user-header {
  background: linear-gradient(135deg, #BEAA96, #8B5E3C);
  padding: 60rpx 40rpx;
  display: flex;
  align-items: center;
  gap: 30rpx;
}
.avatar { width: 120rpx; height: 120rpx; border-radius: 50%; border: 4rpx solid #fff; background:#fff; }
.user-info { flex: 1; }
.nickname { font-size: 36rpx; font-weight: bold; color: #fff; display: block; }
.phone { font-size: 24rpx; color: rgba(255,255,255,0.85); margin-top: 8rpx; display: block; }
.order-status {
  background: #fff; margin: 30rpx; padding: 30rpx;
  border-radius: 24rpx; display: flex; justify-content: space-around;
}
.status-item { text-align: center; font-size: 24rpx; color: #666; }
.status-num { font-size: 40rpx; font-weight: bold; color: #BEAA96; display: block; }
.login-prompt {
  background: #fff; margin: 30rpx; padding: 60rpx 30rpx;
  border-radius: 24rpx; text-align: center;
}
.login-btn {
  background: linear-gradient(135deg, #07C160, #06AD56);
  color: #fff; padding: 24rpx 40rpx; border-radius: 60rpx;
  font-size: 32rpx; font-weight: bold; margin-bottom: 20rpx; display: inline-block;
}
.login-desc { font-size: 24rpx; color: #999; display: block; }
.menu-list {
  background: #fff; margin: 0 30rpx; border-radius: 24rpx; overflow: hidden;
}
.menu-item {
  display: flex; justify-content: space-between; align-items: center;
  padding: 30rpx; border-bottom: 1rpx solid #eee; font-size: 28rpx;
}
.menu-item:last-child { border-bottom: none; }
.arrow { color: #ccc; font-size: 36rpx; }
.logout-btn {
  margin: 60rpx 30rpx; background: #fff; color: #ff4444;
  border: 1rpx solid #ffdddd; border-radius: 48rpx;
}
</style>
