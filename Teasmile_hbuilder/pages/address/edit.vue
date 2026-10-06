<template>
  <view class="edit">
    <view class="form">
      <view class="form-item">
        <text class="label">收货人</text>
        <input class="input" v-model="form.consignee" placeholder="请输入姓名" />
      </view>
      <view class="form-item">
        <text class="label">手机号</text>
        <input class="input" v-model="form.phone" type="number" maxlength="11" placeholder="请输入手机号" />
      </view>
      <view class="form-item">
        <text class="label">性别</text>
        <view class="sex-group">
          <text class="sex" :class="{ active: form.sex === '1' }" @click="form.sex = '1'">先生</text>
          <text class="sex" :class="{ active: form.sex === '0' }" @click="form.sex = '0'">女士</text>
        </view>
      </view>
      <view class="form-item">
        <text class="label">所在地区</text>
        <picker mode="region" :value="regionValue" @change="onRegionChange">
          <view class="picker-value" :class="{ placeholder: !regionText }">{{ regionText || '请选择省 / 市 / 区' }}</view>
        </picker>
      </view>
      <view class="form-item column">
        <text class="label">详细地址</text>
        <textarea class="textarea" v-model="form.detail" placeholder="街道、门牌号等" />
      </view>
      <view class="form-item">
        <text class="label">标签</text>
        <input class="input" v-model="form.label" placeholder="家 / 公司 / 学校（选填）" />
      </view>
      <view class="form-item">
        <text class="label">设为默认</text>
        <switch :checked="form.isDefault === 1" color="#BEAA96" @change="onDefaultChange" />
      </view>
    </view>

    <view class="footer">
      <view class="save-btn" @click="save">保存地址</view>
    </view>
  </view>
</template>

<script setup>
import { reactive, ref, computed } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import { getAddressById, saveAddress, updateAddress } from '@/api/index.js'

const id = ref(null)
const form = reactive({
  consignee: '', phone: '', sex: '1',
  provinceName: '', cityName: '', districtName: '',
  detail: '', label: '', isDefault: 0
})

// 地区选择器：回显值 + 展示文本
const regionValue = computed(() => [form.provinceName, form.cityName, form.districtName])
const regionText = computed(() =>
  [form.provinceName, form.cityName, form.districtName].filter(Boolean).join(' ')
)
const onRegionChange = (e) => {
  const [p, c, d] = e.detail.value
  form.provinceName = p
  form.cityName = c
  form.districtName = d
}

const onDefaultChange = (e) => { form.isDefault = e.detail.value ? 1 : 0 }

const load = async () => {
  if (!id.value) return
  try {
    const data = await getAddressById(id.value)
    if (data) Object.assign(form, data)
  } catch (e) {}
}

const save = async () => {
  if (!form.consignee) return uni.showToast({ title: '请填写收货人', icon: 'none' })
  if (!/^1\d{10}$/.test(form.phone)) return uni.showToast({ title: '手机号格式不正确', icon: 'none' })
  if (!form.provinceName) return uni.showToast({ title: '请选择所在地区', icon: 'none' })
  if (!form.detail) return uni.showToast({ title: '请填写详细地址', icon: 'none' })

  uni.showLoading({ title: '保存中...' })
  try {
    const payload = { ...form, isDefault: Number(form.isDefault) }
    if (id.value) {
      payload.id = id.value
      await updateAddress(payload)
    } else {
      await saveAddress(payload)
    }
    uni.showToast({ title: '保存成功', icon: 'success' })
    setTimeout(() => uni.navigateBack(), 800)
  } catch (e) {} finally { uni.hideLoading() }
}

onLoad((options) => {
  id.value = (options && options.id) || null
  load()
})
</script>

<style scoped>
.edit { min-height: 100vh; background: #f5f5f5; padding-bottom: 140rpx; }
.form { background: #fff; margin: 20rpx; border-radius: 20rpx; padding: 10rpx 30rpx; }
.form-item {
  display: flex; align-items: center; justify-content: space-between;
  padding: 24rpx 0; border-bottom: 1rpx solid #f0f0f0;
}
.form-item:last-child { border-bottom: none; }
.form-item.column { flex-direction: column; align-items: flex-start; gap: 16rpx; }
.label { font-size: 28rpx; color: #333; width: 160rpx; flex-shrink: 0; }
.input { flex: 1; font-size: 28rpx; text-align: right; }
.picker-value { flex: 1; font-size: 28rpx; text-align: right; color: #333; }
.picker-value.placeholder { color: #bbb; }
.textarea { width: 100%; height: 140rpx; font-size: 28rpx; }
.sex-group { display: flex; gap: 20rpx; }
.sex {
  padding: 8rpx 28rpx; border-radius: 30rpx;
  background: #f0f0f0; font-size: 26rpx; color: #666;
}
.sex.active { background: #6B4226; color: #fff; }
.footer { position: fixed; bottom: 0; left: 0; right: 0; padding: 20rpx 30rpx; background: #fff; box-shadow: 0 -4rpx 12rpx rgba(0,0,0,0.05); }
.save-btn {
  background: #6B4226; color: #fff; text-align: center;
  padding: 24rpx 0; border-radius: 48rpx; font-size: 32rpx; font-weight: bold;
}
</style>
