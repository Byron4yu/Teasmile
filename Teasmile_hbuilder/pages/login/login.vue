<template>
  <view class="login-page">
    <view class="logo-area">
      <image class="logo" src="/static/milktealogo_1.png" mode="aspectFit"></image>
      <text class="brand">🥤 茶颜悦色</text>
      <text class="slogan">新鲜现做 · 即刻下单</text>
    </view>

    <view class="login-area">
      <view class="wx-btn" @click="wxLogin">
        <text class="wx-icon">💬</text>
        <text>微信一键登录</text>
      </view>
      <text class="tip">登录即表示同意《用户协议》与《隐私政策》</text>
    </view>
  </view>
</template>

<script setup>
import { login } from '@/api/index.js'
import { TOKEN_KEY, USER_INFO_KEY } from '@/utils/request.js'

const wxLogin = () => {
  uni.login({
    provider: 'weixin',
    success: async (res) => {
      if (!res.code) {
        uni.showToast({ title: '获取登录凭证失败', icon: 'none' })
        return
      }
      uni.showLoading({ title: '登录中...' })
      try {
        // 后端凭 code 换取 openid 并签发 JWT，返回 { id, openid, token }
        const data = await login(res.code)
        if (data && data.token) {
          uni.setStorageSync(TOKEN_KEY, data.token)
          uni.setStorageSync(USER_INFO_KEY, {
            id: data.id,
            openid: data.openid
          })
          uni.showToast({ title: '登录成功', icon: 'success' })
          setTimeout(() => uni.navigateBack(), 1000)
        } else {
          uni.showToast({ title: '登录失败', icon: 'none' })
        }
      } catch (e) {
        /* 错误已统一提示 */
      } finally {
        uni.hideLoading()
      }
    },
    fail: () => {
      uni.showToast({ title: '微信登录失败，请在微信中打开', icon: 'none' })
    }
  })
}
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  background: linear-gradient(160deg, #F5EFE2 0%, #BEAA96 100%);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-between;
  padding: 160rpx 0 120rpx;
}
.logo-area { display: flex; flex-direction: column; align-items: center; }
.logo { width: 180rpx; height: 180rpx; margin-bottom: 30rpx; }
.brand { font-size: 48rpx; font-weight: bold; color: #6B4226; }
.slogan { font-size: 26rpx; color: #8B5E3C; margin-top: 12rpx; }
.login-area { display: flex; flex-direction: column; align-items: center; width: 100%; }
.wx-btn {
  display: flex; align-items: center; justify-content: center; gap: 16rpx;
  width: 80%; background: linear-gradient(135deg, #07C160, #06AD56);
  color: #fff; font-size: 34rpx; font-weight: bold;
  padding: 28rpx 0; border-radius: 60rpx;
  box-shadow: 0 8rpx 20rpx rgba(7,193,96,0.3);
}
.wx-icon { font-size: 38rpx; }
.tip { font-size: 22rpx; color: #8B5E3C; margin-top: 30rpx; opacity: 0.8; }
</style>
