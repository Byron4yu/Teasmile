<template>
  <view class="order">
    <!-- 收货地址 -->
    <view class="addr-card" @click="chooseAddress">
      <block v-if="address">
        <view class="addr-info">
          <view class="addr-top">
            <text class="consignee">{{ address.consignee }}</text>
            <text class="phone">{{ address.phone }}</text>
            <text class="tag" v-if="address.isDefault === 1">默认</text>
          </view>
          <text class="addr-detail">{{ fullAddress(address) }}</text>
        </view>
        <text class="addr-arrow">›</text>
      </block>
      <block v-else>
        <text class="addr-empty">+ 请选择收货地址</text>
      </block>
    </view>

    <!-- 商品清单 -->
    <view class="goods-list">
      <view class="goods-item" v-for="item in cartList" :key="item.id">
        <text class="g-name">{{ item.name }} x{{ item.number }}</text>
        <text class="g-amount">¥{{ item.amount }}</text>
      </view>
      <view class="goods-total">
        <text>共 {{ totalCount }} 件</text>
        <text class="price">合计 ¥{{ totalPrice }}</text>
      </view>
    </view>

    <!-- 备注 -->
    <view class="remark">
      <textarea placeholder="备注（忌口/要求等）" v-model="remark" />
    </view>

    <!-- 支付方式 -->
    <view class="pay-method">
      <text class="pm-label">支付方式</text>
      <view class="pm-options">
        <text class="pm-opt active">微信支付</text>
      </view>
    </view>

    <!-- 底部支付栏 -->
    <view class="pay-bar">
      <view class="pay-total">
        <text>实付</text>
        <text class="pay-price">¥{{ totalPrice }}</text>
      </view>
      <view class="pay-btn" @click="submit">提交订单</view>
    </view>
  </view>
</template>

<script setup>
import { ref, computed } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import {
  getShoppingCartList, getDefaultAddress, getAddressList, submitOrder, payOrder, cleanShoppingCart
} from '@/api/index.js'

const cartList = ref([])
const address = ref(null)
const remark = ref('')
const submitting = ref(false)

const totalCount = computed(() => cartList.value.reduce((s, i) => s + (i.number || 0), 0))
const totalPrice = computed(() => cartList.value.reduce((s, i) => s + Number(i.amount || 0), 0).toFixed(2))

const fullAddress = (a) => {
  return [a.provinceName, a.cityName, a.districtName, a.detail].filter(Boolean).join('')
}

const loadCart = async () => {
  try {
    cartList.value = await getShoppingCartList() || []
  } catch (e) {
    cartList.value = []
  }
}

const loadAddress = async () => {
  // 1) 优先取用户在地址列表里点选的地址
  const picked = uni.getStorageSync('selected_address')
  if (picked && picked.id) {
    address.value = picked
    return
  }
  // 2) 其次取默认地址
  try {
    const def = await getDefaultAddress()
    if (def && def.id) {
      address.value = def
      return
    }
  } catch (e) { /* 无默认地址时继续往下回退 */ }
  // 3) 都没有时，回退取地址列表第一条（新增地址默认 isDefault=0，不会被默认接口查到）
  try {
    const list = await getAddressList() || []
    address.value = list.length > 0 ? list[0] : null
  } catch (e) {
    address.value = null
  }
}

const chooseAddress = () => {
  uni.navigateTo({ url: '/pages/address/list?select=1' })
}

const submit = async () => {
  if (submitting.value) return
  if (!address.value) {
    uni.showToast({ title: '请选择收货地址', icon: 'none' })
    return
  }
  if (cartList.value.length === 0) {
    uni.showToast({ title: '购物车为空', icon: 'none' })
    return
  }
  submitting.value = true
  uni.showLoading({ title: '提交中...' })
  try {
    // 1. 提交订单
    const submitVO = await submitOrder({
      addressBookId: address.value.id,
      payMethod: 1,
      remark: remark.value,
      deliveryStatus: 1,
      tablewareNumber: 1,
      tablewareStatus: 1,
      packAmount: 0,
      amount: Number(totalPrice.value)
    })
    // 2. 发起支付（无微信支付配置时失败不阻断下单）
    if (submitVO && submitVO.orderNumber) {
      try {
        await payOrder({ orderNumber: submitVO.orderNumber })
      } catch (e) { /* 忽略支付配置问题 */ }
    }
    // 3. 清空购物车
    try { await cleanShoppingCart() } catch (e) {}
    uni.removeStorageSync('selected_address')
    uni.showToast({ title: '下单成功', icon: 'success' })
    setTimeout(() => uni.switchTab({ url: '/pages/order-list/list' }), 1200)
  } catch (e) {
    /* 错误已统一提示 */
  } finally {
    submitting.value = false
    uni.hideLoading()
  }
}

onShow(() => {
  loadCart()
  loadAddress()
})
</script>

<style scoped>
.order {
  background: #f5f5f5;
  min-height: 100vh;
  padding-bottom: 140rpx;
}
.addr-card {
  background: #fff;
  margin: 20rpx;
  padding: 30rpx;
  border-radius: 20rpx;
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.addr-info { flex: 1; }
.addr-top {
  display: flex;
  align-items: center;
  gap: 16rpx;
  margin-bottom: 10rpx;
}
.consignee {
  font-size: 32rpx;
  font-weight: bold;
}
.phone { font-size: 28rpx; color: #666; }
.tag {
  font-size: 20rpx;
  color: #BEAA96;
  border: 1rpx solid #BEAA96;
  border-radius: 8rpx;
  padding: 2rpx 10rpx;
}
.addr-detail {
  font-size: 26rpx;
  color: #666;
  display: block;
}
.addr-arrow { font-size: 44rpx; color: #ccc; }
.addr-empty { color: #999; font-size: 30rpx; }
.goods-list {
  background: #fff;
  margin: 20rpx;
  padding: 30rpx;
  border-radius: 20rpx;
}
.goods-item {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20rpx;
  font-size: 28rpx;
}
.goods-total {
  display: flex;
  justify-content: space-between;
  border-top: 1rpx solid #eee;
  padding-top: 20rpx;
  font-size: 28rpx;
  font-weight: bold;
}
.goods-total .price { color: #ff6b6b; font-size: 32rpx; }
.remark {
  background: #fff;
  margin: 20rpx;
  padding: 30rpx;
  border-radius: 20rpx;
}
.remark textarea { width: 100%; height: 120rpx; font-size: 28rpx; }
.pay-method {
  background: #fff;
  margin: 20rpx;
  padding: 30rpx;
  border-radius: 20rpx;
}
.pm-label { font-size: 28rpx; font-weight: bold; display: block; margin-bottom: 16rpx; }
.pm-options { display: flex; gap: 16rpx; }
.pm-opt {
  padding: 10rpx 24rpx;
  border-radius: 30rpx;
  background: #f0f0f0;
  font-size: 26rpx;
  color: #666;
}
.pm-opt.active { background: #6B4226; color: #fff; }
.pay-bar {
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
.pay-total { display: flex; flex-direction: column; }
.pay-price { font-size: 44rpx; font-weight: bold; color: #ff6b6b; }
.pay-btn {
  background: #6B4226;
  padding: 20rpx 60rpx;
  border-radius: 48rpx;
  color: #fff;
  font-size: 32rpx;
  font-weight: bold;
}
</style>
