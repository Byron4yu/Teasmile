<template>
  <view class="addr-list">
    <view v-if="list.length === 0" class="empty">
      <text>📍 暂无收货地址</text>
    </view>

    <view class="list">
      <view class="addr-item" v-for="item in list" :key="item.id" @click="onTap(item)">
        <view class="main">
          <view class="top">
            <text class="name">{{ item.consignee }}</text>
            <text class="phone">{{ item.phone }}</text>
            <text class="tag" v-if="item.isDefault === 1">默认</text>
          </view>
          <text class="detail">{{ fullAddress(item) }}</text>
        </view>
        <view class="ops" @click.stop>
          <text class="op" @click="setDefault(item)" v-if="item.isDefault !== 1">设为默认</text>
          <text class="op" @click="goEdit(item)">编辑</text>
          <text class="op danger" @click="remove(item)">删除</text>
        </view>
      </view>
    </view>

    <view class="footer">
      <view class="add-btn" @click="goEdit()">+ 新增收货地址</view>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { onShow, onLoad } from '@dcloudio/uni-app'
import { getAddressList, setDefaultAddress, deleteAddress } from '@/api/index.js'

const list = ref([])
const selectMode = ref(false)

const fullAddress = (a) =>
  [a.provinceName, a.cityName, a.districtName, a.detail].filter(Boolean).join('')

const load = async () => {
  try { list.value = await getAddressList() || [] } catch (e) { list.value = [] }
}

const onTap = (item) => {
  if (selectMode.value) {
    uni.setStorageSync('selected_address', item)
    uni.navigateBack()
  }
}

const setDefault = async (item) => {
  try { await setDefaultAddress(item.id); uni.showToast({ title: '已设为默认' }); load() } catch (e) {}
}

const remove = (item) => {
  uni.showModal({
    title:'提示', content:'确定删除该地址吗？',
    success: async (r) => { if(r.confirm){ try{ await deleteAddress(item.id); load() }catch(e){} } }
  })
}

const goEdit = (item) => {
  const url = item ? `/pages/address/edit?id=${item.id}` : '/pages/address/edit'
  uni.navigateTo({ url })
}

onLoad((options) => {
  selectMode.value = !!(options && options.select === '1')
})

onShow(() => {
  load()
})
</script>

<style scoped>
.addr-list { min-height: 100vh; background: #f5f5f5; padding-bottom: 140rpx; }
.empty { text-align: center; padding: 200rpx 0; color: #999; }
.list { padding: 20rpx; }
.addr-item {
  background: #fff; border-radius: 20rpx; padding: 24rpx 30rpx;
  margin-bottom: 20rpx;
}
.top { display: flex; align-items: center; gap: 16rpx; margin-bottom: 10rpx; }
.name { font-size: 32rpx; font-weight: bold; }
.phone { font-size: 28rpx; color: #666; }
.tag { font-size: 20rpx; color: #BEAA96; border: 1rpx solid #BEAA96; border-radius: 8rpx; padding: 2rpx 10rpx; }
.detail { font-size: 26rpx; color: #666; display: block; }
.ops { display: flex; gap: 30rpx; margin-top: 16rpx; padding-top: 16rpx; border-top: 1rpx solid #f0f0f0; }
.op { font-size: 26rpx; color: #6B4226; }
.op.danger { color: #ff6b6b; }
.footer { position: fixed; bottom: 0; left: 0; right: 0; padding: 20rpx 30rpx; background: #fff; box-shadow: 0 -4rpx 12rpx rgba(0,0,0,0.05); }
.add-btn {
  background: #6B4226; color: #fff; text-align: center;
  padding: 24rpx 0; border-radius: 48rpx; font-size: 32rpx; font-weight: bold;
}
</style>
