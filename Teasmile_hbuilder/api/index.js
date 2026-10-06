/**
 * API 层 - 对齐 teasmile-backend 用户端接口
 * 所有接口返回 Promise，业务数据已在 request 层解包（ApiResult.data）
 */
import request from '@/utils/request.js'

/* ---------------- 用户登录 ---------------- */
export const login = (code) => request({
  url: '/user/user/login',
  method: 'POST',
  data: { code },
  auth: false
})

/* ---------------- 店铺 ---------------- */
export const getShopStatus = () => request({
  url: '/user/shop/status',
  method: 'GET',
  auth: false
})

export const getMerchantInfo = () => request({
  url: '/user/shop/getMerchantInfo',
  method: 'GET'
})

/* ---------------- 分类 ---------------- */
// type: 1 饮品分类, 2 套餐分类
export const getCategoryList = (type) => request({
  url: '/user/category/list',
  method: 'GET',
  data: { type }
})

/* ---------------- 饮品 ---------------- */
export const getDrinkList = (categoryId) => request({
  url: '/user/drink/list',
  method: 'GET',
  data: { categoryId }
})

/* ---------------- 套餐 ---------------- */
export const getSetmealList = (categoryId) => request({
  url: '/user/setmeal/list',
  method: 'GET',
  data: { categoryId }
})

export const getSetmealDrinkList = (id) => request({
  url: `/user/setmeal/drink/${id}`,
  method: 'GET'
})

/* ---------------- 购物车 ---------------- */
export const addShoppingCart = (drinkId, setmealId, drinkFlavor) => request({
  url: '/user/shoppingCart/add',
  method: 'POST',
  data: { drinkId, setmealId, drinkFlavor }
})

export const getShoppingCartList = () => request({
  url: '/user/shoppingCart/list',
  method: 'GET'
})

export const cleanShoppingCart = () => request({
  url: '/user/shoppingCart/clean',
  method: 'DELETE'
})

export const subShoppingCart = (drinkId, setmealId, drinkFlavor) => request({
  url: '/user/shoppingCart/sub',
  method: 'POST',
  data: { drinkId, setmealId, drinkFlavor }
})

/* ---------------- 订单 ---------------- */
export const submitOrder = (data) => request({
  url: '/user/order/submit',
  method: 'POST',
  data
})

export const payOrder = (data) => request({
  url: '/user/order/payment',
  method: 'PUT',
  data
})

export const getOrderDetail = (id) => request({
  url: `/user/order/orderDetail/${id}`,
  method: 'GET'
})

// params: { page, pageSize, status }
export const getHistoryOrders = (params) => request({
  url: '/user/order/historyOrders',
  method: 'GET',
  data: params
})

export const reminderOrder = (id) => request({
  url: `/user/order/reminder/${id}`,
  method: 'GET'
})

export const repetitionOrder = (id) => request({
  url: `/user/order/repetition/${id}`,
  method: 'POST'
})

export const cancelOrder = (id) => request({
  url: `/user/order/cancel/${id}`,
  method: 'PUT'
})

/* ---------------- 地址簿 ---------------- */
export const getAddressList = () => request({
  url: '/user/addressBook/list',
  method: 'GET'
})

export const saveAddress = (data) => request({
  url: '/user/addressBook',
  method: 'POST',
  data
})

export const getAddressById = (id) => request({
  url: `/user/addressBook/${id}`,
  method: 'GET'
})

export const updateAddress = (data) => request({
  url: '/user/addressBook',
  method: 'PUT',
  data
})

export const setDefaultAddress = (id) => request({
  url: '/user/addressBook/default',
  method: 'PUT',
  data: { id }
})

export const deleteAddress = (id) => request({
  url: '/user/addressBook',
  method: 'DELETE',
  data: { id }
})

export const getDefaultAddress = () => request({
  url: '/user/addressBook/default',
  method: 'GET'
})
