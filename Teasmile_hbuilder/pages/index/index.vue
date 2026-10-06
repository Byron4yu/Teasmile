<template>
  <view class="container">
    <!-- 头部 -->
    <view class="header">
      <view class="title-wrap">
        <text class="title">🥤 茶颜悦色</text>
        <text class="subtitle">新鲜现做 · 即刻下单</text>
      </view>
      <view class="cart-icon" @click="goCart">
        <text>🛒</text>
        <text class="badge" v-if="cartCount > 0">{{ cartCount }}</text>
      </view>
    </view>

    <!-- 营业状态提示 -->
    <view class="shop-status" v-if="shopStatus !== null">
      <text class="status-dot" :class="{ open: shopStatus === 1 }"></text>
      <text class="status-text">{{ shopStatus === 1 ? '营业中 · 欢迎下单' : '门店休息中 · 暂不接单' }}</text>
    </view>

    <!-- 分类侧栏 + 商品列表 -->
    <view class="main-content">
      <scroll-view class="category-sidebar" scroll-y show-scrollbar="false">
        <view
          class="sidebar-item"
          :class="{ active: currentCategoryId === item.id }"
          v-for="item in categories"
          :key="item.id"
          @click="selectCategory(item.id)"
        >
          {{ item.name }}
        </view>
      </scroll-view>

      <scroll-view class="menu-list" scroll-y show-scrollbar="false">
        <view v-if="loading" class="loading-state">加载中...</view>

        <view v-else>
          <view
            class="menu-item"
            v-for="item in menuList"
            :key="item.id"
            @click="handleItemClick(item)"
          >
            <image class="item-img" :src="item.image" mode="aspectFill" v-if="item.image"></image>
            <view class="item-img placeholder" v-else>🥤</view>
            <view class="item-info">
              <view class="item-name">
                {{ item.name }}
                <text class="hot-tag" v-if="item.hot === 1">🔥 热销</text>
              </view>
              <text class="item-desc">{{ item.description || '新鲜现做' }}</text>
              <view class="price-row">
                <text class="price">¥{{ item.price }}</text>
                <text class="origin-price" v-if="item.originalPrice">¥{{ item.originalPrice }}</text>
                <text class="sales" v-if="item.sales">月售{{ item.sales }}杯</text>
              </view>
            </view>
            <view class="add-btn" @click.stop="handleItemClick(item)">
              <text>+</text>
            </view>
          </view>

          <view v-if="menuList.length === 0" class="empty-state">
            该分类暂无商品
          </view>
        </view>
      </scroll-view>
    </view>

    <!-- 购物车悬浮栏 -->
    <view class="cart-float" v-if="cartList.length > 0" @click="goCart">
      <view class="cart-float-left">
        <text class="cart-icon">🛒</text>
        <text class="cart-count">{{ cartCount }}</text>
        <text class="cart-amount">¥{{ cartTotal }}</text>
      </view>
      <view class="cart-float-right">去下单</view>
    </view>
  </view>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import { getCategoryList, getDrinkList, getSetmealList, getShoppingCartList, getShopStatus, addShoppingCart } from '@/api/index.js'
import { isLogin } from '@/utils/request.js'

const categories = ref([])
const currentCategoryId = ref(null)
const currentCategoryType = ref(1)
const menuList = ref([])
const loading = ref(true)
const cartList = ref([])
const shopStatus = ref(null)

const cartCount = computed(() => cartList.value.reduce((s, i) => s + (i.number || 0), 0))
const cartTotal = computed(() => cartList.value.reduce((s, i) => s + Number(i.amount || 0), 0).toFixed(2))
const currentIsSetmeal = computed(() => currentCategoryType.value === 2)

// 加载营业状态
const loadShopStatus = async () => {
  try {
    shopStatus.value = await getShopStatus()
  } catch (e) {
    shopStatus.value = 1
  }
}

// 加载分类：饮品(type=1) + 套餐(type=2)
const loadCategories = async () => {
  try {
    const [drinkCats, setmealCats] = await Promise.all([
      getCategoryList(1).catch(() => []),
      getCategoryList(2).catch(() => [])
    ])
    categories.value = [...(drinkCats || []), ...(setmealCats || [])]
    if (categories.value.length > 0) {
      const first = categories.value[0]
      currentCategoryId.value = first.id
      currentCategoryType.value = first.type || 1
      await loadMenu(first)
    } else {
      menuList.value = []
    }
  } catch (e) {
    console.error('加载分类失败', e)
  } finally {
    loading.value = false
  }
}

// 按分类类型加载：饮品(type=1) 走 drink 接口，套餐(type=2) 走 setmeal 接口
const loadMenu = async (category) => {
  loading.value = true
  try {
    const isSetmeal = (category.type || 1) === 2
    const list = isSetmeal
      ? await getSetmealList(category.id)
      : await getDrinkList(category.id)
    menuList.value = list || []
  } catch (e) {
    menuList.value = []
  } finally {
    loading.value = false
  }
}

const selectCategory = (id) => {
  if (currentCategoryId.value === id) return
  const cat = categories.value.find(c => c.id === id)
  if (!cat) return
  currentCategoryId.value = id
  currentCategoryType.value = cat.type || 1
  loadMenu(cat)
}

