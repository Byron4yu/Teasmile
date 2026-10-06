<template>
  <view class="detail">
    <view v-if="!goods.id" class="loading">加载中...</view>

    <block v-else>
      <!-- 商品图片 -->
      <image class="main-img" :src="goods.image" mode="aspectFill" v-if="goods.image"></image>
      <view class="main-img placeholder" v-else>🥤</view>

      <!-- 商品信息 -->
      <view class="info-card">
        <text class="name">{{ goods.name }}</text>
        <text class="desc">{{ goods.description || '新鲜现做，口感醇厚' }}</text>
        <view class="price">
          <text class="current">¥{{ goods.price }}</text>
          <text class="origin" v-if="goods.originalPrice">¥{{ goods.originalPrice }}</text>
        </view>
        <text class="sales" v-if="goods.sales">月售 {{ goods.sales }} 杯</text>
      </view>

      <!-- 规格选择（甜度/冰度/加料 等，由后端 flavors 动态渲染） -->
      <view class="spec-card" v-if="flavorGroups.length">
        <view class="spec-item" v-for="group in flavorGroups" :key="group.name">
          <text class="spec-label">{{ group.name }}</text>
          <view class="spec-options">
            <view
              class="spec-option"
              :class="{ active: selected[group.name] === opt }"
              v-for="opt in group.options"
              :key="opt"
              @click="selected[group.name] = opt"
            >
              {{ opt }}
            </view>
          </view>
        </view>
      </view>

      <!-- 数量 -->
      <view class="quantity-card">
        <text class="label">数量</text>
        <view class="quantity-control">
          <view class="btn" @click="quantity = Math.max(1, quantity - 1)">-</view>
          <text class="num">{{ quantity }}</text>
          <view class="btn" @click="quantity++">+</view>
        </view>
      </view>

      <!-- 底部栏 -->
      <view class="bottom-bar">
        <view class="total">
          <text class="total-label">合计</text>
          <text class="total-price">¥{{ finalPrice }}</text>
        </view>
        <view class="add-cart-btn" @click="addToCart">加入购物车</view>
      </view>
    </block>
  </view>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { addShoppingCart } from '@/api/index.js'
import { isLogin } from '@/utils/request.js'

const goods = ref({})
const quantity = ref(1)
// 各规格维度选中值，如 { 甜度: '半糖', 冰度: '少冰' }
const selected = reactive({})

// 解析后端 flavors: [{ name, value: '["无糖","少糖"]' }]
const flavorGroups = computed(() => {
  if (!goods.value.flavors) return []
  return goods.value.flavors.map(f => {
    let options = []
    try {
      options = JSON.parse(f.value)
    } catch (e) {
      options = []
    }
    // 默认选中第一个
    if (options.length && selected[f.name] === undefined) {
      selected[f.name] = options[0]
    }
    return { name: f.name, options }
  }).filter(g => g.options.length > 0)
})

const finalPrice = computed(() => {
  if (!goods.value.price) return '0.00'
  return (Number(goods.value.price) * quantity.value).toFixed(2)
})

const addToCart = async () => {
  if (!isLogin()) {
    uni.showModal({
      title: '提示',
      content: '请先登录后再加购',
      success: (res) => {
        if (res.confirm) uni.navigateTo({ url: '/pages/login/login' })
      }
    })
    return
  }
  // 拼规格组合 JSON，如 {"甜度":"半糖","冰度":"少冰"}
  const drinkFlavor = JSON.stringify(selected)

  uni.showLoading({ title: '加入中...' })
  try {
    // 后端按「同一商品+同一规格」累加数量，前端只需把每次加购作为一次 add 请求
    await addShoppingCart(goods.value.id, null, drinkFlavor)
    uni.showToast({ title: '已加入购物车', icon: 'success' })
    setTimeout(() => uni.navigateBack(), 800)
  } catch (e) {
    // 错误提示已在 request 层处理
  } finally {
    uni.hideLoading()
  }
}

onMounted(() => {
  const drink = uni.getStorageSync('current_drink')
  if (drink && drink.id) {
    goods.value = drink
  } else {
    uni.showToast({ title: '商品信息缺失', icon: 'none' })
  }
})
</script>

<style scoped>
.detail {
  background: #f5f5f5;
  min-height: 100vh;
  padding-bottom: 140rpx;
}
.loading {
  text-align: center;
  padding: 120rpx;
  color: #999;
}
.main-img {
  width: 100%;
  height: 500rpx;
}
.main-img.placeholder {
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f0e6d8;
  font-size: 160rpx;
}
.info-card, .spec-card, .quantity-card {
  background: #fff;
  margin: 20rpx;
  padding: 30rpx;
  border-radius: 24rpx;
}
.name {
  font-size: 40rpx;
  font-weight: bold;
  display: block;
}
.desc {
  font-size: 26rpx;
  color: #999;
  margin: 12rpx 0;
  display: block;
}
.price {
  margin: 16rpx 0;
}
.current {
  font-size: 44rpx;
  font-weight: bold;
  color: #ff6b6b;
}
.origin {
  font-size: 28rpx;
  color: #ccc;
  text-decoration: line-through;
  margin-left: 16rpx;
}
.sales {
  font-size: 24rpx;
  color: #999;
}
.spec-item {
  margin-bottom: 32rpx;
}
.spec-item:last-child {
  margin-bottom: 0;
}
.spec-label {
  font-size: 28rpx;
  font-weight: bold;
  display: block;
  margin-bottom: 16rpx;
}
.spec-options {
  display: flex;
  flex-wrap: wrap;
  gap: 16rpx;
}
.spec-option {
  background: #f0f0f0;
  padding: 12rpx 24rpx;
  border-radius: 40rpx;
  font-size: 26rpx;
  color: #666;
}
.spec-option.active {
  background: #6B4226;
  color: #fff;
}
.quantity-card {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.label {
  font-size: 30rpx;
  font-weight: bold;
}
.quantity-control {
  display: flex;
  align-items: center;
  gap: 20rpx;
}
.quantity-control .btn {
  width: 56rpx;
  height: 56rpx;
  background: #f0f0f0;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 40rpx;
  font-weight: bold;
}
.quantity-control .num {
  font-size: 32rpx;
  font-weight: bold;
  min-width: 60rpx;
  text-align: center;
}
.bottom-bar {
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
.total-label {
  font-size: 24rpx;
  color: #999;
}
.total-price {
  font-size: 44rpx;
  font-weight: bold;
  color: #ff6b6b;
}
.add-cart-btn {
  background: #6B4226;
  padding: 20rpx 60rpx;
  border-radius: 48rpx;
  color: #fff;
  font-size: 32rpx;
  font-weight: bold;
}
</style>
