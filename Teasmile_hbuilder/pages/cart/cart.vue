<template>
  <view class="cart">
    <view class="cart-header">
      <text class="title">🛒 购物车</text>
      <text class="clear" v-if="cartList.length" @click="clearCart">清空</text>
    </view>

    <view v-if="cartList.length === 0" class="empty">
      <text>🥤 购物车空空如也</text>
      <text class="go-buy" @click="goHome">去点单</text>
    </view>

    <view v-else>
      <view class="cart-list">
        <view class="cart-item" v-for="item in cartList" :key="item.id">
          <image class="item-img" :src="item.image" mode="aspectFill" v-if="item.image"></image>
          <view class="item-img placeholder" v-else>🥤</view>
          <view class="item-info">
            <text class="item-name">{{ item.name }}</text>
            <text class="item-spec" v-if="item.drinkFlavor">{{ formatFlavor(item.drinkFlavor) }}</text>
            <text class="item-price">¥{{ item.amount }}/{{ item.number }}份</text>
          </view>
          <view class="item-control">
            <view class="quantity">
              <view class="btn" @click="updateNum(item, -1)">-</view>
              <text class="num">{{ item.number }}</text>
              <view class="btn" @click="updateNum(item, 1)">+</view>
            </view>
            <text class="item-total">¥{{ item.amount }}</text>
          </view>
        </view>
      </view>

      <view class="cart-footer">
        <view class="total">
          <text>合计</text>
          <text class="total-price">¥{{ cartTotal }}</text>
        </view>
        <view class="checkout-btn" @click="checkout">去结算</view>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, computed } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import {
  getShoppingCartList, cleanShoppingCart,
  addShoppingCart, subShoppingCart
} from '@/api/index.js'
import { isLogin } from '@/utils/request.js'

const cartList = ref([])

const cartTotal = computed(() =>
  cartList.value.reduce((s, i) => s + Number(i.amount || 0), 0).toFixed(2)
)

// 格式化规格 JSON 串为展示文本
const formatFlavor = (json) => {
  try {
    const obj = JSON.parse(json)
    return Object.entries(obj).map(([k, v]) => `${k}:${v}`).join(' · ')
  } catch (e) {
    return ''
  }
}

const loadCart = async () => {
  if (!isLogin()) {
    cartList.value = []
    return
  }
  try {
    cartList.value = await getShoppingCartList() || []
  } catch (e) {
    cartList.value = []
  }
}

const updateNum = async (item, delta) => {
  try {
    if (delta > 0) {
      await addShoppingCart(item.drinkId, item.setmealId, item.drinkFlavor)
    } else {
      await subShoppingCart(item.drinkId, item.setmealId, item.drinkFlavor)
    }
    await loadCart()
  } catch (e) { /* 提示已统一处理 */ }
}

const clearCart = () => {
  uni.showModal({
    title: '提示',
    content: '确定清空购物车吗？',
    success: async (res) => {
      if (res.confirm) {
        try {
          await cleanShoppingCart()
          cartList.value = []
        } catch (e) {}
      }
    }
  })
}

const checkout = () => {
  if (!isLogin()) {
    uni.showModal({
      title: '提示',
      content: '请先登录再下单',
      success: (r) => r.confirm && uni.navigateTo({ url: '/pages/login/login' })
    })
    return
  }
  if (cartList.value.length === 0) return
  uni.navigateTo({ url: '/pages/order/order' })
}

const goHome = () => {
  uni.switchTab({ url: '/pages/index/index' })
}

onShow(() => {
  loadCart()
})
</script>

<style scoped>
.cart {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 140rpx;
}
.cart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 30rpx;
  background: #fff;
  border-bottom: 1rpx solid #eee;
}
.title {
  font-size: 36rpx;
  font-weight: bold;
}
.clear {
  color: #ff6b6b;
  font-size: 28rpx;
}
.empty {
  text-align: center;
  padding: 200rpx 0;
  color: #999;
}
.go-buy {
  display: inline-block;
  margin-top: 30rpx;
  background: #6B4226;
  color: #fff;
  padding: 16rpx 48rpx;
  border-radius: 48rpx;
}
.cart-list {
  padding: 20rpx;
}
.cart-item {
  background: #fff;
  border-radius: 20rpx;
  padding: 20rpx;
  margin-bottom: 20rpx;
  display: flex;
  gap: 20rpx;
}
.item-img {
  width: 120rpx;
  height: 120rpx;
  border-radius: 16rpx;
}
.item-img.placeholder {
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f0e6d8;
  font-size: 48rpx;
}
.item-info {
  flex: 1;
}
.item-name {
  font-size: 30rpx;
  font-weight: bold;
  display: block;
}
.item-spec {
  font-size: 24rpx;
  color: #999;
  margin: 8rpx 0;
  display: block;
}
.item-price {
  font-size: 24rpx;
  color: #ff6b6b;
}
.item-control {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  justify-content: space-between;
}
.quantity {
  display: flex;
  align-items: center;
  gap: 16rpx;
}
.quantity .btn {
  width: 48rpx;
  height: 48rpx;
  background: #f0f0f0;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 32rpx;
  font-weight: bold;
}
.quantity .num {
  font-size: 28rpx;
  min-width: 48rpx;
  text-align: center;
}
.item-total {
  font-size: 28rpx;
  font-weight: bold;
  color: #ff6b6b;
  margin-top: 20rpx;
}
.cart-footer {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background: #fff;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20rpx 30rpx;
  box-shadow: 0 -4rpx 12rpx rgba(0,0,0,0.05);
}
.total {
  display: flex;
  flex-direction: column;
}
.total-price {
  font-size: 40rpx;
  font-weight: bold;
  color: #ff6b6b;
}
.checkout-btn {
  background: #6B4226;
  padding: 20rpx 60rpx;
  border-radius: 48rpx;
  color: #fff;
  font-size: 32rpx;
  font-weight: bold;
}
</style>