// 点击商品卡片：饮品进详情选规格，套餐直接加入购物车
const handleItemClick = (item) => {
  if (currentIsSetmeal.value) {
    addSetmealToCart(item)
  } else {
    goDetail(item.id)
  }
}

// 套餐无需选规格，直接整份加入购物车
const addSetmealToCart = async (setmeal) => {
  if (!isLogin()) {
    uni.navigateTo({ url: '/pages/login/login' })
    return
  }
  try {
    await addShoppingCart(null, setmeal.id, null)
    uni.showToast({ title: '已加入购物车', icon: 'success' })
    loadCart()
  } catch (e) {
    uni.showToast({ title: '加购失败', icon: 'none' })
  }
}

// 加载购物车（需登录）
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

const goDetail = (id) => {
  // 后端无单品详情接口，将当前列表中的饮品对象暂存供详情页读取
  const drink = menuList.value.find(d => d.id === id)
  if (drink) {
    uni.setStorageSync('current_drink', drink)
  }
  uni.navigateTo({ url: `/pages/detail/detail?id=${id}` })
}

const goCart = () => {
  uni.navigateTo({ url: '/pages/cart/cart' })
}

onShow(() => {
  loadCart()
})

onMounted(() => {
  loadShopStatus()
  loadCategories()
  loadCart()
})
</script>

<style scoped>
.container {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 220rpx;
}
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 30rpx;
  background: linear-gradient(135deg, #BEAA96, #F5EFE2);
  color: #fff;
}
.title {
  font-size: 40rpx;
  font-weight: bold;
}
.subtitle {
  font-size: 24rpx;
  opacity: 0.85;
  margin-left: 12rpx;
}
.cart-icon {
  position: relative;
  font-size: 48rpx;
}
.badge {
  position: absolute;
  top: -10rpx;
  right: -20rpx;
  background: #ff4444;
  color: #fff;
  font-size: 20rpx;
  padding: 2rpx 10rpx;
  border-radius: 20rpx;
}
.shop-status {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12rpx;
  padding: 16rpx;
  background: #fff;
  font-size: 24rpx;
  color: #666;
}
.status-dot {
  width: 16rpx;
  height: 16rpx;
  border-radius: 50%;
  background: #ccc;
}
.status-dot.open {
  background: #4cd964;
}
.main-content {
  display: flex;
  padding: 20rpx;
  gap: 20rpx;
  height: calc(100vh - 320rpx);
}
.category-sidebar {
  width: 170rpx;
  background: #fff;
  border-radius: 20rpx;
  padding: 10rpx 0;
  flex-shrink: 0;
}
.sidebar-item {
  padding: 24rpx 16rpx;
  font-size: 26rpx;
  color: #666;
  text-align: center;
  border-radius: 12rpx;
  margin: 0 10rpx 8rpx;
}
.sidebar-item.active {
  color: #fff;
  background: #BEAA96;
  font-weight: bold;
}
.menu-list {
  flex: 1;
  padding-right: 10rpx;
}
.menu-item {
  display: flex;
  background: #fff;
  margin-bottom: 20rpx;
  padding: 20rpx;
  border-radius: 20rpx;
  position: relative;
}
.item-img {
  width: 160rpx;
  height: 160rpx;
  border-radius: 16rpx;
  margin-right: 20rpx;
}
.item-img.placeholder {
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f0e6d8;
  font-size: 60rpx;
}
.item-info {
  flex: 1;
}
.item-name {
  font-size: 32rpx;
  font-weight: bold;
  margin-bottom: 8rpx;
}
.hot-tag {
  font-size: 22rpx;
  color: #ff6b6b;
  margin-left: 12rpx;
}
.item-desc {
  font-size: 24rpx;
  color: #999;
  margin-bottom: 12rpx;
}
.price-row {
  display: flex;
  align-items: baseline;
  gap: 16rpx;
}
.price {
  font-size: 36rpx;
  font-weight: bold;
  color: #ff6b6b;
}
.origin-price {
  font-size: 24rpx;
  color: #ccc;
  text-decoration: line-through;
}
.sales {
  font-size: 22rpx;
  color: #999;
}
.add-btn {
  position: absolute;
  bottom: 20rpx;
  right: 20rpx;
  width: 56rpx;
  height: 56rpx;
  background: #BEAA96;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 40rpx;
  font-weight: bold;
}
.cart-float {
  position: fixed;
  bottom: calc(120rpx + env(safe-area-inset-bottom));
  left: 30rpx;
  right: 30rpx;
  background: #333;
  border-radius: 60rpx;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20rpx 30rpx;
  color: #fff;
  box-shadow: 0 8rpx 24rpx rgba(0,0,0,0.2);
  z-index: 999;
}
.cart-float-left {
  display: flex;
  align-items: center;
  gap: 16rpx;
}
.cart-float-left .cart-icon {
  font-size: 44rpx;
}
.cart-count {
  background: #ff6b6b;
  padding: 4rpx 16rpx;
  border-radius: 30rpx;
  font-size: 24rpx;
}
.cart-amount {
  font-size: 32rpx;
  font-weight: bold;
}
.cart-float-right {
  background: #ff6b6b;
  padding: 12rpx 32rpx;
  border-radius: 40rpx;
  font-size: 28rpx;
}
.loading-state,
.empty-state {
  text-align: center;
  padding: 60rpx;
  color: #999;
  font-size: 28rpx;
}
</style>
