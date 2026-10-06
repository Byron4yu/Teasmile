<template>
  <view class="od" v-if="order.id">
    <!-- 状态条 -->
    <view class="status-bar">
      <text class="st-text">{{ statusText(order.status) }}</text>
      <text class="st-desc" v-if="order.status === 2">商家正在备货，请耐心等待</text>
      <text class="st-desc" v-else-if="order.status === 5">感谢您的惠顾，欢迎再次光临</text>
    </view>

    <!-- 收货信息 -->
    <view class="card">
      <view class="card-row">
        <text class="consignee">{{ order.consignee }} {{ order.phone }}</text>
      </view>
      <text class="addr">{{ order.address }}</text>
    </view>

    <!-- 商品明细 -->
    <view class="card">
      <view class="item" v-for="d in order.orderDetailList" :key="d.id">
        <image class="item-img" :src="d.image" mode="aspectFill" v-if="d.image"></image>
        <view class="item-img placeholder" v-else>🥤</view>
        <view class="item-info">
          <text class="item-name">{{ d.name }}</text>
          <text class="item-flavor" v-if="d.drinkFlavor">{{ formatFlavor(d.drinkFlavor) }}</text>
        </view>
        <view class="item-right">
          <text class="item-num">x{{ d.number }}</text>
          <text class="item-amount">¥{{ d.amount }}</text>
        </view>
      </view>
      <view class="sum">
        <text>实付</text>
        <text class="sum-price">¥{{ order.amount }}</text>
      </view>
    </view>

    <!-- 订单信息 -->
    <view class="card">
      <view class="kv"><text>订单号</text><text>{{ order.number }}</text></view>
      <view class="kv"><text>下单时间</text><text>{{ formatTime(order.orderTime) }}</text></view>
      <view class="kv" v-if="order.remark"><text>备注</text><text>{{ order.remark }}</text></view>
    </view>

    <!-- 操作 -->
    <view class="actions">
      <button v-if="order.status === 1" class="btn danger" @click="cancel">取消订单</button>
      <button v-if="order.status === 5" class="btn primary" @click="repetition">再来一单</button>
    </view>
  </view>

  <view v-else class="loading">加载中...</view>
</template>

<script setup>
import { ref } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import { getOrderDetail, cancelOrder, repetitionOrder } from '@/api/index.js'

const order = ref({})
const orderId = ref(null)

const statusMap = { 1:'待付款', 2:'待接单', 3:'已接单', 4:'派送中', 5:'已完成', 6:'已取消', 7:'已退款' }
const statusText = (s) => statusMap[s] || '未知'
const formatTime = (t) => t ? String(t).replace('T', ' ').slice(0, 16) : ''
const formatFlavor = (json) => {
  try {
    const o = JSON.parse(json)
    return Object.entries(o).map(([k, v]) => `${k}:${v}`).join(' · ')
  } catch (e) { return '' }
}

const load = async () => {
  try {
    order.value = await getOrderDetail(orderId.value) || {}
  } catch (e) { order.value = {} }
}

const cancel = () => {
  uni.showModal({
    title:'提示', content:'确定取消订单吗？',
    success: async (r) => { if(r.confirm){ try{ await cancelOrder(orderId.value); load() }catch(e){} } }
  })
}
const repetition = async () => {
  try { await repetitionOrder(orderId.value); uni.showToast({ title:'已加入购物车' }); setTimeout(()=>uni.switchTab({url:'/pages/cart/cart'}),800) } catch(e){}
}

onLoad((options) => {
  orderId.value = (options && options.id) || null
  load()
})
</script>

<style scoped>
.od { min-height: 100vh; background: #f5f5f5; padding-bottom: 60rpx; }
.loading { text-align: center; padding: 120rpx; color: #999; }
.status-bar {
  background: linear-gradient(135deg, #BEAA96, #8B5E3C);
  color: #fff; padding: 50rpx 30rpx;
}
.st-text { font-size: 40rpx; font-weight: bold; display: block; }
.st-desc { font-size: 24rpx; opacity: 0.9; display: block; margin-top: 8rpx; }
.card { background: #fff; margin: 20rpx; padding: 30rpx; border-radius: 20rpx; }
.card-row { display: flex; }
.consignee { font-size: 32rpx; font-weight: bold; }
.addr { font-size: 26rpx; color: #666; margin-top: 10rpx; display: block; }
.item { display: flex; gap: 20rpx; padding: 16rpx 0; border-bottom: 1rpx solid #f0f0f0; }
.item:last-of-type { border-bottom: none; }
.item-img { width: 100rpx; height: 100rpx; border-radius: 12rpx; }
.item-img.placeholder { display:flex; align-items:center; justify-content:center; background:#f0e6d8; font-size:40rpx; }
.item-info { flex: 1; }
.item-name { font-size: 28rpx; font-weight: bold; display: block; }
.item-flavor { font-size: 22rpx; color: #999; margin-top: 6rpx; display: block; }
.item-right { display: flex; flex-direction: column; align-items: flex-end; justify-content: center; gap: 8rpx; }
.item-num { font-size: 24rpx; color: #999; }
.item-amount { font-size: 28rpx; color: #ff6b6b; font-weight: bold; }
.sum { display: flex; justify-content: flex-end; gap: 20rpx; align-items: baseline; margin-top: 16rpx; font-size: 28rpx; }
.sum-price { color: #ff6b6b; font-size: 36rpx; font-weight: bold; }
.kv { display: flex; justify-content: space-between; font-size: 26rpx; padding: 10rpx 0; color: #666; }
.actions { display: flex; gap: 20rpx; padding: 30rpx; }
.btn { flex: 1; font-size: 30rpx; border-radius: 44rpx; }
.btn.primary { background: #6B4226; color: #fff; }
.btn.danger { background: #fff; color: #ff6b6b; border: 1rpx solid #ff6b6b; }
</style>
