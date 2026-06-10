import request from './request'

export function getMarginAccountList(params) {
  return request({
    url: '/margin-account/list',
    method: 'get',
    params
  })
}

export function getMarginAccount(id) {
  return request({
    url: `/margin-account/${id}`,
    method: 'get'
  })
}

export function saveMarginAccount(data) {
  return request({
    url: '/margin-account',
    method: 'post',
    data
  })
}

export function updateMarginAccount(data) {
  return request({
    url: '/margin-account',
    method: 'put',
    data
  })
}

export function approveMarginAccount(data) {
  return request({
    url: '/margin-account/approve',
    method: 'post',
    data
  })
}

export function rechargeMarginAccount(data) {
  return request({
    url: '/margin-account/recharge',
    method: 'post',
    data
  })
}

export function refundMarginAccount(data) {
  return request({
    url: '/margin-account/refund',
    method: 'post',
    data
  })
}

export function closeMarginAccount(data) {
  return request({
    url: '/margin-account/close',
    method: 'post',
    data
  })
}

export function getChannelList() {
  return request({
    url: '/margin-account/channels',
    method: 'get'
  })
}