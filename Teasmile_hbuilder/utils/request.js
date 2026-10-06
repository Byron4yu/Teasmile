/**
 * 网络请求封装 - 对齐 teasmile-backend
 * 统一响应格式 ApiResult: { code: 1成功/0失败, msg, data }
 * 用户端 JWT 请求头名称：authentication
 */

// 后端服务地址（按需修改）
export const BASE_URL = 'http://localhost:8080'

// 本地存储 key
export const TOKEN_KEY = 'teasmile_token'
export const USER_INFO_KEY = 'teasmile_user_info'

/**
 * 获取登录 token
 */
export const getToken = () => uni.getStorageSync(TOKEN_KEY) || ''

/**
 * 是否已登录
 */
export const isLogin = () => !!getToken()

/**
 * 统一请求方法
 * @param {Object} options { url, method, data, header, auth }
 */
const request = (options = {}) => {
  return new Promise((resolve, reject) => {
    const token = getToken()
    const header = {
      'Content-Type': 'application/json',
      ...(options.header || {})
    }
    // 需要登录的接口自动携带 token
    if (options.auth !== false && token) {
      header['authentication'] = token
    }

    uni.request({
      url: BASE_URL + options.url,
      method: options.method || 'GET',
      data: options.data || {},
      header,
      success: (res) => {
        const { statusCode, data } = res
        // 401 未授权
        if (statusCode === 401) {
          uni.removeStorageSync(TOKEN_KEY)
          uni.removeStorageSync(USER_INFO_KEY)
          uni.showToast({ title: '登录已过期，请重新登录', icon: 'none' })
          // 跳登录页（避免重复跳转）
          const pages = getCurrentPages()
          const cur = pages[pages.length - 1]
          if (cur && cur.route !== 'pages/login/login') {
            uni.navigateTo({ url: '/pages/login/login' })
          }
          reject(new Error('未授权'))
          return
        }
        // 业务响应
        if (data && typeof data.code !== 'undefined') {
          if (data.code === 1) {
            resolve(data.data)
          } else {
            uni.showToast({ title: data.msg || '请求失败', icon: 'none' })
            reject(new Error(data.msg || '请求失败'))
          }
        } else {
          resolve(data)
        }
      },
      fail: (err) => {
        uni.showToast({ title: '网络异常，请检查服务是否启动', icon: 'none' })
        reject(err)
      }
    })
  })
}

export default request
